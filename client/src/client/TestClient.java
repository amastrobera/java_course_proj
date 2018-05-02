package client;

import canteen.*;
import university.CanteenUser;

import java.util.ArrayList;
import java.util.HashMap;


class TestClient implements Runnable {
        
    @Override
    public void run() {
        Client client = new Client("localhost", 8080);

        // ---- test ViewCourseInfo(String) ---- 
        System.out.println("------- thread " + 
                        Thread.currentThread().getId() + 
                        " : ViewCourseInfo -------");
        String cName = "Riso alla zucca";
        System.out.println(">> searching for course: " + cName);
        Course cFound = client.getCourseInfo(cName);
        System.out.println("   found: " + cFound);

        // ------- test : ViewCourses (print 2)-------
        System.out.println("------- thread " + 
                        Thread.currentThread().getId() + 
                        " : ViewCourses (print 2)-------");
        HashMap<String, ArrayList<String>> meals = client.getCourses();
        ArrayList<String> first = meals.get("First");
        ArrayList<String> dessert = meals.get("Dessert");
        for (int i = 0; i < 2; ++i) 
            System.out.println(first.get(i));
        for (int i = 0; i < 2; ++i) 
            System.out.println(dessert.get(i));

        // ----- test : ViewUsers (print 2) -------
        System.out.println("------- thread " + 
                        Thread.currentThread().getId() + 
                        " : ViewUsers (print 2)-------");
        ArrayList<CanteenUser> users = client.getUsers();
        for (int i = 0; i < 2; ++i) {
            System.out.println(users.get(i));
        }

        // ----- test : ViewAllergicUsers (print 2) -------
        System.out.println("------- thread " + 
                        Thread.currentThread().getId() + 
                        " : ViewAllergicUsers (print 2)-------");
        Menu menu = new Menu("pranzo di venerdì");
        menu.setCourse("Riso alla zucca", Course.Type.First);
        menu.setCourse("Arrosto di tacchino al forno", Course.Type.Second);
        menu.setCourse("Budino al cioccolato", Course.Type.Dessert);
        menu.setCourse("Macedonia di frutta", Course.Type.Fruit);
        users = client.getAllergicUsers(menu);
        for (int i = 0; i < 2; ++i) {
            System.out.println(users.get(i));
        }
    }

    public static void main(String args[]) {

        System.out.println("--- Test Client --- ");

        // multiple threads testing
        ArrayList<Thread> testPool = new ArrayList<>();

        for (int j = 0; j < 2; ++j)
            testPool.add(new Thread(new TestClient()));
        for (int j = 0; j < 2; ++j)
            testPool.get(j).start();
        for (int j = 0; j < 2; ++j)
            try {
                testPool.get(j).join();
            } catch(Exception ex ) {
            }
    }
}