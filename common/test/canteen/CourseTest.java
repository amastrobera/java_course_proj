package canteen;

import junit.framework.Assert;
import org.junit.Test;


public class CourseTest {
        
    @Test
    public void testValues() {
        Course c = new Course();
        
        Assert.assertTrue(c.name.isEmpty());
        Assert.assertTrue(Course.Type.Unknown == c.type);
        Assert.assertTrue(c.ingredients.isEmpty());
        
        c.name = "pasta al pomodoro";
        c.type = Course.Type.First;
        c.ingredients.add("pasta");
        c.ingredients.add("pomodoro");

        Assert.assertEquals("pasta al pomodoro", c.name);
        Assert.assertTrue(Course.Type.First == c.type);
        Assert.assertEquals(2, c.ingredients.size());
        Assert.assertEquals("pasta", c.ingredients.get(0));
        Assert.assertEquals("pomodoro", c.ingredients.get(1));
    }
    
}
