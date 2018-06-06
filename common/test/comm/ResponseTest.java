package comm;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResponseTest {
    
    public ResponseTest() {
    }
    
    @Before
    public void setUp() {
        Response msg = new Response("ViewUsers");
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
            msg = (Response)in.readObject();
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
    
    @After
    public void tearDown() {
    }


}
