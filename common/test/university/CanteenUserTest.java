package university;

import java.util.HashSet;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class CanteenUserTest {

    private CanteenUser p1, p2, p3, p4, p5;
    
    @Before
    public void setUp() {
        p1 = new Student("pietro", "quarta");
        
        p2 = new CanteenUser("matteo", "ricci");
        p2.setPhone("123456789");
        HashSet<String> allergies = new HashSet<>();
        allergies.add("pomodoro");
        allergies.add("burro");
        p2.setAllergies(allergies);
        
        p3 = new Professor("pietro", "quarta");
        
        p4 = new Student("pietro", "quarta");
        
        p5 = new CanteenUser("matteo", "ricci");
    }

    @Test 
    public void testString() {
        Assert.assertEquals("user\nmatteo ricci 123456789\n"+
                            "allergies: burro,pomodoro",
                            p2.toString());
    }
    
    @Test
    public void testEqual() {
        Assert.assertTrue(p1.equals(p4)); // same name and type
        Assert.assertFalse(p1.equals(p3)); // same name, different type
        Assert.assertFalse(p1.equals(p2)); // all different
        Assert.assertTrue(p2.equals(p5));
    }
    
}
