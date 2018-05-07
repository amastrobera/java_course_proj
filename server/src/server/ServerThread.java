package server; 

import comm.*;
import canteen.*;
import university.*;

import java.net.Socket;
import java.io.*;
import java.util.concurrent.Semaphore;
import java.util.ArrayList;


class ServerThread implements Runnable {

    private final String mDataPath;
    private Socket mClient;
    private Semaphore mSemaphore;
    private DataManager mDataManager;

    public ServerThread(String dataPath, Socket client, Semaphore sem) {
        mDataPath = dataPath;
        mClient = client;
        mSemaphore = sem;
        mDataManager = new FileDataManager(mDataPath);
    }
    
    public ServerThread(Socket client) {
        this("data", client, null);
    }
    
    private void prepareResponse(ObjectOutputStream output, Request req) 
                                                            throws IOException {            
        String type = req.type();
        
        if (type.equals("ViewUsers")) {
            
            ViewUsersResponse res = new ViewUsersResponse();
            res.setUserList(mDataManager.getUsers());
            res.setStatus(Response.Status.SUCCESS);
            
            output.writeObject(res);
            System.out.println("sending back: " + res.toString());
            
        } else if (type.equals("ViewCourses")) {
            
            ViewCoursesResponse res = new ViewCoursesResponse();
            res.setUserList(mDataManager.getCourses());
            res.setStatus(Response.Status.SUCCESS);
            
            output.writeObject(res);
            System.out.println("sending back: " + res.toString());
            
        } else if (type.equals("ViewCourseInfo")) {

            ViewCourseInfoResponse res = new ViewCourseInfoResponse();
            Course course = mDataManager.findCourse(req.getParam("Name"));
            res.setStatus((!course.name.isEmpty()? 
                            Response.Status.SUCCESS : 
                            Response.Status.FAILURE));
            output.writeObject(res);
            System.out.println("sending back: " + res.toString());
            
        } else if (type.equals("ViewAllergicUsers")) {

            ViewAllergicUsersResponse res = new ViewAllergicUsersResponse();

            String name = req.getParam("Name");
            String first = req.getParam("First");
            String second = req.getParam("Second");
            String dessert = req.getParam("Dessert");
            String fruit = req.getParam("Fruit");
            
            if (first.isEmpty() || second.isEmpty() || 
                (dessert.isEmpty() && fruit.isEmpty())) {
                res.setStatus(Response.Status.FAILURE);
                res.setError("Missing params: {\"First\", \"Second\", " +
                                "(\"Dessert\" or \"Fruit\")}");
            } else {            
                Menu menu = new Menu();
                menu.setName(name);
                menu.setCourse(first, Course.Type.First);
                menu.setCourse(second, Course.Type.Second);
                menu.setCourse(dessert, Course.Type.Dessert);
                menu.setCourse(fruit, Course.Type.Fruit);
                ArrayList<CanteenUser> users = 
                                    mDataManager.getAllergicUsers(menu);
                res.setUserList(users);
                res.setStatus(Response.Status.SUCCESS);
            }
            output.writeObject(res);
            System.out.println("sending back: " + res.toString());

        } else if (type.equals("ViewNumberOfUsers")) {

            ViewNumberOfUsersResponse res = new ViewNumberOfUsersResponse();

            String uType = req.getParam("Type");
            long num =  mDataManager.getNumberOfUsers(uType);

            if (num >= 0) {
                res.setNumber(num);
                res.setType(uType);
                res.setStatus(Response.Status.SUCCESS);
            } else {
                res.setStatus(Response.Status.FAILURE);
            }
            output.writeObject(res);
            System.out.println("sending back: " + res.toString());
            
        } else if (type.equals("SaveMenu")) {

            Response res = new Response("SaveMenu");
            
            if (req.getParam("Date").isEmpty() || 
                req.getParam("First").isEmpty() || 
                req.getParam("Second").isEmpty() ||
                (req.getParam("Dessert").isEmpty() &&
                req.getParam("Fruit").isEmpty()) ) {
                res.setStatus(Response.Status.FAILURE);
                res.setError("Missing params: {\"First\", \"Second\", " +
                                "(\"Dessert\" or \"Fruit\"), \"Date\"}");
            } else {            
                Menu menu = new Menu();
                menu.fromMap(req.params());
                if (menu.date().isEmpty()) {
                    System.err.println("SaveMenu: invalid date format " + 
                                       "(" + req.getParam("Date") + ")");
                    res.setStatus(Response.Status.FAILURE);                    
                } else
                    if (mDataManager.saveMenu(menu))
                        res.setStatus(Response.Status.SUCCESS);
                    else
                        res.setStatus(Response.Status.FAILURE);
            }
            output.writeObject(res);
            System.out.println("sending back: " + res.toString());

        } else if (type.equals("SaveCourse")) {

            Response res = new Response("SaveCourse");
            
            if (req.getParam("Name").isEmpty() || 
                req.getParam("Type").isEmpty() || 
                req.getParam("Ingredients").isEmpty() ) {
                res.setStatus(Response.Status.FAILURE);
                res.setError("Missing params: {\"Nae\", \"Type\", " +
                                "\"Ingredients\" }");
            } else {            
                Course course = new Course();
                course.fromMap(req.params());
                if (mDataManager.saveCourse(course))
                    res.setStatus(Response.Status.SUCCESS);
                else
                    res.setStatus(Response.Status.FAILURE);
            }
            
            output.writeObject(res);
            System.out.println("sending back: " + res.toString());
            
        } else if (type.equals("SaveUser")) {

            Response res = new Response("SaveUser");
            
            if (req.getParam("Name").isEmpty() || 
                req.getParam("Surname").isEmpty() || 
                req.getParam("Type").isEmpty() || 
                req.getParam("Address").isEmpty() ||
                req.getParam("Allergies").isEmpty()) {
                res.setStatus(Response.Status.FAILURE);
                res.setError("Missing params: {\"Name\", \"Surname\", " + 
                              "\"Type\", \"Address\", \"Allergies\" }");
            } else if (req.getParam("Type").equals("student") && 
                        req.getParam("Parents").isEmpty() ) {
                res.setError("Missing param: {\"Parents\"} for User student");
            } else {            
                CanteenUser user;
                if (req.getParam("Type").equals("student"))
                    user = new Student();
                else if (req.getParam("Type").equals("professor"))
                    user = new Professor();
                else
                    user = new CanteenUser();
                user.fromMap(req.params());
                if (mDataManager.saveUser(user))
                    res.setStatus(Response.Status.SUCCESS);
                else
                    res.setStatus(Response.Status.FAILURE);
            }
            
            output.writeObject(res);
            System.out.println("sending back: " + res.toString());
            
        }
    }

    @Override 
    public void run() {
        
        ObjectOutputStream output = null;
        ObjectInputStream input = null;

        try {
            // set up streams to communicate with the socket 
            output =  new ObjectOutputStream(mClient.getOutputStream());
            input = new ObjectInputStream(mClient.getInputStream());

            System.out.println("... init server input/output streams");

        } catch (Exception ex) {
            System.err.println("failed init input/output streams" + ex);
            try {
                input.close();
                output.flush();
                output.close();
            } catch (Exception ex2) {
                System.err.println("ServerThread::run(release) " + ex2);
            }
            return;
        }
        
        System.out.println("server thread spinning .. ");
        while (true) {
            try {
                // synch threads
                mSemaphore.acquire();

                // unpack the request sent by the client
                Request req = (Request)input.readObject();
                System.out.println("received: " + req.toString());

                // pack the result and send the result back to the client
                prepareResponse(output, req);

                mSemaphore.release();
                
            } catch (Exception ex) {
                System.err.println("ServerThread::run " + ex);
                mSemaphore.release();
                break;
            }
        }
        
        try {
            input.close();
            output.flush();
            output.close();
        } catch (Exception ex2) {
            System.err.println("ServerThread::run(release) " + ex2);
        }
    }
}
