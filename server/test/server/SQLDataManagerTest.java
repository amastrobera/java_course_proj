package server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import junit.framework.Assert;

import canteen.*;
import university.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;



public class SQLDataManagerTest {
    
    private SQLDataManager manager;
    
    @Before
    public void setUp() {

        manager = new SQLDataManager("meals_and_allergies", "angelo", "angelo");
        
        if (!manager.isReady()) {
            throw new RuntimeException(
                   "Database 'meals_and_allergies' not ready: cannot run "+
                   "JUNIT tests with user 'angelo' pass 'angelo'.\n" + 
                   "Create a database by loading the dump /sql/data_setup.sql");
        }
    }
    
    @After
    public void tearDown() {
        manager.close();
    }
    
    @Test
    public void testCourseInfo() {
        Course c = manager.findCourse("Riso alla zucca");
        Assert.assertEquals("Riso alla zucca", c.name);
        Assert.assertEquals(Course.Type.First, c.type);
        Assert.assertEquals("[riso, zucca, latte, farina, olio d'oliva, sale]", 
                            c.ingredients.toString());
    }
    
    
    @Test 
    public void testViewCourses() {
        
        HashMap<String, ArrayList<String>> meals = manager.getCourses();
        
        Assert.assertFalse(meals.isEmpty());


        // 1. there exist a list of First courses. 
        // 2. it is not null
        Assert.assertTrue(meals.containsKey("First"));
        ArrayList<String> first = meals.get("First");
        Assert.assertTrue("there is no first course saved to test",
                            first.size() > 0);
        
        // same for Second
        Assert.assertTrue(meals.containsKey("Second"));
        ArrayList<String> second = meals.get("Second");
        Assert.assertTrue("there is no second course saved to test",
                            second.size() > 0);
        
        // same for Dessert
        Assert.assertTrue(meals.containsKey("Dessert"));
        ArrayList<String> dessert = meals.get("Dessert");
        Assert.assertTrue("there is no dessert course saved to test",
                            dessert.size() > 0);
        
        // same for Fruit
        Assert.assertTrue(meals.containsKey("Fruit"));
        ArrayList<String> fruit = meals.get("Fruit");
        Assert.assertTrue("there is no fruit course saved to test",
                            fruit.size() > 0 );
    }
    
    @Test
    public void testViewUsers(){
        // there exist users in the database or file
        ArrayList<CanteenUser> users = manager.getUsers();
        Assert.assertTrue("there is no user saved to test", users.size() > 0 );
    }
    
    @Test
    public void testViewMenus() {
        // there exist menus in the database or file
        HashSet<Menu> menus = manager.getMenus();
        Assert.assertTrue("there is no menu saved to test", menus.size() > 0);
    }
    
    @Test
    public void testViewAllergicUsers(){

        Menu menu = new Menu();
        menu.setCourse("Pasta ai broccoli", Course.Type.First);
        menu.setCourse("Arrosto di tacchino al forno", Course.Type.Second);
        menu.setCourse("Budino al cioccolato", Course.Type.Dessert); 
        
        ArrayList<CanteenUser> users = manager.getAllergicUsers(menu);
        
        // 1. there are allergic users
        Assert.assertTrue("no allergic users to " + menu.toString(), 
                          users.size() > 0);
        
        // 2. (with my dataset) there are allergic users to all these dishes
        ArrayList<CanteenUser> first = new ArrayList<>();
        ArrayList<CanteenUser> second = new ArrayList<>();
        ArrayList<CanteenUser> dessert = new ArrayList<>();
        
        for (CanteenUser user : users) {
            if (user.isAllergicTo(menu.getCourse(Course.Type.First)))
                first.add(user);
            if (user.isAllergicTo(menu.getCourse(Course.Type.Second)))
                second.add(user);
            if (user.isAllergicTo(menu.getCourse(Course.Type.Dessert)))
                dessert.add(user);
        }
        
        Assert.assertTrue("no allergic users to " +
                          menu.getCourse(Course.Type.First),
                          first.size() > 0);
        Assert.assertTrue("no allergic users to " +
                          menu.getCourse(Course.Type.Second),
                            second.size() > 0);
        Assert.assertTrue("no allergic users to " +
                          menu.getCourse(Course.Type.Dessert),
                            dessert.size() > 0);
    }
    
    @Test
    public void testGetNumberOfUsers() {
        Assert.assertTrue("there is no user", manager.getNumberOfUsers("") > 0);
        Assert.assertTrue("there is no student",
                                manager.getNumberOfUsers("student") > 0); 
        Assert.assertTrue("there is no professor",
                                manager.getNumberOfUsers("professor") > 0);
    }
    
