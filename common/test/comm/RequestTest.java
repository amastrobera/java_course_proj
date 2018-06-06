package comm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RequestTest {
    
    private Request r1, r2;
    
    private String filePath;
    
    private FileOutputStream fOutput;
    private ObjectOutputStream oOutput;
    private FileInputStream fInput;
    private ObjectInputStream oInput;
    
    public RequestTest() {
    }
    
    @Before
    public void setUp() {
        
        filePath = "../data/request.ser";
        
        // crete test file
        try {
            File output = new File(filePath);
            if (output.exists())
                output.delete();
            output.createNewFile();
        } catch (Exception ex) {
            System.err.println(ex);
        }

        // fill initial request
        r1 = new Request("ViewUsers");
        r1.setParam("param1", "ciao");
        ArrayList<String> users = new ArrayList<>(
            Arrays.asList("John", "Paul", "George", "Ringo"));
        r1.setList("UserList", users);
        
    }


    @Test
    public void testFilling() {
        Assert.assertEquals("ViewUsers", r1.type());
        ArrayList<String> users = r1.getList("UserList");
        Assert.assertEquals(4, users.size());
        Assert.assertEquals("[John, Paul, George, Ringo]", users.toString());
        Assert.assertEquals("ciao", r1.getParam("param1"));        
    }
    
    @Test
    public void testDeserialize() {
        
        // Serialization
        try {
            fOutput = new FileOutputStream(filePath);
            oOutput = new ObjectOutputStream(fOutput);
            oOutput.writeObject(r1);
            oOutput.close();
            fOutput.close();
        } catch (Exception ex) {
            System.err.println(ex);
        }
        
        // Deserialization
        ArrayList<String> users = new ArrayList<>();
        try {
            fInput = new FileInputStream(filePath);
            oInput = new ObjectInputStream(fInput);
            
            r2 = (Request)oInput.readObject();
            users = r2.getList("UserList");
            oInput.close();
            fInput.close();
        } catch (Exception ex) {
            System.err.println(ex);
        }

        Assert.assertEquals(4, users.size());
        Assert.assertEquals("[John, Paul, George, Ringo]", users.toString());
        Assert.assertEquals("ciao", r2.getParam("param1"));
        
}
    
    @After
    public void tearDown() {
        File output = new File(filePath);
        if (output.exists())
            output.delete();
    }

}
