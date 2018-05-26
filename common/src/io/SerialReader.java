package io;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class SerialReader <T extends Object> {
    
    private final String mFilePath;
    private FileInputStream mFile;
    private ObjectInputStream mBuffer;
           
    public SerialReader(String filePath) {
        mFilePath = filePath;
        open();
    }
     
    
    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
    
    public boolean open() {
        try {
            mFile  = new FileInputStream(mFilePath);
            mBuffer = new ObjectInputStream(mFile);
        } catch(Exception ex) {
            System.err.println("SerialReader (file open): " + ex);
            return false;
        }
        return true;
    }
    
    public void close() {
        try {
            mBuffer.close();
            mFile.close();
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    public synchronized void reset() {
        close();
        open();
    }
    
    /**
     * 
     * @return "null" if end-of-file, "empty object" for empty line, 
     *          "object T" otherwise
     */
    public T getNextLine() {
        try {            
            T obj = (T)mBuffer.readObject();
            return obj;
        } catch (Exception ex) {
            //System.err.println("end of reader file " + mFilePath);
        }
        return null;
    }
    
    /**
     * 
     * @param objToFind serialised object to find in the file; must have 
     *                  "equals()" function defined
     * @return the line (number of object serialised) at which the user stands
     *          or "-1" in case the user hasn't been found
     */
    public long find(T objToFind) {
        long line = -1;
        try {
            reset();
            long cur = 0;
            while (true) {
                try { 
                    T obj = (T)mBuffer.readObject();
                    if (obj.equals(objToFind)) {
                        line = cur;
                        break;
                    }
                    ++cur;
                } catch (Exception ex) {
                    //System.err.println("end of reader file " + mFilePath + 
                    //                    ", read : " + cur + " objects");
                    break;
                }
            }
        } catch (Exception ex) {
            System.err.println("failed to read serialized file " + mFilePath +
                                ", "+ ex);
        }
        return line;
    }

    /**
     * 
     * @param objToFind (minimal version of the) serialised  object to find in 
     *                  the file; must have "equals()" function defined
     * @return "null" if the object doesn't match any in the file, 
     *         "object T" (complete) otherwise
     */
    public T findObj(T objToFind) {
        T objFound = null;
        try {
            reset();
            while (true) {
                try { 
                    T obj = (T)mBuffer.readObject();
                    if (obj.equals(objToFind)) {
                        objFound = obj;
                        break;
                    }
                } catch (Exception ex) {
                    //System.err.println("end of reader file " + mFilePath );
                    break;
                }
            }
        } catch (Exception ex) {
            System.err.println("failed to read serialized file " + mFilePath +
                                ", "+ ex);
        }
        return objFound;
    }
    
    /**
     * 
     * @param lineNo object number in the file 
     * @return "null" if not found, the "object T" otherwise
     */
    public T findAt(long lineNo) {
        T objFound = null;
        try {
            reset();
            int num = 0;
            while (num <= lineNo) {
                try { 
                    T obj = (T)mBuffer.readObject();
                    if (num == lineNo) {
                        objFound = obj;
                        break;
                    }
                    ++num;
                } catch (Exception ex) {
                    //System.err.println("end of reader file " + mFilePath + 
                    //                    ", read : " + num + " objects");
                    break;
                }
            }
        } catch (Exception ex) {
            System.err.println("failed to read serialized file " + mFilePath +
                                ", "+ ex);
        }
        return objFound;
    }
    
}
