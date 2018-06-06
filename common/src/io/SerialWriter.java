package io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class SerialWriter <T extends Object> {
    
    private final String mFilePath;
    private FileOutputStream mFile;
    private ObjectOutputStream mBuffer;
    
    public SerialWriter(String filePath) {
        mFilePath = filePath;
        // if file does not exist, create one. then open it in append mode
        if (!(new File(mFilePath).exists())) {
            try { 
                mFile = new FileOutputStream(mFilePath);
                mBuffer = new ObjectOutputStream(mFile);
                mBuffer.close();
            } catch (IOException ex) {
            }
        }
        open();
    }
            
    private void open() {
        try {
            mFile = new FileOutputStream(mFilePath, true);             
            mBuffer = new ObjectOutputStream(mFile) {
            protected void writeStreamHeader() throws IOException {
                reset();
            }
        };
            
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
    
    public synchronized void reset() {
        close();
        open();
    }
    
    public synchronized boolean writeNextLine(T line) {
        try {
            mBuffer.writeObject(line);
            mBuffer.flush();
            mFile.flush();
        } catch (Exception ex) {
            System.err.println("failed to write to " + mFilePath + ", " + ex);
            return false;
        }
        return true;
    }        
}