    @Test
    public void testSaveMenu() {
        
        // first record 
        Menu m1 = new Menu();
        m1.setName("pranzo di venerdi");
        m1.setCourse("Riso alla milanese", Course.Type.First);
        m1.setCourse("Tonno sott’olio", Course.Type.Second);
        m1.setCourse("Banana", Course.Type.Fruit);
        m1.setCourse("Strudel", Course.Type.Dessert);
        m1.setDate("2018-10-27");
        boolean r1 = manager.saveMenu(m1);
        Assert.assertTrue("failed to save menu " + m1.toString(), r1); 
        
        // second record
        Menu m2 = new Menu();
        m2.setName("pranzo di lunedì");    
        m2.setCourse("Riso alla zucca", Course.Type.First);
        m2.setCourse("Arrosto di tacchino al forno", Course.Type.Second);
        m2.setCourse("Budino al cioccolato", Course.Type.Dessert);
        m2.setDate("2018-10-30");
        boolean r2 = manager.saveMenu(m2);
        Assert.assertTrue("failed to save menu " + m2.toString(), r2);
        
        // duplicate record (same date)
        Menu m3 = new Menu();
        m3.setName("pranzo di lunedì");    
        m3.setCourse("Riso alla zucca", Course.Type.First);
        m3.setCourse("Arrosto di tacchino al forno", Course.Type.Second);
        m3.setCourse("Macedonia di frutta", Course.Type.Fruit);
        m3.setDate("2018-10-30");
        boolean r3 = manager.saveMenu(m3);
        Assert.assertTrue("failed to save menu " + m3.toString(), r3);
        
        int foundAll = 0;
        for (Menu menu : manager.getMenus())
            if (menu.equals(m1) || menu.equals(m3))
                ++foundAll;
        
        Assert.assertEquals("failed to find all menus saved", 2, foundAll);
        
    }
  
    @Test
    public void testSaveCourse(){
        
        // new record 
        Course c1 = new Course();
        c1.name = "Pasta e patate";
        c1.type = Course.Type.First;
        c1.ingredients = new LinkedList<>(
            Arrays.asList("pasta","olio d'oliva","burro","patate","sale"));
        boolean r1 = manager.saveCourse(c1);
        Assert.assertTrue("failed to save course " + c1.toString(), r1);
        
        // second new record 
        Course c2 = new Course();
        c2.name = "Petto di pollo alla piastra";
        c2.type = Course.Type.Second;
        c2.ingredients = new LinkedList<>(
            Arrays.asList("pollo","olio d'oliva","peperoni",
                           "origano","sale","pepe"));
        boolean r2 = manager.saveCourse(c2);
        Assert.assertTrue("failed to save course " + c2.toString(), r2);
        
        // duplicate record
        Course c3 = new Course();
        c3.name = "Pasta e patate";
        c3.type = Course.Type.First;
        c3.ingredients = new LinkedList<>(
            Arrays.asList("pasta","olio d'oliva","patate","sale", "verza", "ceci"));
        boolean r3 = manager.saveCourse(c3);
        Assert.assertTrue("failed to save course " + c3.toString(), r3);

        int foundAll = 0;
        if (!manager.findCourse(c1.name).name.isEmpty())
            ++foundAll;
        if (!manager.findCourse(c3.name).name.isEmpty())
            ++foundAll;
        
        Assert.assertEquals("failed to find all menus saved", 2, foundAll);
    }
    

    @Test
    public void testSaveUser() {

        // new record 
        CanteenUser user1 = new Professor("Tony", "Morano");
        user1.setPhone("082.54.12.312");
        user1.setAddress(new Address("via Piave 12", "83100", "Avellino"));
        user1.addAllergy("patate");
        user1.addAllergy("cipolla");
        boolean r1 = manager.saveUser(user1);
        Assert.assertTrue("failed to save user " + user1.toString(), r1);

        // second record
        CanteenUser user2 = new Student("Alessandro", "Bianchi");
        user2.setPhone("082.54.12.312");
        Person dad2 = new Person("Mario", "Rossi");
        dad2.setPhone("01.12.43.12.43");
        Person mom2 = new Person("Teresa", "Romano");
        ((Student)user2).setParents(new Person[]{dad2,mom2});
        user2.setAddress(new Address("viale Montenero 25", "20121", "Milano"));
        boolean r2 = manager.saveUser(user2);
        Assert.assertTrue("failed to save user " + user2.toString(), r2);
        
        // duplicate record
        CanteenUser user3 = new Student("Alessandro", "Bianchi");
        user3.setPhone("082.78.21.431");
        Person dad3 = new Person("Mario", "Bianchi");
        dad3.setPhone("01.12.43.12.43");
        Person mom3 = new Person("Teresa", "Giusti");
        ((Student)user3).setParents(new Person[]{dad3,mom3});
        user3.setAddress(new Address("viale Montenero 20", "20121", "Milano"));
        user3.addAllergy("tonno");
        boolean r3 = manager.saveUser(user3);
        Assert.assertTrue("failed to save user " + user3.toString(), r3);


        int foundAll = 0;
        for (CanteenUser user : manager.getUsers()) 
            if (user.equals(user1) || user.equals(user3))
                ++foundAll;
        
        Assert.assertEquals("failed to find all users saved", 2, foundAll);
    }
    
    
}
