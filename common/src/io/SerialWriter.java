package io;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;


public class SerialWriter <T extends Object> {
    
    private final String mFilePath;
    private FileOutputStream mFile;
    private ObjectOutputStream mBuffer;
    
    public SerialWriter(String filePath) {
        mFilePath = filePath;
        open();
    }
            
    private void open() {
        try {
            mFile = new FileOutputStream(mFilePath, true); // append mode
            mBuffer = new ObjectOutputStream(mFile);
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    
    public void close() {
        try {
            mBuffer.close();
            mFile.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
    
    private synchronized void reset() {
        close();
        open();
    }
    
    public synchronized boolean writeNextLine(T line) {
        try {
            mBuffer.writeObject(line);
            mBuffer.flush();
        } catch (Exception ex) {
            System.err.println("failed to write to " + mFilePath + ", " + ex);
            return false;
        }
        return true;
    }        
}