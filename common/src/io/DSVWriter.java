package io;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.util.ArrayList;

public class DSVWriter {
    
    private String mSeparator;
    private boolean mEncloseInBrackets;
    private File mFile;
    private FileWriter mWriter;
    private String mNewLine;
    
    public DSVWriter(String filePath, String separator, 
                    boolean encloseInBrackets ) {
        mSeparator = separator;
        mEncloseInBrackets = encloseInBrackets;
        mNewLine = System.getProperty("line.separator");
//        System.out.println("DSVWriter(): filePath(" + filePath + ")" + 
//                            ", newline(" + mNewLine + 
//                            "), separator(" + mSeparator + ")");
        try {
            mFile = new File(filePath);
            mWriter = new FileWriter(mFile);
        } catch (IOException ex) {
            System.err.println(ex);
            return;
        }
    
    }
    public DSVWriter(String filePath) {
        this(filePath, ",", false);
    }
    
    
    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
    
    public void close() {
        try {
            mWriter.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    
    public synchronized boolean writeLine(ArrayList<String> input) {
        String line = new String();
        for (String word : input) {
            if (mEncloseInBrackets) 
                line += "\"" + word + "\"" + mSeparator;
            else
                line += word + mSeparator;
        }
        if (line.length() > 0)
            line = line.substring(0, line.length() - mSeparator.length());

        try {
            mWriter.write(line + mNewLine);
        } catch (IOException ex) {
            System.err.println(ex);
            return false;
        }
    return true;
    }

}
