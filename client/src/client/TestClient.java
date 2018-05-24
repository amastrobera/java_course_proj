package client;

import canteen.*;
import university.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.LinkedList;


/**
 * This is an integration testing module. To be removed on the second part 
 * of the assignment. It tests the client request. It requires the server
 * to be up and running, and data-folder to be created and non-empty
 * 
 * <p>
 * TestClient contains a Client class, and it is a runnable that runs a series
 * of tests. Because it is a runnable, <b>more than one TestClient (threads)</b>
 * can be run simultaneously. Therefore, we need to <b>synchronise the writing 
 * to console </b> of the results of the two threads so that this output is 
 * visible
 * <p> ---- thread 10: testCourseInfo() ---- </p>
 * <p> [... output thread 10 ...] </p>
 * <p> ---- thread 9: testCourseInfo() ---- </p>
 * <p> [... output thread 9 ...] </p>
 * 
 * <p>
 * In order to to that we need a <b>condition variable</b> ("boolean available"). Each 
 * testFunction will set wait it to be true, set it to false, execute, then 
 * set it to true again and notify the other threads using testFunction
 * </p>
 * 
 * <p>
 * Unfortunately, this only helps the viewer of the test. This module is not
 * testing <b>multiple simultaneous requests</b> sent to the server. 
 * This type of event <b>should be tested in the Server module</b>.
 * </p>
 */

class TestClient implements Runnable {

    private static boolean available = true;
    
    public synchronized void testCourseInfo() {
        
        try {while(!available) wait(3000); } catch (Exception ex) { return; }
        available = false;
        
        System.out.println("------- thread " + 
                        Thread.currentThread().getId() + 
                        " : ViewCourseInfo -------");
        String cName = "Riso alla zucca";
        System.out.println(">> searching for course: " + cName);
        Course cFound = mClient.getCourseInfo(cName);
        System.out.println("   found: " + cFound);
        
        available = true;
        notifyAll();
    }

    public synchronized void testViewCourses() {

        try {while(!available) wait(3000); } catch (Exception ex) { return; }
        available = false;
        
        System.out.println("------- thread " + 
                        Thread.currentThread().getId() + 
                        " : ViewCourses (print 2)-------");
        HashMap<String, ArrayList<String>> meals = mClient.getCourses();
        ArrayList<String> first = meals.get("First");
        ArrayList<String> dessert = meals.get("Dessert");
        if (first.size() > 0)
            for (int i = 0; i < 2; ++i) 
                System.out.println(first.get(i));
        if (dessert.size() > 0)
            for (int i = 0; i < 2; ++i) 
                System.out.println(dessert.get(i));

        available = true;
        notifyAll();
    }
    
    public synchronized void testViewUsers() {

        try {while(!available) wait(3000); } catch (Exception ex) { return; }
        available = false;
        
        System.out.println("------- thread " + 
                        Thread.currentThread().getId() + 
                        " : ViewUsers (print 2)-------");
        ArrayList<CanteenUser> users = mClient.getUsers();
        System.out.println("   found: " + users.size() + " users");
        if (users.size() > 0)
            for (int i = 0; i < 2; ++i)
                System.out.println(users.get(i));
        
        available = true;
        notifyAll();
    }
    
