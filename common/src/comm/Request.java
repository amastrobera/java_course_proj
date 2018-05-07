package comm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;

import java.io.*; // for testing only, used in main()

public class Request implements Serializable {
    
    private final String mType;    
    protected HashMap<String,String> mParams;
    
    public Request(String type) {
        mType = type;
        mParams = new HashMap<>();
    }
        
    public String type() {
        return mType;
    }
    
    public void setParam(String k, String v) {
        mParams.put(k, v);
    }

    public void setParams(HashMap<String,String> packed) {
        mParams = packed;
    }
    
    public String getParam(String k) {
        String p = new String();
        try {
            p = mParams.get(k);
        } catch(Exception ex) {
            return "";
        }
        return p;
    }

    public HashMap<String,String> params() {
        return mParams;
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
                .substring(1, strList.length()-1)
                .split(",")
            ));
        list.replaceAll(String::trim);
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
        return "Request " + mType + ", params: " + mParams;
    }

    
    public static void main(String[] args) {
        // this is a module-level test (not to be used)
        
        Request msg = new Request("ViewUsers");
        ArrayList<String> users = new ArrayList<>(
            Arrays.asList("John", "Paul", "George", "Ringo"));
        msg.setList("UserList", users);
        // Serialization
        try {
            FileOutputStream file = new FileOutputStream("../data/request.ser");
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
            FileInputStream file = new FileInputStream("../data/request.ser");
            ObjectInputStream in = new ObjectInputStream(file);
            msg = (Request)in.readObject();
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
