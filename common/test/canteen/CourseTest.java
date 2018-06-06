package canteen;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;


public class CourseTest {

    Course c1, c2;
    
    @Before
    public void setUp() {
        c1 = new Course();
        
        c2 = new Course();
        c2.name = "pasta al pomodoro";
        c2.type = Course.Type.First;
        c2.ingredients.add("pasta");
        c2.ingredients.add("pomodoro");
    }
    
    @Test
    public void testEmpty() {
        Assert.assertTrue(c1.name.isEmpty());
        Assert.assertTrue(Course.Type.Unknown == c1.type);
        Assert.assertTrue(c1.ingredients.isEmpty());
    }
    
    @Test 
    public void testValue() {
        Assert.assertEquals("pasta al pomodoro", c2.name);
        Assert.assertTrue(Course.Type.First == c2.type);
        Assert.assertEquals(2, c2.ingredients.size());
        Assert.assertEquals("pasta", c2.ingredients.get(0));
        Assert.assertEquals("pomodoro", c2.ingredients.get(1));
    }
}
