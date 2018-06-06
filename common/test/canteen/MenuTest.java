package canteen;


import org.junit.Test;
import junit.framework.Assert;
import org.junit.Before;


public class MenuTest {

    private Menu m1, m2;
    
    @Before
    public void setUp() {
        m1 = new Menu();
        
        m2 = new Menu();
        m2.setName("menu del giovedi");
        m2.setDate("2018-03-12");
        m2.setCourse("pappa al pomodoro", Course.Type.First);
        m2.setCourse("arrosto di maiale", Course.Type.Second);
        m2.setCourse("strudel", Course.Type.Dessert);
    }

    @Test
    public void testEmpty() {
        Assert.assertTrue(m1.name().isEmpty());
        Assert.assertTrue(m1.date().isEmpty());
        Assert.assertTrue(m1.getCourse(Course.Type.First).isEmpty());
        Assert.assertTrue(m1.getCourse(Course.Type.Second).isEmpty());
        Assert.assertTrue(m1.getCourse(Course.Type.Dessert).isEmpty());
        Assert.assertTrue(m1.getCourse(Course.Type.Fruit).isEmpty());
    }
    
    @Test
    public void testValue() {

        Assert.assertEquals("menu del giovedi", m2.name());
        Assert.assertEquals("2018-03-12", m2.date());
        Assert.assertEquals("pappa al pomodoro", m2.getCourse(Course.Type.First));
        Assert.assertEquals("arrosto di maiale", m2.getCourse(Course.Type.Second));
        Assert.assertEquals("strudel", m2.getCourse(Course.Type.Dessert));
        Assert.assertTrue(m2.getCourse(Course.Type.Fruit).isEmpty());
        
        // test: only one value gets filled among Dessert and Fruit
        m2.setCourse("mela", Course.Type.Fruit);
        Assert.assertEquals("mela", m2.getCourse(Course.Type.Fruit));
        Assert.assertTrue(m2.getCourse(Course.Type.Dessert).isEmpty());
        
        // test: only one value gets filled among Dessert and Fruit
        m2.setCourse("profiteroles", Course.Type.Dessert);
        Assert.assertEquals("profiteroles", m2.getCourse(Course.Type.Dessert));
        Assert.assertTrue(m2.getCourse(Course.Type.Fruit).isEmpty());
    }
    
}
