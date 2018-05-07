package comm;

import university.CanteenUser;
import java.util.ArrayList;


import java.io.*; // for testing only, used in main()


public class ViewUsersResponse extends Response {
    
    private ArrayList<CanteenUser> mUsers;
    
    public ViewUsersResponse() {
        super("ViewUsers");
        mUsers = new ArrayList<>();
    }
   
    public ArrayList<CanteenUser> getUserList() {
        return mUsers;
    }
    
    public void setUserList(ArrayList<CanteenUser> users) {
        mUsers = users;
    }
    
    
    public static void main(String[] args) {
        // this is a module-level test (not to be used)
        
        ViewUsersResponse res = new ViewUsersResponse();
        ArrayList<CanteenUser> users = new ArrayList<>();
        users.add(new CanteenUser("franco", "manca"));
        users.add(new CanteenUser("franco", "pepe"));
        res.setUserList(users);
        
        // Serialization
        try {
            FileOutputStream file = new FileOutputStream("../data/response.ser");
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(res);
            out.close();
            file.close();
            System.out.println("--- Object has been serialized ---");
            System.out.println(res);
        } catch (Exception ex) {
            System.out.println("caught " + ex );
        }
        
        res = null;
        
        // Deserialization
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream("../data/response.ser");
            ObjectInputStream in = new ObjectInputStream(file);
            res = (ViewUsersResponse)in.readObject();
            users = res.getUserList();
            in.close();
            file.close();
            System.out.println("--- Object has been deserialized ---");
            System.out.println(res);
            System.out.println("paramsToList: " + users);
        } catch (Exception ex) {
            System.out.println("caught " + ex );
        }
    }
    
}
