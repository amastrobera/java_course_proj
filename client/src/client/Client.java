package client;

import comm.*;
import canteen.*;
import university.*;

import java.net.Socket;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


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
    
    private void sendSimpleRequest(String type) {
        try {
            Request req = new Request(type);
            System.out.println("sending " + req);
            mOutput.writeObject(req);
        } catch (Exception ex) {
            System.err.println("sendSimpleRequest exception: " + ex);
        }
    }

    private void sendSimpleRequest(String type, HashMap<String,String> params) {
        try {
            Request req = new Request(type);
            req.setParams(params);
            System.out.println("sending " + req);
            mOutput.writeObject(req);
        } catch (Exception ex) {
            System.err.println("sendSimpleRequest exception: " + ex);
        }
    }

    
    public ArrayList<CanteenUser> getUsers() {
        sendSimpleRequest("ViewUsers");
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
        try {
            ViewAllergicUsersRequest req = new ViewAllergicUsersRequest(menu);
            mOutput.writeObject(req);
            ViewAllergicUsersResponse response = 
                                (ViewAllergicUsersResponse)mInput.readObject();
            System.out.println("received response: " + response);
            return response.getUserList();
        } catch (Exception ex) {
            System.err.println("getAllergicUsers failed: " + ex);
        }
        return new ArrayList<>();
    }
    
    public HashMap<String, ArrayList<String>> getCourses() {
        sendSimpleRequest("ViewCourses");
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

    public HashSet<Menu> getMenus() {
        try {
            sendSimpleRequest("ViewMenus");
            ViewMenusResponse response = 
                                (ViewMenusResponse)mInput.readObject();
            System.out.println("received response: " + response);
            return response.getMenus();
        } catch (Exception ex) {
            System.err.println("getMenus failed: " + ex);
        }
        return new HashSet<>();
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
                System.err.println("getNumberOfUsers failed: " + response.error());
                return 0;
            }
        } catch (Exception ex) {
            System.err.println("getNumberOfUsers exception: " + ex);
        }
        return 0;
    }

    
    public boolean saveMenu(Menu menu) {
        try {
            SaveMenuRequest req = new SaveMenuRequest(menu);
            mOutput.writeObject(req);
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
        try {
            SaveCourseRequest req = new SaveCourseRequest(course);
            mOutput.writeObject(req);
            Response response = (Response)mInput.readObject();
            System.out.println("received response: " + response);
            if (response.status() == Response.Status.SUCCESS)
                return true;
            else {
                System.err.println("saveCourse failed: " + response.error());
                return false;
            }
        } catch (Exception ex) {
            System.err.println("saveCourse exception: " + ex);
        }
        return false;
    }

    public boolean saveUser(CanteenUser user) {
        try {
            SaveUserRequest req = new SaveUserRequest(user);
            mOutput.writeObject(req);
            Response response = (Response)mInput.readObject();
            System.out.println("received response: " + response);
            if (response.status() == Response.Status.SUCCESS)
                return true;
            else {
                System.err.println("saveUser failed: " + response.error());
                return false;
            }
        } catch (Exception ex) {
            System.err.println("saveUser exception: " + ex);
        }
        return false;        
    }

    
}
