package client;

import comm.Message;

import java.net.Socket;
import java.io.*;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.ArrayList;

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
    
    public ArrayList<String> getUsers() {
        
        System.out.println("... client run");
    
        try {
            // prepare a request 
            Message msg = new Message(Message.Type.Request, 
                                        Message.Content.ViewUsers);
            System.out.println("sending request: " + msg);
            mOutput.writeObject(msg);

            // unpack the result
            Message feed = (Message)mInput.readObject();
            System.out.println("received response: " + feed);

            if (feed.getParam("Return").equals("OK"))            
                return new ArrayList<String>(
                    Arrays.asList(feed.getParam("UserList").split(", ")));
            else
                System.err.println("response: " + feed.getParam("Return"));
            
        } catch (Exception ex) {
            try {
                mInput.close();
                mOutput.flush(); 
                mOutput.close();
                mClient.close();
            } catch (IOException ex2) {
                System.err.println("exception while closing : " + ex2);
            }
            System.err.println(ex);
        }
        
        System.out.println("--- stopping client ---");
        
        return new ArrayList<String>();
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
        
        for (int i = 0; i < 10; ++i) {
            Client c1 = new Client(host, port);
            ArrayList<String> users = c1.getUsers();
            System.out.println(users);
        }
    }
    
}
