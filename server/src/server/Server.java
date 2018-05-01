package server;

import com.sun.media.jfxmedia.MetadataParser;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;


public class Server implements Runnable {
    
    private final String mDataPath;
    private int mPort;
    private ServerSocket mServer;
    private Semaphore mSemaphore;
    private LinkedList<Socket> mClients;
    
    public Server(String dataPath, int port, int numThreads) {
        mDataPath = dataPath;
        mPort = port;
        mServer = null;
        mSemaphore = new Semaphore(numThreads);
        mClients = new LinkedList<>();
        
        try {
            mServer = new ServerSocket(mPort);
            System.out.println("--- starting server ---");
            System.out.println("HostName: " + 
                                mServer.getInetAddress().getHostName() +
                                "\nLocalPort: " + mServer.getLocalPort());
        } catch (IOException ex) {
            System.err.println(ex + "\n--- server not started ---");
            System.exit(1);
        }
    }
    
    public void close() {
        try {
            for (Socket client : mClients) {
                System.out.println("--- stopping client " + 
                        client.getInetAddress().getHostAddress() + " " +
                        client.getPort() + " ---");
                client.close();
            }
            mServer.close();
        } catch (IOException ex) {
            System.err.println("stopping: " + ex);
        }
        System.out.println("--- stopping server ---");
    }
    
    @Override
    public void finalize() throws Throwable {
        close();
        super.finalize();
    }
    
    @Override
    public void run(){
        
        System.out.println("... server spinning");
        
        while (true) {
            
            try {
                Socket client = mServer.accept();
                mClients.add(client);
                System.out.println("... accepted client connection");
                
                Thread tServer = new Thread(
                            new ServerThread(mDataPath, client, mSemaphore));
                tServer.start();

            } catch (Exception ex) {
                System.err.println("Server::run " + ex);
                break;
            }
            
        }
        
    }
    
    public static void main(String[] args) {

        
        System.out.println("--- Server --- ");
        
        if (args.length < 2) {
            System.err.println("missing arguments");
            System.out.println("$ java Server /data/path port-number");
            System.exit(1);
        }

        Server server = null;
        try {
            // laucnhes a server with 4 parallel threads serving requests
            server = new Server(args[0], Integer.parseInt(args[1]), 4);
            server.run();
        } catch (Exception ex) {
            server.close();
        }
            
    }
    
}

