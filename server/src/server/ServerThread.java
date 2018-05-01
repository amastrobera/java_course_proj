package server; 

import comm.*;
import canteen.Course;

import java.net.Socket;
import java.io.*;
import java.util.concurrent.Semaphore;


class ServerThread implements Runnable {

    private final String mDataPath;
    private Socket mClient;
    private Semaphore mSemaphore;
    private MealScanner mMealScanner;
    private UserScanner mUserScanner;

    public ServerThread(String dataPath, Socket client, Semaphore sem) {
        mDataPath = dataPath;
        mClient = client;
        mSemaphore = sem;
        mMealScanner = new FileMealScanner(mDataPath);
        mUserScanner = new FileUserScanner(mDataPath);
    }
    
    public ServerThread(Socket client) {
        this("data", client, null);
    }
    
    private void prepareResponse(ObjectOutputStream output, Request req) 
                                                            throws IOException {            
        String type = req.type();
        
        if (type.equals("ViewUsers")) {
            
            ViewUsersResponse res = new ViewUsersResponse();
            res.setUserList(mUserScanner.getUsers());
            res.setStatus(Response.Status.SUCCESS);
            
            output.writeObject(res);
            System.out.println("sending back: " + res.toString());
            
        } else if (type.equals("ViewMeals")) {
            
            ViewMealsResponse res = new ViewMealsResponse();
            res.setUserList(mMealScanner.getMeals());
            res.setStatus(Response.Status.SUCCESS);
            
            output.writeObject(res);
            System.out.println("sending back: " + res.toString());
            
        } else if (type.equals("FindMeal")) {

            FindMealResponse res = new FindMealResponse();
            Course course = mMealScanner.findCourse(req.getParam("Name"));
            res.setStatus((!course.name.isEmpty()? 
                            Response.Status.SUCCESS : 
                            Response.Status.FAILURE));
            output.writeObject(res);
            System.out.println("sending back: " + res.toString());
            
        } else if (type.equals("ViewAllergicUsers")) {

            ViewAllergicUsersResponse res = new ViewAllergicUsersResponse();
            // todo 
            res.setStatus(Response.Status.SUCCESS);
            
            output.writeObject(res);
            System.out.println("sending back: " + res.toString());
            
        } else if (type.equals("SetMenu")) {

            SetResponse res = new SetResponse();
            // todo 
            res.setStatus(Response.Status.SUCCESS);
            
            output.writeObject(res);
            System.out.println("sending back: " + res.toString());
            
        } else if (type.equals("SetUser")) {

            SetResponse res = new SetResponse();
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
        // InputStream stream = null;

        try {
            // set up streams to communicate with the socket 
            output =  new ObjectOutputStream(mClient.getOutputStream());
            input = new ObjectInputStream(mClient.getInputStream());

            System.out.println("... init server input/output streams");
            // stream = new DataInputStream(new FileInputStream("data/input.cmd"));
        } catch (Exception ex) {
            System.err.println("failed init input/output streams" + ex);
            try {
                //stream.close();
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
            //stream.close();
            input.close();
            output.flush();
            output.close();
        } catch (Exception ex2) {
            System.err.println("ServerThread::run(release) " + ex2);
        }
    }
}
