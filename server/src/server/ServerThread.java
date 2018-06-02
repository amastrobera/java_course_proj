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
    private final String mDataBase, mUser, mPass;
    private Socket mClient;
    private Semaphore mSemaphore;
    private DataManager mDataManager;

    public ServerThread(String dataPath, Socket client, Semaphore sem) {
        mDataPath = dataPath;
        mDataBase = mUser = mPass = "";
        mClient = client;
        mSemaphore = sem;
        mDataManager = new FileDataManager(mDataPath);
    }
    
    public ServerThread(String database, String user, String pass, 
                        Socket client, Semaphore sem) {
        mDataPath = "";
        mDataBase = database;
        mUser = user;
        mPass = pass;
        mClient = client;
        mSemaphore = sem;
        mDataManager = new SQLDataManager(mDataBase, mUser, mPass);

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
            res.setStatus((!res.isEmpty() ? Response.Status.SUCCESS : 
                                Response.Status.FAILURE));
            output.writeObject(res);
            System.out.println("sending back: " + res.toString());
            
        } else if (type.equals("ViewCourses")) {
            
            ViewCoursesResponse res = new ViewCoursesResponse();
            res.setUserList(mDataManager.getCourses());
            res.setStatus((!res.isEmpty() ? Response.Status.SUCCESS : 
                                Response.Status.FAILURE));            
            output.writeObject(res);
            System.out.println("sending back: " + res.toString());

        } else if (type.equals("ViewMenus")) {
            
            ViewMenusResponse res = new ViewMenusResponse();
            res.setMenus(mDataManager.getMenus());
            res.setStatus((!res.isEmpty() ? Response.Status.SUCCESS : 
                          Response.Status.FAILURE));
            output.writeObject(res);
            System.out.println("sending back: " + res.toString());

            
        } else if (type.equals("ViewCourseInfo")) {

            ViewCourseInfoResponse res = new ViewCourseInfoResponse();
            Course course = mDataManager.findCourse(req.getParam("Name"));
            res.setStatus((!res.isEmpty() ? Response.Status.SUCCESS : 
                          Response.Status.FAILURE));
            res.setCourse(course);
            output.writeObject(res);
            System.out.println("sending back: " + res.toString());
            
        } else if (type.equals("ViewAllergicUsers")) {

            ViewAllergicUsersResponse res = new ViewAllergicUsersResponse();
            
            Menu menu = ((ViewAllergicUsersRequest)req).getMenu();
            
            if(menu.getCourse(Course.Type.First).isEmpty() || 
                menu.getCourse(Course.Type.Second).isEmpty() ||
                (menu.getCourse(Course.Type.Dessert).isEmpty() &&
                menu.getCourse(Course.Type.Fruit).isEmpty()) ) {
                res.setStatus(Response.Status.FAILURE);
                res.setError("Missing params: {\"First\", \"Second\", " +
                                "(\"Dessert\" or \"Fruit\")}");
            } else {            
                ArrayList<CanteenUser> users = 
                                    mDataManager.getAllergicUsers(menu);
                res.setUserList(users);
                res.setStatus((!res.isEmpty() ? Response.Status.SUCCESS : 
                          Response.Status.FAILURE));
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
            
            Menu menu = ((SaveMenuRequest)req).getMenu();
            
            if (menu.date().isEmpty() || 
                menu.getCourse(Course.Type.First).isEmpty() || 
                menu.getCourse(Course.Type.Second).isEmpty() ||
                (menu.getCourse(Course.Type.Dessert).isEmpty() &&
                menu.getCourse(Course.Type.Fruit).isEmpty()) ) {
                res.setStatus(Response.Status.FAILURE);
                res.setError("Missing params: {\"First\", \"Second\", " +
                                "(\"Dessert\" or \"Fruit\"), \"Date\"}");
            } else {
                if (mDataManager.saveMenu(menu))
                    res.setStatus(Response.Status.SUCCESS);
                else
                    res.setStatus(Response.Status.FAILURE);
            }
            output.writeObject(res);
            System.out.println("sending back: " + res.toString());

        } else if (type.equals("SaveCourse")) {

            Response res = new Response("SaveCourse");
            Course course = ((SaveCourseRequest)req).getCourse();
            
            if (course.name.isEmpty() || 
                course.type == Course.Type.Unknown || 
                course.ingredients.isEmpty() ) {
                res.setStatus(Response.Status.FAILURE);
                res.setError("Missing params: {\"Name\", \"Type\", " +
                                "\"Ingredients\" }");
            } else {            
                if (mDataManager.saveCourse(course))
                    res.setStatus(Response.Status.SUCCESS);
                else
                    res.setStatus(Response.Status.FAILURE);
            }
            
            output.writeObject(res);
            System.out.println("sending back: " + res.toString());
            
        } else if (type.equals("SaveUser")) {

            Response res = new Response("SaveUser");
            
            CanteenUser user = ((SaveUserRequest)req).getUser();
            
            if (user.name().isEmpty() || 
                user.surname().isEmpty() || 
                user.type().isEmpty() ) {
                res.setStatus(Response.Status.FAILURE);
                res.setError("Missing params: {\"Name\", \"Surname\", " + 
                              "\"Type\" }");
            } else {            
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
