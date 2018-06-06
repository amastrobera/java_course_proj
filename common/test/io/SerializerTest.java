package io;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.Serializable;

class MyStruct implements Serializable {

    public String tag;
    public int num;

    public MyStruct(String t, int n) {
        tag = t;
        num = n;
    }

    public MyStruct() {
        this("", 0);
    }        

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof MyStruct)) return false;
        MyStruct comp = (MyStruct) obj;
        return    comp.tag.equals(tag)
               && comp.num == num;
    }

}



public class SerializerTest {
    
    private MyStruct o1, o2;
    private String filePath = "../data/test.ser";
    private SerialWriter<MyStruct> writer;
    private SerialReader<MyStruct> reader;
    
    @Before
    public void setUp() {
        
        o1 = new MyStruct("ciao", 12);
        
        File output = new File(filePath);
        if (output.exists())
            output.delete();
        
        writer = new SerialWriter<>(filePath);
    }
    
    @After
    public void tearDown() {
        writer.close();
        File output = new File(filePath);
        if (output.exists())
            output.delete();
    }

    @Test
    public void testWriteAndRead(){
        
        Assert.assertEquals("ciao", o1.tag);
        Assert.assertEquals(12, o1.num);
        
        
        Assert.assertTrue((new File(filePath)).exists());
        
        try {
            writer.writeNextLine(o1);
            
            reader = new SerialReader<>(filePath);
            o2 = reader.getNextLine();
            reader.close();
        } catch(Exception ex) {
            System.err.println(ex);
        }
        
        Assert.assertEquals(o1, o2);
        
    }
}
