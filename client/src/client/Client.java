package client;

import comm.*;
import canteen.Course;

import java.net.Socket;
import java.io.*;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import university.CanteenUser;

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
    
    public HashMap<String, ArrayList<String>> getMeals() {
        sendRequestNoParam("ViewMeals");
        try {
            // unpack the result
            ViewMealsResponse response = (ViewMealsResponse)mInput.readObject();
            System.out.println("received response: " + response);
            return response.getMealsLists();
        } catch (Exception ex) {
            System.err.println("getMeals failed: " + ex);
        }
        return new HashMap<>();

    }

    public Course findMeal(String name) {
        try {
            Request req = new Request("FindMeal");
            req.setParam("Name", name);
            System.out.println("sending " + req);
            mOutput.writeObject(req);
        } catch (Exception ex) {
            System.err.println("sendRequest exception: " + ex);
        }

        try {
            // unpack the result
            FindMealResponse response = (FindMealResponse)mInput.readObject();
            System.out.println("received response: " + response);
            return response.getCouse();
        } catch (Exception ex) {
            System.err.println("getMeals failed: " + ex);
        }
        return new Course();
    }

    
    public static void main(String args[]) {

        System.out.println("--- Client --- ");
        
        if (args.length < 2) {
            System.err.println("missing arguments");
            System.out.println("$ java Client host-name port-number");
            System.exit(1);
        }
                
        String host = args[0];
        int port = Integer.valueOf(args[1]);
        
        for (int j = 0; j < 2; ++j) {
            Client c1 = new Client(host, port);
            
            ArrayList<CanteenUser> users = c1.getUsers();
            for (int i = 0; i < 2; ++i) {
                System.out.println(users.get(i));
            }
            
            HashMap<String, ArrayList<String>> meals = c1.getMeals();
            ArrayList<String> first = meals.get("First");
            ArrayList<String> dessert = meals.get("Dessert");
            for (int i = 0; i < 2; ++i) 
                System.out.println(first.get(i));
            for (int i = 0; i < 2; ++i) 
                System.out.println(dessert.get(i));
            
            meals = c1.getMeals();
            ArrayList<String> second = meals.get("Second");
            ArrayList<String> fruit = meals.get("Fruit");
            for (int i = 0; i < 2; ++i) 
                System.out.println(second.get(i));
            for (int i = 0; i < 2; ++i) 
                System.out.println(fruit.get(i));
            
        }
    }
    
}
