package university;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class PersonTest {

    private Person p1, p2, p3, p4;
    
    @Before
    public void setUp() {
        p1 = new Person("marco", "maggioli");
        
        p2 = new Person("hamady", "ndiaye");
        p2.setPhone("123456789");
        
        p3 = new Person("gianmarco", "pozzecco");
        p3.setAddress(new Address("Piazza Azzarita 8", "40121", "Bologna"));
        p3.setPhone("987654321");
        
        p4 = new Person("hamady", "ndiaye");
    }
    
    @Test 
    public void testString() {
        Assert.assertEquals(p1.name() + " " + p1.surname(), p1.toString());
        Assert.assertEquals(p2.name() + " " + p2.surname() + " " + p2.phone(), 
                            p2.toString());
        Assert.assertEquals(p3.name() + " " + p3.surname() + " " + p3.phone() + 
                            " " + p3.address(), 
                            p3.toString());
    }
    
    @Test
    public void testEqual() {
        Assert.assertTrue(p2.equals(p4)); // same name
        Assert.assertFalse(p2.equals(p3));
    }
    
}
