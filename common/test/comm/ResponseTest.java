package comm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ResponseTest {
    
    private Response r1, r2;
    
    private String filePath;
    
    private FileOutputStream fOutput;
    private ObjectOutputStream oOutput;
    private FileInputStream fInput;
    private ObjectInputStream oInput;
    
    public ResponseTest() {
    }
    
    @Before
    public void setUp() {
        
        filePath = "../data/response.ser";
        
        // crete test file
        try {
            File output = new File(filePath);
            if (output.exists())
                output.delete();
            output.createNewFile();
        } catch (Exception ex) {
            System.err.println(ex);
        }

        // fill initial response
        r1 = new Response("ViewUsers");
        r1.setError("something wrong");
        r1.setStatus(Response.Status.FAILURE);        
    }


    @Test
    public void testFilling() {
        Assert.assertEquals("ViewUsers", r1.type());
        Assert.assertEquals("something wrong", r1.error());
        Assert.assertEquals(Response.Status.FAILURE, r1.status());
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
        try {
            fInput = new FileInputStream(filePath);
            oInput = new ObjectInputStream(fInput);
            r2 = (Response)oInput.readObject();
            oInput.close();
            fInput.close();
        } catch (Exception ex) {
            System.err.println(ex);
        }

        Assert.assertEquals("ViewUsers", r2.type());
        Assert.assertEquals("something wrong", r2.error());
        Assert.assertEquals(Response.Status.FAILURE, r2.status());
    }
    
    @After
    public void tearDown() {
        File output = new File(filePath);
        if (output.exists())
            output.delete();
    }


}
