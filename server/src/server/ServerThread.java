package server; 

import comm.*;
import canteen.*;
import university.CanteenUser;

import java.net.Socket;
import java.io.*;
import java.util.concurrent.Semaphore;
import java.util.ArrayList;


class ServerThread implements Runnable {

    private final String mDataPath;
    private Socket mClient;
    private Semaphore mSemaphore;
    //private MealScanner mMealScanner;
    //private UserScanner mUserScanner;
    private DataManager mDataManager;

    public ServerThread(String dataPath, Socket client, Semaphore sem) {
        mDataPath = dataPath;
        mClient = client;
        mSemaphore = sem;
        mDataManager = new FileDataManager(mDataPath);
        //mMealScanner = new FileMealScanner(mDataPath);
        //mUserScanner = new FileUserScanner(mDataPath);
    }
    
    public ServerThread(Socket client) {
        this("data", client, null);
    }
    
    private void prepareResponse(ObjectOutputStream output, Request req) 
                                                            throws IOException {            
        String type = req.type();
        
        if (type.equals("ViewUsers")) {
            
            ViewUsersResponse res = new ViewUsersResponse();
            //res.setUserList(mUserScanner.getUsers());
            res.setUserList(mDataManager.getUsers());
            res.setStatus(Response.Status.SUCCESS);
            
            output.writeObject(res);
            System.out.println("sending back: " + res.toString());
            
        } else if (type.equals("ViewCourses")) {
            
            ViewCoursesResponse res = new ViewCoursesResponse();
            //res.setUserList(mMealScanner.getMeals());
            res.setUserList(mDataManager.getCourses());
            res.setStatus(Response.Status.SUCCESS);
            
            output.writeObject(res);
            System.out.println("sending back: " + res.toString());
            
        } else if (type.equals("ViewCourseInfo")) {

            ViewCourseInfoResponse res = new ViewCourseInfoResponse();
            //Course course = mMealScanner.findCourse(req.getParam("Name"));
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
            
            if (first.isEmpty() || second.isEmpty() || dessert.isEmpty() ||
                fruit.isEmpty()) {
                res.setStatus(Response.Status.FAILURE);
                res.setError("Missing params: {\"First\", \"Second\", " +
                                "\"Dessert\", \"Fruit\"}");
            } else {            
                Menu menu = new Menu(name);
                menu.setCourse(first, Course.Type.First);
                menu.setCourse(second, Course.Type.Second);
                menu.setCourse(dessert, Course.Type.Dessert);
                menu.setCourse(fruit, Course.Type.Fruit);
                ArrayList<CanteenUser> users = 
                                    mDataManager.getAllergicUsers(menu);
                                    //mUserScanner.getAllergicUsers(menu);
                res.setUserList(users);
                res.setStatus(Response.Status.SUCCESS);
            }
            output.writeObject(res);
            System.out.println("sending back: " + res.toString());
            
        } else if (type.equals("SaveMenu")) {

            Response res = new Response("SaveMenu");
            // todo 
            res.setStatus(Response.Status.SUCCESS);
            
            output.writeObject(res);
            System.out.println("sending back: " + res.toString());

        } else if (type.equals("SaveCourse")) {

            Response res = new Response("SaveCourse");
            // todo 
            res.setStatus(Response.Status.SUCCESS);
            
            output.writeObject(res);
            System.out.println("sending back: " + res.toString());
            
        } else if (type.equals("SaveUser")) {

            Response res = new Response("SaveUser");
            // todo 
            res.setStatus(Response.Status.SUCCESS);
            
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
                
                Thread.sleep(2000);
                
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
