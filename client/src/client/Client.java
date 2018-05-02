package client;

import comm.*;
import canteen.*;
import university.CanteenUser;

import java.net.Socket;
import java.io.*;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class Client {

    // all requests and response behaviours 
    
    private String mHost;
    private int mPort;
    private Socket mClient;
    private ObjectOutputStream mOutput;
    private ObjectInputStream mInput;
    
    public Client(String hostName, int port) {
        mHost = hostName;
        this.mPort = port;
        mClient = null;
        mOutput = null;
        mInput = null;
        
        try {
            //host = InetAddress.getLocalHost();
            mClient = new Socket(mHost, mPort);
            System.out.println("--- starting client ---");
            System.out.println("HostAddress: " + 
                                    mClient.getInetAddress().getHostAddress() +
                                "\nPort: " + mClient.getPort());
            
            mOutput = new ObjectOutputStream(mClient.getOutputStream());
            mInput = new ObjectInputStream(mClient.getInputStream());
            System.out.println("... init client input/output streams");
            
        } catch (UnknownHostException ex ){
            System.err.println(ex);
            System.err.println("--- stopping client ---");
            System.exit(2);
        } catch(IOException ex) {
            System.err.println(ex);
            System.err.println("--- stopping client ---");
            System.exit(2);
        }
    }
    
    private void sendRequestNoParam(String type) {
        try {
            Request req = new Request(type);
            System.out.println("sending " + req);
            mOutput.writeObject(req);
        } catch (Exception ex) {
            System.err.println("sendRequest exception: " + ex);
        }
    }
    
    public ArrayList<CanteenUser> getUsers() {
        sendRequestNoParam("ViewUsers");
        try {
            // unpack the result
            ViewUsersResponse response = (ViewUsersResponse)mInput.readObject();
            System.out.println("received response: " + response);
            return response.getUserList();
        } catch (Exception ex) {
            System.err.println("getUsers failed: " + ex);
        }
        return new ArrayList<CanteenUser>();
    }

    public ArrayList<CanteenUser> getAllergicUsers(Menu menu) {
        try {
            Request req = new Request("ViewAllergicUsers");
            req.setParam("Name", menu.name());
            req.setParam("First", menu.getCourse(Course.Type.First));
            req.setParam("Second", menu.getCourse(Course.Type.Second));
            req.setParam("Dessert", menu.getCourse(Course.Type.Dessert));
            req.setParam("Fruit", menu.getCourse(Course.Type.Fruit));
            System.out.println("sending " + req);
            mOutput.writeObject(req);

            // unpack the result
            ViewAllergicUsersResponse response = 
                                (ViewAllergicUsersResponse)mInput.readObject();
            System.out.println("received response: " + response);
            return response.getUserList();
        } catch (Exception ex) {
            System.err.println("getUsers failed: " + ex);
        }
        return new ArrayList<CanteenUser>();
    }
    
    public HashMap<String, ArrayList<String>> getCourses() {
        sendRequestNoParam("ViewCourses");
        try {
            // unpack the result
            ViewCoursesResponse response = 
                                (ViewCoursesResponse)mInput.readObject();
            System.out.println("received response: " + response);
            return response.getCouresLists();
        } catch (Exception ex) {
            System.err.println("getCourses failed: " + ex);
        }
        return new HashMap<>();

    }

    public Course getCourseInfo(String name) {
        try {
            Request req = new Request("ViewCourseInfo");
            req.setParam("Name", name);
            System.out.println("sending " + req);
            mOutput.writeObject(req);

            // unpack the result
            ViewCourseInfoResponse response = 
                                (ViewCourseInfoResponse)mInput.readObject();
            System.out.println("received response: " + response);
            return response.getCouse();
        } catch (Exception ex) {
            System.err.println("getCourseInfo failed: " + ex);
        }
        return new Course();
    }    
}
