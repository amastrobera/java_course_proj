package client;

import comm.Message;

//import java.net.InetAddress;
import java.net.Socket;
import java.io.*;
import java.net.UnknownHostException;

public class Client implements Runnable {

    // all requests and response behaviours 
    
    private String host;
    private int port;
    private Socket client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    
    public Client(String hostName, int port) {
        host = null;
        this.port = port;
        client = null;
        input = null;
        output = null;
        try {
            //host = InetAddress.getLocalHost();
            client = new Socket(host, port);
            System.out.println("--- starting client ---");
            System.out.println("HostAddress: " + 
                                    client.getInetAddress().getHostAddress() +
                                "\nPort: " + client.getPort());
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
    
    //public static void main(String[] args) {
    @Override
    public void run() {
        
        System.out.println("... client run");
        
        try {
            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());
            System.out.println("... init client input/output streams");

        } catch (IOException ex ) {
            System.err.println(ex);
            System.err.println("--- stopping client ---");
            try {
                input.close();
                output.flush();
                output.close();
                client.close();
            } catch (IOException ex2) {
                System.err.println("exception while stoppping client : " +ex);
            }
            return ;
        }

        System.out.println("... client spinning");
        
        while (true) {
            try {
                // prepare a request 
                Message msg = new Message(Message.Type.Request, 
                                            Message.Content.ViewUsers);
                System.out.println("sending request: " + msg);
                
                // unpack the result from server
                output.writeObject(msg);
                
                // interpret the result
                Message feed = (Message)input.readObject();
                System.out.println("received response: " + feed);
                
                Thread.sleep(1000);
                
            } catch (Exception ex) {
                try {
                    input.close();
                    output.flush(); 
                    output.close();
                    client.close();
                } catch (IOException ex2) {
                    System.err.println("exception while closing : " + ex2);
                }
                System.err.println(ex);
                break;
            }
        }
        
        System.out.println("--- stopping client ---");
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
            c1.run();
        }
    }
    
}
