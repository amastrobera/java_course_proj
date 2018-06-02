package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;


public class Server implements Runnable {

    enum Type { Files, Database };
    
    private final Type mType;
    private final String mDataPath;
    private final String mDataBase, mUser, mPass;
    private int mPort;
    private ServerSocket mServer;
    private Semaphore mSemaphore;
    private LinkedList<Socket> mClients;
    
    public Server(String dataPath, int port, int numThreads) {
        mDataPath = dataPath;
        mDataBase =  mUser = mPass = "";
        mType = Type.Files;
        init(port, numThreads);
    }

    public Server(String database, String user, String pass, 
                  int port, int numThreads) {
        mDataPath = "";
        mDataBase = database;
        mUser = user;
        mPass = pass;
        mType = Type.Database;
        init(port, numThreads);
    }
    
    private void init(int port, int numThreads){
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
                
                Thread tServer;
                switch (mType) {
                    case Files:
                        tServer = new Thread(
                            new ServerThread(mDataPath, client, mSemaphore));
                        break;
                    case Database:
                        tServer = new Thread(
                            new ServerThread(mDataBase, mUser, mPass, 
                                                client, mSemaphore));
                    break;
                    default:
                        throw new RuntimeException("unknown server type");
                }
                tServer.start();

            } catch (Exception ex) {
                System.err.println("Server::run " + ex);
                break;
            }
            
        }
        
    }
    
    public static void main(String[] args) {

        
        System.out.println("--- Server --- ");

        Server server = null;
        try {
            // launches a server with 4 parallel threads serving requests
            switch (args.length) {
                case 2:
                    server = new Server(args[1], Integer.parseInt(args[0]), 4); 
                    break;
                case 4:
                    server = new Server(args[1], args[2], args[3], 
                                    Integer.parseInt(args[0]), 4);
                    break;
                default:
                    System.err.println("wrong number of arguments");
                    System.out.println("$ java Server port-number /data/path");
                    System.out.println("            or");
                    System.out.println("$ java Server port-number database-name user pass");
                    System.exit(1);
            }
            server.run();
        } catch (Exception ex) {
            server.close();
        }
    }
    
}

