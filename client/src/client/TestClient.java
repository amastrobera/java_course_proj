package client;

import canteen.*;
import university.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.LinkedList;
import javafx.scene.Parent;

class TestClient implements Runnable {
    
    public void testCourseInfo() {
        System.out.println("------- thread " + 
                        Thread.currentThread().getId() + 
                        " : ViewCourseInfo -------");
        String cName = "Riso alla zucca";
        System.out.println(">> searching for course: " + cName);
        Course cFound = mClient.getCourseInfo(cName);
        System.out.println("   found: " + cFound);
    }

    public void testViewCourses() {
        System.out.println("------- thread " + 
                        Thread.currentThread().getId() + 
                        " : ViewCourses (print 2)-------");
        HashMap<String, ArrayList<String>> meals = mClient.getCourses();
        ArrayList<String> first = meals.get("First");
        ArrayList<String> dessert = meals.get("Dessert");
        for (int i = 0; i < 2; ++i) 
            System.out.println(first.get(i));
        for (int i = 0; i < 2; ++i) 
            System.out.println(dessert.get(i));
    }
    
    public void testViewUsers() {
        System.out.println("------- thread " + 
                        Thread.currentThread().getId() + 
                        " : ViewUsers (print 2)-------");
        ArrayList<CanteenUser> users = mClient.getUsers();
        System.out.println("   found: " + users.size() + " users");
        for (int i = 0; i < 2; ++i) {
            System.out.println(users.get(i));
        }
    }
    
    public void testViewAllergicUsers() {
        System.out.println("------- thread " + 
                        Thread.currentThread().getId() + 
                        " : ViewAllergicUsers (print 2)-------");
        Menu menu = new Menu();
        menu.setName("pranzo di venerdì");
        menu.setCourse("Riso alla zucca", Course.Type.First);
        menu.setCourse("Arrosto di tacchino al forno", Course.Type.Second);
        menu.setCourse("Budino al cioccolato", Course.Type.Dessert);
        menu.setCourse("Macedonia di frutta", Course.Type.Fruit);
        ArrayList<CanteenUser> users = mClient.getAllergicUsers(menu);
        System.out.println("   found: " + users.size() + " users");
        for (int i = 0; i < 2; ++i) {
            System.out.println(users.get(i));
        }
    }
    
    public void testSaveMenu() {
        System.out.println("------- thread " + 
                        Thread.currentThread().getId() + 
                        " : SaveMenu -------");
        Menu menu = new Menu();
        boolean ret;
        
        // first record 
        menu.setName("pranzo di venerdi");
        menu.setCourse("Riso alla milanese", Course.Type.First);
        menu.setCourse("Tonno sott’olio", Course.Type.Second);
        menu.setCourse("Strudel", Course.Type.Dessert);
        menu.setCourse("Banana", Course.Type.Fruit);
        menu.setDate("2018-10-27");
        ret = mClient.saveMenu(menu);
        System.out.println("response: " + ret); 
        
        // second record
        menu = new Menu();
        menu.setName("pranzo di lunedì");    
        menu.setCourse("Riso alla zucca", Course.Type.First);
        menu.setCourse("Arrosto di tacchino al forno", Course.Type.Second);
        menu.setCourse("Budino al cioccolato", Course.Type.Dessert);
        menu.setCourse("Macedonia di frutta", Course.Type.Fruit);
        menu.setDate("2018-10-30");
        ret = mClient.saveMenu(menu);
        System.out.println("response: " + ret);
        
        // duplicate record
        menu = new Menu();
        menu.setName("pranzo di lunedì");    
        menu.setCourse("Riso alla zucca", Course.Type.First);
        menu.setCourse("Arrosto di tacchino al forno", Course.Type.Second);
        menu.setCourse("Budino al cioccolato", Course.Type.Dessert);
        menu.setCourse("Macedonia di frutta", Course.Type.Fruit);
        menu.setDate("2018-10-30");
        ret = mClient.saveMenu(menu);
        System.out.println("response: " + ret);
    }


    public void testSaveCourse() {
        System.out.println("------- thread " + 
                        Thread.currentThread().getId() + 
                        " : SaveCourse -------");
        Course course = new Course();
        boolean ret;
        
        // new record 
        course.name = "Pasta e patate";
        course.type = Course.Type.First;
        course.ingredients = new LinkedList<>(
            Arrays.asList("pasta","olio d'oliva","burro","patate","sale"));
        ret = mClient.saveCourse(course);
        System.out.println("response: " + ret);
        
        // duplicate record
        course.name = "Petto di pollo alla piastra";
        course.type = Course.Type.Second;
        course.ingredients = new LinkedList<>(
            Arrays.asList("pollo","olio d'oliva","peperoni",
                           "origano","sale","pepe"));
        ret = mClient.saveCourse(course);
        System.out.println("response: " + ret);
    }
    
    public void testSaveUser() {
        System.out.println("------- thread " + 
                        Thread.currentThread().getId() + 
                        " : SaveUser -------");
        boolean ret;
        
        // new record 
        Professor user1 = new Professor("Tony", "Morano");
        user1.setPhone("082.54.12.312");
        user1.setAddress(new Address("via Piave 12", "83100", "Avellino"));
        user1.addAllergy("patate");
        user1.addAllergy("cipolla");
        ret = mClient.saveUser(user1);
        System.out.println("response: " + ret);
        
        // duplicate record
        Student user2 = new Student("Alessandro", "Bianchi");
        user2.setPhone("082.54.12.312");
        Person dad = new Person("Mario", "Rossi");
        dad.setPhone("01.12.43.12.43");
        Person mom = new Person("Teresa", "Romano");
        user2.setParents(new Person[]{dad,mom});
        user2.setAddress(new Address("viale Montenero 25", "20121", "Milano"));
        user2.addAllergy("tonno");
        ret = mClient.saveUser(user2);
        System.out.println("response: " + ret);        
    }
    
    private Client mClient; 
    
    public TestClient() {
        mClient = new Client("localhost", 8080);
    }
    
    @Override
    public void run() {
        //testCourseInfo();
        //testViewCourses();
        //testViewUsers();
        //testViewAllergicUsers();
        //testSaveMenu();
        //testSaveCourse();
        testSaveUser();
    }

    public static void main(String args[]) {

        System.out.println("--- Test Client --- ");

        // multiple threads testing
        int numThreads = 1;
        ArrayList<Thread> testPool = new ArrayList<>(numThreads);
        for (int j = 0; j < numThreads; ++j)
            testPool.add(new Thread(new TestClient()));
        
        for (int j = 0; j < numThreads; ++j)
            testPool.get(j).start();
        for (int j = 0; j < numThreads; ++j)
            try {
                testPool.get(j).join();
            } catch(Exception ex ) {
            }
    }
}