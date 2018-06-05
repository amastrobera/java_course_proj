package canteen;


import org.junit.Test;
import junit.framework.Assert;


public class MenuTest {

    @Test
    public void testValues() {
        Menu m = new Menu();
        
        Assert.assertTrue(m.name().isEmpty());
        Assert.assertTrue(m.date().isEmpty());
        Assert.assertTrue(m.getCourse(Course.Type.First).isEmpty());
        Assert.assertTrue(m.getCourse(Course.Type.Second).isEmpty());
        Assert.assertTrue(m.getCourse(Course.Type.Dessert).isEmpty());
        Assert.assertTrue(m.getCourse(Course.Type.Fruit).isEmpty());
        
        m.setName("menu del giovedi");
        m.setDate("2018-03-12");
        m.setCourse("pappa al pomodoro", Course.Type.First);
        m.setCourse("arrosto di maiale", Course.Type.Second);
        m.setCourse("strudel", Course.Type.Dessert);

        Assert.assertEquals("menu del giovedi", m.name());
        Assert.assertEquals("2018-03-12", m.date());
        Assert.assertEquals("pappa al pomodoro", m.getCourse(Course.Type.First));
        Assert.assertEquals("arrosto di maiale", m.getCourse(Course.Type.Second));
        Assert.assertEquals("strudel", m.getCourse(Course.Type.Dessert));
        Assert.assertTrue(m.getCourse(Course.Type.Fruit).isEmpty());
        
        // test: only one value gets filled among Dessert and Fruit
        m.setCourse("mela", Course.Type.Fruit);
        Assert.assertEquals("mela", m.getCourse(Course.Type.Fruit));
        Assert.assertTrue(m.getCourse(Course.Type.Dessert).isEmpty());
        
        
        m.setCourse("profiteroles", Course.Type.Dessert);
        Assert.assertEquals("profiteroles", m.getCourse(Course.Type.Dessert));
        Assert.assertTrue(m.getCourse(Course.Type.Fruit).isEmpty());
        
    }
    
}
