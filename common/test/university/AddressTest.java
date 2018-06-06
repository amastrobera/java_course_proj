package university;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class AddressTest {

    private Address a1, a2;
    
    @Before
    public void setUp() {
        a1 = new Address();
        a2 = new Address("via nappi 24", "83100", "Avellino");
    }
    
    @Test
    public void testValuesConstructor() {
        Assert.assertEquals("via nappi 24", a2.street);
        Assert.assertEquals("83100", a2.postcode);
        Assert.assertEquals("Avellino", a2.city);
    }

    @Test
    public void testValues() {
        Assert.assertTrue(a1.street.isEmpty());
        Assert.assertTrue(a1.postcode.isEmpty());
        Assert.assertTrue(a1.city.isEmpty());
        
        a1.street = "via nappi 24";
        a1.postcode = "83100";
        a1.city = "Avellino";

        Assert.assertEquals("via nappi 24", a1.street);
        Assert.assertEquals("83100", a1.postcode);
        Assert.assertEquals("Avellino", a1.city);
    }
    
    @Test 
    public void testString() {
        Assert.assertEquals(",,", a1.toString());
        Assert.assertEquals("via nappi 24,83100,Avellino", a2.toString());
    }
    
}
