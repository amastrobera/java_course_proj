package io;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DSVReader {
    
    private String mFilePath;
    private String mSeparator;
    private boolean mHasHeaders;
    private boolean mFieldsEnclosedInBrackets;
    private ArrayList<String> mFields;
    private FileReader mFile;
    private BufferedReader mBuffer;
       
    public DSVReader(String filePath, String separator, 
                        boolean hasHeaders, boolean fieldsEnclosedInBrackets) {
        mFilePath = filePath;
        mSeparator = (fieldsEnclosedInBrackets ? 
                        separator + "(?=([^\"]*\"[^\"]*\")*[^\"]*$)"
                        : separator);
        mHasHeaders = hasHeaders;
        mFieldsEnclosedInBrackets = fieldsEnclosedInBrackets;
        mFields = new ArrayList<>();

        open();
        
        if (mHasHeaders) getHeaderFields();
    }
     
        
    public DSVReader(String filePath, String separator) {
        this(filePath, separator, false, false);
    }
    
    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
    
        public boolean open() {
        try {
            mFile = new FileReader(mFilePath);
            mBuffer = new BufferedReader(mFile);
        } catch(Exception ex) {
            System.err.println("DSVReader (file open): " + ex);
            return false;
        }
        return true;
    }
    
    public void close() {
        try {
            mBuffer.close();
            mFile.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public void reset() {
        reset(true);
    }
    
    public synchronized void reset(boolean skipHeaders) {
        close();
        open();
        if (skipHeaders && mHasHeaders)
            getHeaderFields();
    }
    
    private void getHeaderFields() {
        try {
            if (mBuffer.ready()) {
                String line = mBuffer.readLine();
                String[] fields = line.split(mSeparator);
                mFields = new ArrayList<>();
                for (int i = 0; i < fields.length; ++i)
                    if (mFieldsEnclosedInBrackets)
                        mFields.add(fields[i]
                                    .substring(1,fields[i].length()-1));
                    else
                        mFields.add(fields[i]);
            }
        } catch(Exception ex) {
            System.err.println("DSVReader (read headers): " + ex);
        }
    }
    
    public ArrayList<String> fields() {
        return mFields;
    }


    public boolean getNextLine(HashMap<String,String> ret) {
        try {
            if (mBuffer.ready()) {
                String line = mBuffer.readLine();
                if (!line.isEmpty()) {
                    String[] fields = line.split(mSeparator);
                    for (int i = 0; i < fields.length; ++i)
                        if (i < mFields.size()) {
                            String field;
                            if (mFieldsEnclosedInBrackets && 
                                !fields[i].isEmpty())
                                field = fields[i].substring(1,
                                                        fields[i].length()-1);
                                 else
                                   field = fields[i];
                            ret.put(mFields.get(i), field);
                        } else 
                            break;
                }
                return true;
            }
            return false;
        } catch (Exception ex) {
            System.err.println("DSVReader (getNextLine): " + ex );
        }
        return false;
    }
    
    public boolean getNextLine(ArrayList<String> ret) {
        try {
            if (mBuffer.ready()) {
                String line = mBuffer.readLine();
                if (!line.isEmpty()) {
                    String[] fields = line.split(mSeparator);
                    for (int i = 0; i < fields.length; ++i)
                        ret.add(fields[i].trim());
                }
                return true;
            }
            return false;
        } catch (Exception ex) {
            System.err.println("DSVReader (getNextLine): " + ex );
        }
        return false;
    }

    public boolean getNextLine(StringBuffer ret) {
        try {
            if (mBuffer.ready()) {
                String line = mBuffer.readLine();
                ret.append(line);
                return true;
            }
            return false;
        } catch (Exception ex) {
            System.err.println("DSVReader (getNextLine): " + ex );
        }
        return false;
    }
    

}
