package client;

import canteen.*;
import university.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Iterator;


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

    public synchronized void testViewCourses(boolean printCourseList) {

        try {while(!available) wait(3000); } catch (Exception ex) { return; }
        available = false;
        
        System.out.println("------- thread " + 
                        Thread.currentThread().getId() + 
                        " : ViewCourses -------");
        HashMap<String, ArrayList<String>> meals = mClient.getCourses();
        ArrayList<String> first = meals.get("First");
        ArrayList<String> second = meals.get("Second");
        ArrayList<String> dessert = meals.get("Dessert");
        ArrayList<String> fruit = meals.get("Fruit");
        
        System.out.println("   found " +  first.size() + " first courses");
        if (printCourseList)
            for (int i = 0; i < first.size(); ++i) 
                System.out.println("       " + first.get(i));
        
        System.out.println("   found " +  second.size() + " second courses");
        if (printCourseList)
            for (int i = 0; i < second.size(); ++i) 
                System.out.println("       " + second.get(i));
        
        System.out.println("   found " +  dessert.size() + " dessert courses");
        if (printCourseList)
            for (int i = 0; i < dessert.size(); ++i) 
                System.out.println("       " + dessert.get(i));
        
        System.out.println("   found " +  fruit.size() + " fruit courses");
        if (printCourseList)
            for (int i = 0; i < fruit.size(); ++i) 
                System.out.println("       " + fruit.get(i));
        
        available = true;
        notifyAll();
    }
    
    public synchronized void testViewUsers(boolean printUserList) {

        try {while(!available) wait(3000); } catch (Exception ex) { return; }        
        available = false;
        
        System.out.println("------- thread " + 
                        Thread.currentThread().getId() + 
                        " : ViewUsers -------");
        ArrayList<CanteenUser> users = mClient.getUsers();
        System.out.println("   found: " + users.size() + " users");
        if (printUserList)
            for (int i = 0; i < users.size(); ++i)
                System.out.println("       " + users.get(i));

        available = true;
        notifyAll();
    }
    
    
    public synchronized void testViewMenus(boolean printMenuList) {
    
        try {while(!available) wait(3000); } catch (Exception ex) { return; }
        available = false;
        
        System.out.println("------- thread " + Thread.currentThread().getId() + 
                        " : ViewMenus -------");
        HashSet<Menu> menus = mClient.getMenus();
        System.out.println("   found: " + menus.size() + " menus");
        if (printMenuList) {
            Iterator<Menu> mit = menus.iterator();
            while (mit.hasNext())
                System.out.println(mit.next());
        }
        available = true;
        notifyAll();        
    }
    
    public synchronized void testViewAllergicUsers(boolean printUserList) {
        
        try {while(!available) wait(3000); } catch (Exception ex) { return; }
        available = false;
        
        System.out.println("------- thread " + 
                        Thread.currentThread().getId() + 
                        " : ViewAllergicUsers -------");
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
        Menu m1 = new Menu();
        boolean ret;
        
        // first record 
        m1.setName("pranzo di venerdi");
        m1.setCourse("Riso alla milanese", Course.Type.First);
        m1.setCourse("Tonno sott’olio", Course.Type.Second);
        m1.setCourse("Banana", Course.Type.Fruit);
        m1.setCourse("Strudel", Course.Type.Dessert);
        m1.setDate("2018-10-27");
        ret = mClient.saveMenu(m1);
        System.out.println("response: " + ret); 
        
        // second record
        Menu m2 = new Menu();
        m2.setName("pranzo di lunedì");    
        m2.setCourse("Riso alla zucca", Course.Type.First);
        m2.setCourse("Arrosto di tacchino al forno", Course.Type.Second);
        m2.setCourse("Budino al cioccolato", Course.Type.Dessert);
        m2.setDate("2018-10-30");
        ret = mClient.saveMenu(m2);
        System.out.println("response: " + ret);
        
        // duplicate record (same date)
        Menu m3 = new Menu();
        m3.setName("pranzo di lunedì");    
        m3.setCourse("Riso alla zucca", Course.Type.First);
        m3.setCourse("Arrosto di tacchino al forno", Course.Type.Second);
        m3.setCourse("Macedonia di frutta", Course.Type.Fruit);
        m3.setDate("2018-10-30");
        ret = mClient.saveMenu(m3);
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
        
        boolean ret;
        
        // new record 
        Course c1 = new Course();
        c1.name = "Pasta e patate";
        c1.type = Course.Type.First;
        c1.ingredients = new LinkedList<>(
            Arrays.asList("pasta","olio d'oliva","burro","patate","sale"));
        ret = mClient.saveCourse(c1);
        System.out.println("response: " + ret);
        
        // second new record 
        Course c2 = new Course();
        c2.name = "Petto di pollo alla piastra";
        c2.type = Course.Type.Second;
        c2.ingredients = new LinkedList<>(
            Arrays.asList("pollo","olio d'oliva","peperoni",
                           "origano","sale","pepe"));
        ret = mClient.saveCourse(c2);
        System.out.println("response: " + ret);
        
        // duplicate record
        Course c3 = new Course();
        c3.name = "Pasta e patate";
        c3.type = Course.Type.First;
        c3.ingredients = new LinkedList<>(
            Arrays.asList("pasta","olio d'oliva","patate","sale", "verza", "ceci"));
        ret = mClient.saveCourse(c3);
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
        CanteenUser user1 = new Professor("Tony", "Morano");
        user1.setPhone("082.54.12.312");
        user1.setAddress(new Address("via Piave 12", "83100", "Avellino"));
        user1.addAllergy("patate");
        user1.addAllergy("cipolla");
        ret = mClient.saveUser(user1);
        System.out.println("response: " + ret);

        // second record
        CanteenUser user2 = new Student("Alessandro", "Bianchi");
        user2.setPhone("082.54.12.312");
        Person dad2 = new Person("Mario", "Rossi");
        dad2.setPhone("01.12.43.12.43");
        Person mom2 = new Person("Teresa", "Romano");
        ((Student)user2).setParents(new Person[]{dad2,mom2});
        user2.setAddress(new Address("viale Montenero 25", "20121", "Milano"));
        ret = mClient.saveUser(user2);
        System.out.println("response: " + ret);        
        
        // duplicate record
        CanteenUser user3 = new Student("Alessandro", "Bianchi");
        user3.setPhone("082.78.21.431");
        Person dad3 = new Person("Mario", "Bianchi");
        dad3.setPhone("01.12.43.12.43");
        Person mom3 = new Person("Teresa", "Giusti");
        ((Student)user3).setParents(new Person[]{dad3,mom3});
        user3.setAddress(new Address("viale Montenero 20", "20121", "Milano"));
        user3.addAllergy("tonno");
        ret = mClient.saveUser(user3);
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
        testSaveMenu();
        testViewMenus(false);
        
        testSaveCourse();
        testViewCourses(false);
        testCourseInfo();
        
        testSaveUser();
        testViewUsers(false);
        testViewAllergicUsers(false); // true, to view full list
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