    public synchronized void testViewAllergicUsers(boolean printUserList) {
        
        try {while(!available) wait(3000); } catch (Exception ex) { return; }
        available = false;
        
        System.out.println("------- thread " + 
                        Thread.currentThread().getId() + 
                        " : ViewAllergicUsers (print 2)-------");
        Menu menu = new Menu();
        menu.setName("pranzo di venerdì");
        menu.setCourse("Riso alla zucca", Course.Type.First);
        menu.setCourse("Arrosto di tacchino al forno", Course.Type.Second);
        menu.setCourse("Macedonia di frutta", Course.Type.Fruit); // will be deleted
        menu.setCourse("Budino al cioccolato", Course.Type.Dessert); 
        ArrayList<CanteenUser> users = mClient.getAllergicUsers(menu);
        
        ArrayList<CanteenUser> first = new ArrayList<>();
        ArrayList<CanteenUser> second = new ArrayList<>();
        ArrayList<CanteenUser> dessert = new ArrayList<>();
        ArrayList<CanteenUser> fruit = new ArrayList<>();

        long numUsers = mClient.getNumberOfUsers();
        
        System.out.println("   found: " + numUsers + " in database");
        
        for (CanteenUser user : users) {
            if (user.isAllergicTo(menu.getCourse(Course.Type.First)))
                first.add(user);
            if (user.isAllergicTo(menu.getCourse(Course.Type.Second)))
                second.add(user);
            if (user.isAllergicTo(menu.getCourse(Course.Type.Dessert)))
                dessert.add(user);
            if (user.isAllergicTo(menu.getCourse(Course.Type.Fruit)))
                fruit.add(user);
        }
        
        System.out.println("   found: " + first.size() + "(" +
                            (first.size()*100l)/numUsers + "%)" + 
                            " users allergic to "+ 
                            menu.getCourse(Course.Type.First));
        if (printUserList)
            for (int i = 0; i < first.size(); ++i) 
                System.out.println("           "  + 
                                    first.get(i).name() + " " + 
                                    first.get(i).surname() + " " +
                                    first.get(i).type());

        System.out.println("   found: " + second.size() + "(" +
                            (second.size()*100l)/numUsers + "%)" + 
                            " users allergic to "+ 
                            menu.getCourse(Course.Type.Second));
        if (printUserList)
            for (int i = 0; i < second.size(); ++i) 
                System.out.println("           "  + 
                                    second.get(i).name() + " " + 
                                    second.get(i).surname() + " " +
                                    second.get(i).type());
        
        if (!menu.getCourse(Course.Type.Dessert).isEmpty()) {
            System.out.println("   found: " + dessert.size() + "(" +
                                (dessert.size()*100l)/numUsers + "%)" + 
                                " users allergic to "+ 
                                menu.getCourse(Course.Type.Dessert));
            if (printUserList)
                for (int i = 0; i < dessert.size(); ++i) 
                    System.out.println("           "  + 
                                        dessert.get(i).name() + " " + 
                                        dessert.get(i).surname() + " " +
                                        dessert.get(i).type());
        }
        
        if (!menu.getCourse(Course.Type.Fruit).isEmpty()) {
            System.out.println("   found: " + fruit.size() + "(" +
                                (fruit.size()*100l)/numUsers + "%)" + 
                                " users allergic to "+ 
                                menu.getCourse(Course.Type.Fruit));
            if (printUserList)
                for (int i = 0; i < fruit.size(); ++i) 
                    System.out.println("           "  + 
                                        fruit.get(i).name() + " " + 
                                        fruit.get(i).surname() + " " +
                                        fruit.get(i).type());
        }
        
        available = true;
        notifyAll();
    }
    
    public synchronized void testSaveMenu() {

        try {while(!available) wait(3000); } catch (Exception ex) { return; }
        available = false;
        
        System.out.println("------- thread " + 
                        Thread.currentThread().getId() + 
                        " : SaveMenu -------");
        Menu menu = new Menu();
        boolean ret;
        
        // first record 
        menu.setName("pranzo di venerdi");
        menu.setCourse("Riso alla milanese", Course.Type.First);
        menu.setCourse("Tonno sott’olio", Course.Type.Second);
        menu.setCourse("Banana", Course.Type.Fruit);
        menu.setCourse("Strudel", Course.Type.Dessert);
        menu.setDate("2018-10-27");
        ret = mClient.saveMenu(menu);
        System.out.println("response: " + ret); 
        
        // second record
        menu = new Menu();
        menu.setName("pranzo di lunedì");    
        menu.setCourse("Riso alla zucca", Course.Type.First);
        menu.setCourse("Arrosto di tacchino al forno", Course.Type.Second);
        menu.setCourse("Budino al cioccolato", Course.Type.Dessert);
        menu.setDate("2018-10-30");
        ret = mClient.saveMenu(menu);
        System.out.println("response: " + ret);
        
        // duplicate record
        menu = new Menu();
        menu.setName("pranzo di lunedì");    
        menu.setCourse("Riso alla zucca", Course.Type.First);
        menu.setCourse("Arrosto di tacchino al forno", Course.Type.Second);
        menu.setCourse("Macedonia di frutta", Course.Type.Fruit);
        menu.setDate("2018-10-30");
        ret = mClient.saveMenu(menu);
        System.out.println("response: " + ret);
        
        available = true;
        notifyAll();
    }


    public synchronized void testSaveCourse() {
        
        try {while(!available) wait(3000); } catch (Exception ex) { return; }
        available = false;
        
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
        
        available = true;
        notifyAll();
    }
    
    public synchronized void testSaveUser() {

        try {while(!available) wait(3000); } catch (Exception ex) { return; }
        available = false;

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
        
        available = true;
        notifyAll();
    }
    
    private Client mClient; 
    
    public TestClient() {
        mClient = new Client("localhost", 8080);
    }
    
    @Override
    public void run() {
        //testCourseInfo(); // no
        //testViewCourses();
        //testViewUsers();
        //testViewAllergicUsers(false); // true, to view full list  // no
        //testSaveMenu(); // no
        //testSaveCourse(); // no
        testSaveUser();
    }

    public static void main(String args[]) {

        System.out.println("--- Test Client --- ");

        // multiple clients requests
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