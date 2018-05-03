package io;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
//import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class DSVWriter {
    
    private String mFilePath;
    private String mSeparator;
    private boolean mHasHeaders;
    private boolean mEncloseInBrackets;
    private boolean mWriteHeaders;
    private ArrayList<String> mHeaders;
    private File mFile;
    private FileWriter mWriter;
    //private RandomAccessFile mWriter;
    private String mNewLine;
    
    public DSVWriter(String filePath, String separator, 
                    boolean hasHeaders, boolean encloseInBrackets,
                    boolean writeHeadersFirst) {
        mFilePath = filePath;
        mSeparator = separator;
        mHasHeaders = hasHeaders;
        mEncloseInBrackets = encloseInBrackets;
        mWriteHeaders = writeHeadersFirst;
        mHeaders = new ArrayList<>();
        mNewLine = System.getProperty("line.separator");
        open();
    }
    
    public DSVWriter(String filePath) {
        this(filePath, ",", false, false, false);
    }
    
    public void setHeaders(ArrayList<String> headers) {
        mHeaders = headers;
        if (mWriteHeaders) 
            writeLine(mHeaders);
    }
    
    public ArrayList<String> headers(){
        return mHeaders;
    }
        
    private void open() {
        try {
            mFile = new File(mFilePath);
            if (!mFile.exists()) mFile.createNewFile();
            //long len = mFile.length();
            //mWriter = new RandomAccessFile(mFile, "rw");
            //mWriter.seek(len);
            mWriter = new FileWriter(mFile, true); //append-mode for 2+ threads
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    
    public void close() {
        try {
            mWriter.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
    
    private void reset() {
        close();
        open();
    }

    public boolean writeMap(HashMap<String, String> input) {
        ArrayList<String> line = new ArrayList<>();
        if (mHasHeaders) {
            for (int i = 0; i < mHeaders.size(); ++i) {
                String col = mHeaders.get(i);
                if (input.containsKey(col))
                    line.add(input.get(col));
                else
                    line.add("");
            }
        } else {
            Iterator it = input.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String,String> pair = 
                            (Map.Entry<String,String>)it.next();
                line.add(pair.getValue());
            }
        }
        return writeLine(line);
    }
    
    public boolean writeLine(ArrayList<String> input) {
        String line = new String();
        for (String word : input) {
            if (mEncloseInBrackets) 
                line += "\"" + word + "\"" + mSeparator;
            else
                line += word + mSeparator;
        }
        if (line.length() > 0)
            line = line.substring(0, line.length() - mSeparator.length());

        return writeLine(line);
    }

    public synchronized boolean writeLine(String line) {
        try {
            //mWriter.write((line + mNewLine).getBytes());
            mWriter.write(line + mNewLine);
            mWriter.flush(); // very important ... 
        } catch (IOException ex) {
            System.err.println(ex);
            return false;
        }
        return true;
    }
    
}