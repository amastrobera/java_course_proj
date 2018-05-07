package client;

import comm.*;
import canteen.*;
import university.*;

import java.net.Socket;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


public class Client {
    
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
            mClient = new Socket(mHost, mPort);
            System.out.println("--- starting client ---");
            System.out.println("HostAddress: " + 
                                    mClient.getInetAddress().getHostAddress() +
                                "\nPort: " + mClient.getPort());
            
            mOutput = new ObjectOutputStream(mClient.getOutputStream());
            mInput = new ObjectInputStream(mClient.getInputStream());
            System.out.println("... init client input/output streams");
            
        } catch(Exception ex) {
            System.err.println(ex);
            System.err.println("--- stopping client ---");
            System.exit(2);
        }
    }
    
    private void sendRequest(String type) {
        try {
            Request req = new Request(type);
            System.out.println("sending " + req);
            mOutput.writeObject(req);
        } catch (Exception ex) {
            System.err.println("sendRequest exception: " + ex);
        }
    }

    private void sendRequest(String type, HashMap<String,String> params) {
        try {
            Request req = new Request(type);
            req.setParams(params);
            System.out.println("sending " + req);
            mOutput.writeObject(req);
        } catch (Exception ex) {
            System.err.println("sendRequest exception: " + ex);
        }
    }

    
    public ArrayList<CanteenUser> getUsers() {
        sendRequest("ViewUsers");
        try {
            ViewUsersResponse response = (ViewUsersResponse)mInput.readObject();
            System.out.println("received response: " + response);
            return response.getUserList();
        } catch (Exception ex) {
            System.err.println("getUsers failed: " + ex);
        }
        return new ArrayList<CanteenUser>();
    }

    public ArrayList<CanteenUser> getAllergicUsers(Menu menu) {
        sendRequest("ViewAllergicUsers", menu.toMap());
        try {
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
        sendRequest("ViewCourses");
        try {
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

            ViewCourseInfoResponse response = 
                                (ViewCourseInfoResponse)mInput.readObject();
            System.out.println("received response: " + response);
            return response.getCouse();
        } catch (Exception ex) {
            System.err.println("getCourseInfo failed: " + ex);
        }
        return new Course();
    }

    public long getNumberOfUsers() {
        return getNumberOfUsers("");
    }
    
    public long getNumberOfUsers(String type) {
        try {
            Request req = new Request("ViewNumberOfUsers");
            req.setParam("Type", type);
            System.out.println("sending " + req);
            mOutput.writeObject(req);
            
            ViewNumberOfUsersResponse response = 
                (ViewNumberOfUsersResponse)mInput.readObject();
            System.out.println("received response: " + response);
            if (response.status() == Response.Status.SUCCESS)
                return response.getNumber();
            else {
                System.err.println("saveMenu failed: " + response.error());
                return 0;
            }
        } catch (Exception ex) {
            System.err.println("saveMenu exception: " + ex);
        }
        return 0;
    }

    
    public boolean saveMenu(Menu menu) {
        sendRequest("SaveMenu", menu.toMap());
        try {
            Response response = (Response)mInput.readObject();
            System.out.println("received response: " + response);
            if (response.status() == Response.Status.SUCCESS)
                return true;
            else {
                System.err.println("saveMenu failed: " + response.error());
                return false;
            }
        } catch (Exception ex) {
            System.err.println("saveMenu exception: " + ex);
        }
        return false;        
    }
    
    public boolean saveCourse(Course course) {
        sendRequest("SaveCourse",course.toMap());
        try {
            Response response = (Response)mInput.readObject();
            System.out.println("received response: " + response);
            if (response.status() == Response.Status.SUCCESS)
                return true;
            else {
                System.err.println("saveMenu failed: " + response.error());
                return false;
            }
        } catch (Exception ex) {
            System.err.println("saveMenu exception: " + ex);
        }
        return false;
    }

    public boolean saveUser(CanteenUser user) {
        sendRequest("SaveUser",user.toMap());
        try {
            Response response = (Response)mInput.readObject();
            System.out.println("received response: " + response);
            if (response.status() == Response.Status.SUCCESS)
                return true;
            else {
                System.err.println("saveMenu failed: " + response.error());
                return false;
            }
        } catch (Exception ex) {
            System.err.println("saveMenu exception: " + ex);
        }
        return false;        
    }

    
}
