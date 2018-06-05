package university;

import junit.framework.Assert;
import org.junit.Test;

public class AddressTest {

    @Test
    public void testValuesConstructor() {
        
        Address a = new Address("via nappi 24", "83100", "Avellino");
        
        Assert.assertEquals("via nappi 24", a.street);
        Assert.assertEquals("83100", a.postcode);
        Assert.assertEquals("Avellino", a.city);

    }

    @Test
    public void testValues() {
        
        Address a = new Address();
        
        Assert.assertTrue(a.street.isEmpty());
        Assert.assertTrue(a.postcode.isEmpty());
        Assert.assertTrue(a.city.isEmpty());
        
        a.street = "via nappi 24";
        a.postcode = "83100";
        a.city = "Avellino";

        Assert.assertEquals("via nappi 24", a.street);
        Assert.assertEquals("83100", a.postcode);
        Assert.assertEquals("Avellino", a.city);
        
    }


    
}
