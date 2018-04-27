package comm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;

// testing
import java.io.*;

public class Message implements Serializable {

    public static enum Type {Request, Response};
    
    public static enum Content {ViewMenus, ViewUsers, ViewAllergicUsers,
                                SetMenu, SetUser, Unknown};

    private final Type mType;
    private Content mContent;
    protected HashMap<String,String> mParams;

    public Message(Type type, Content content) {
        mType = type;
        mContent = content;
        mParams = new HashMap<>();
        
        if (mType == Type.Request)
            fillRequest();
        else
            fillResponse();
        
    }
    
    private void fillRequest(){
        switch (mContent) {
            case ViewMenus:
            case ViewUsers:
                break;
            case ViewAllergicUsers:
                mParams.put("Menu", "");
                break;
            case SetMenu:
                mParams.put("Menu", "");
                break;
            case SetUser:
                mParams.put("User", "");
                break;
        }        
    }
    
    private void fillResponse() {
        switch (mContent) {
            case ViewMenus:
                // comma-separated list
                mParams.put("MenuList", (new ArrayList<>()).toString()); 
                mParams.put("Return", "OK"); // OK, FAIL
                break;
            case ViewUsers:
                // comma-separated list
                mParams.put("UserList", (new ArrayList<>()).toString()); 
                mParams.put("Return", "OK"); // OK, FAIL
                break;
            case ViewAllergicUsers:
                // comma-separated list
                mParams.put("UserList", (new ArrayList<>()).toString()); 
                mParams.put("Return", "OK"); // OK, FAIL
                break;
            case SetMenu:
                mParams.put("Return", "OK"); // OK, FAIL
                break;
            case SetUser:
                mParams.put("Return", "OK"); // OK, FAIL
                break;
        }

    }
    
    public Type type() {
        return mType;
    }
    
    public Content content() {
        return mContent;
    }

    public void setConntent(Content content) {
        mContent = content;
    }

    
    public void setParam(String k, String v) {
        mParams.put(k, v);
    }

    public String getParam(String k) {
        return mParams.get(k);
    }

    /**
     * @param <k> name of a parameters containing a list compressed into a 
     *                string, of this type "[a, b, c, ...]"
     * @return an ArrayList of the type [a,b,c,...]
    */
    public ArrayList<String> getList(String k) {
        String strList = mParams.get(k);
        ArrayList<String> list = new ArrayList<>(Arrays.asList(
            strList
                .substring(1, strList.length()-1).replaceAll("\\s", "")
                .split(",")
            ));
         return list;
    }

    /**
     * @param <k> name of the parameter in the map 
     * @param <v> list of type [a,b,c,...] to the compressed into a string
     * @return void
    */
    public void setList(String k, ArrayList<String> v) {
        mParams.put(k, v.toString());
    }
    
    
    @Override
    public String toString(){
        String type = (mType == Type.Request ? "Request" : "Response");
        String content;
        switch (mContent) {
            case ViewMenus:
                content = "ViewMenus";
                break;
            case ViewUsers:
                content = "ViewUsers";
                break;
            case ViewAllergicUsers:
                content = "ViewAllergicUsers";
                break;
            case SetMenu:
                content = "SetMenu";
                break;
            case SetUser:
                content = "SetUser";
                break;
            default :
                content = "Unknown";
                break;
        }
        String ret = new String();
        ret += type + " " + content + ", params: " + mParams;
        return ret;
    }
    
    public static void main(String[] args) {
        
        Message msg = new Message(Type.Response, Content.ViewUsers);
        ArrayList<String> users = new ArrayList<>(
            Arrays.asList("John", "Paul", "George", "Ringo"));
        msg.setList("UserList", users);
        // Serialization
        try {
            FileOutputStream file = new FileOutputStream("data/request.ser");
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(msg);
            out.close();
            file.close();
            System.out.println("--- Object has been serialized ---");
            System.out.println(msg);
        } catch (IOException ex) {
            System.out.println("IOException is caught");
        }
        
        msg = null;
        users = null;
        
        // Deserialization
        try {
 
            // Reading the object from a file
            FileInputStream file = new FileInputStream("data/request.ser");
            ObjectInputStream in = new ObjectInputStream(file);
            msg = (Message)in.readObject();
            users = msg.getList("UserList");
            in.close();
            file.close();
            System.out.println("--- Object has been deserialized ---");
            System.out.println(msg);
            System.out.println("paramsToList: " + users);
        } catch (IOException ex) {
            System.out.println("IOException is caught");
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }
        
    }
    
}
