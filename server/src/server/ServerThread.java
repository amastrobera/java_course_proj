package server; 

import comm.Message;

import java.net.Socket;
import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;


class ServerThread implements Runnable {

    private Socket mClient;
    private Semaphore mSemaphore;

    public ServerThread(Socket client, Semaphore sem) {
        mClient = client;
        mSemaphore = sem;
    }
    
    public ServerThread(Socket client) {
        this(client, null);
    }
    
    private Message prepareResponse(Message req) {
        
        Message res = new Message(Message.Type.Response, 
                            Message.Content.Unknown);
        
        if (req.type() != Message.Type.Request) {
            res.setParam("Return", "FAIL");
            res.setParam("Error", "Received response instead of request");
            return res;
        }
        
        switch (req.content()) {
            case ViewMenus:
                res.setConntent(Message.Content.ViewMenus);
                res.setParam("Return", "OK");
                break;
            case ViewUsers:
                res.setConntent(Message.Content.ViewUsers);
                res.setList("UserList", new ArrayList<>(
                    Arrays.asList("John", "Paul", "George", "Ringo")));
                res.setParam("Return", "OK");
                break;
            case ViewAllergicUsers:
                res.setConntent(Message.Content.ViewAllergicUsers);
                res.setParam("Return", "OK");
                break;
            case SetMenu: 
                res.setConntent(Message.Content.SetMenu);
                res.setParam("Return", "OK");
                break;
            case SetUser:
                res.setConntent(Message.Content.SetUser);
                res.setParam("Return", "OK");
                break;
            default:
                res.setParam("Return", "FAIL");
                res.setParam("Error", "Unknown request content");
                break;
        }
        return res;
    }

    @Override 
    public void run() {
        
        ObjectOutputStream output = null;
        ObjectInputStream input = null;
        // InputStream stream = null;
        
        try {
            // synch threads
            mSemaphore.acquire();
            
            // set up streams to communicate with the socket 
            output =  new ObjectOutputStream(mClient.getOutputStream());
            input = new ObjectInputStream(mClient.getInputStream());
            
            System.out.println("... init server input/output streams");
            // stream = new DataInputStream(new FileInputStream("data/input.cmd"));

            // unpack the request sent by the client
            Message req = (Message)input.readObject();
            System.out.println("received: " + req.toString());

            // pack the result
            Message res = prepareResponse(req);
            System.out.println("sending back: " + res.toString());

            // send the result back to the client
            output.writeObject(res);
            
            Thread.sleep(2000);
            
        } catch (Exception ex) {
            System.err.println("ServerThread::run " + ex);
        }
        
        try {
            //stream.close();
            input.close();
            output.flush();
            output.close();
            mSemaphore.release();
        } catch (Exception ex) {
            System.err.println("ServerThread::run(release) " + ex);
        }
        
    }
}
