package server;

import io.DSVReader;
import io.DSVWriter;
import canteen.*;
import university.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


public class FileDataManager extends DataManager {

    private final String mDataPath;
    private final HashMap<String, DSVReader> mReaders;
    private final HashMap<String, DSVWriter> mWriters;
    
    
    public FileDataManager(String dataPath) {
        mDataPath = dataPath;
        mReaders = new HashMap<>();
        mReaders.put("courses", 
                new DSVReader(mDataPath + "/courses.csv", ",", true, true));
        mReaders.put("users", 
                new DSVReader(mDataPath + "/users.csv", ",", true, true));
        mReaders.put("menus", 
                new DSVReader(mDataPath + "/menus.csv", ",", true, true));
        
        mWriters = new HashMap<>();
        mWriters.put("courses", 
                new DSVWriter(mDataPath + "/courses.csv", ",", true));
        mWriters.put("users", 
                new DSVWriter(mDataPath + "/users.csv", ",", true));
        mWriters.put("menus", 
                new DSVWriter(mDataPath + "/menus.csv", ",", true));
    }
            
    @Override
    public HashMap<String, ArrayList<String>> getCourses() {

        HashMap<String, ArrayList<String>> ret = new HashMap<>();
        
        if (mReaders.containsKey("courses")) {
            
            DSVReader reader = mReaders.get("courses");
            
            ArrayList<String> first = new ArrayList<>();
            ArrayList<String> second = new ArrayList<>();
            ArrayList<String> dessert = new ArrayList<>();
            ArrayList<String> fruit = new ArrayList<>();
            
            HashMap<String,String> line = new HashMap<>();
            while (reader.getNextLine(line)) {
                if (line.size() > 0) {
                    Course c = new Course();
                    c.fromMap(line);
                    switch (c.type) {
                        case First: 
                            first.add(c.name);
                            break;
                        case Second: 
                            second.add(c.name);
                            break;
                        case Dessert: 
                            dessert.add(c.name);
                            break;
                        case Fruit: 
                            fruit.add(c.name);
                            break;
                    }
                    line = new HashMap<>();
                }
            }
            reader.reset(); // the file pointer goes back to the top
            
            ret.put("First", first);
            ret.put("Second", second);
            ret.put("Dessert", dessert);
            ret.put("Fruit", fruit);
        }
        return(ret);
    }
    
    @Override
    public Course findCourse(String name) {
        
        HashMap<String,String> line = new HashMap<>();

        if (mReaders.containsKey("courses")) {
            DSVReader reader = mReaders.get("courses");
            while (reader.getNextLine(line)) {
                if (line.size() > 0) {
                    Course c = new Course();
                    c.fromMap(line);
                    if (c.name.equals(name)) {
                        reader.reset();
                        return c;
                    }
                    line = new HashMap<>();
                }
            }
            reader.reset(); // the file pointer goes back to the top
        }
        return new Course();
    }
    
    @Override
    public ArrayList<Course> findMenu(ArrayList<String> menu){
        
        ArrayList<Course> ret = new ArrayList<>();

        if (mReaders.containsKey("courses")) {
            DSVReader reader = mReaders.get("courses");
        
            HashMap<String,String> line = new HashMap<>();
            while (reader.getNextLine(line)) {
                if (line.size() > 0) {
                    Course c = new Course();
                    c.fromMap(line);
                    for (String course : menu) {
                        if (c.name.equals(course)){
                            ret.add(c);
                            break;
                        }
                    }
                    line = new HashMap<>();
                }
            }
            reader.reset(); // the file pointer goes back to the top
        }
        return(ret);
    }
    
    
    @Override
    public ArrayList<CanteenUser> getUsers() {
        ArrayList<CanteenUser> ret = new ArrayList<>();
                
        HashMap<String,String> line = new HashMap<>();

        if (mReaders.containsKey("users")) {
            DSVReader reader = mReaders.get("users");
        
            while (reader.getNextLine(line)) {
                if (line.size() > 0) {
                    // factory pattern here 
                    CanteenUser user;
                    String type = line.get("Type");
                    if (type.equals("student"))
                        user = new Student();
                    else if (type.equals("professor"))
                        user = new Professor();
                    else
                        user = new CanteenUser();
                    user.fromMap(line);
                    ret.add(user);
                }
                line = new HashMap<>();
            }
            reader.reset(); // file pointer back to the top
        }
        return ret;
    }

    @Override
    public ArrayList<CanteenUser> getAllergicUsers(Menu menu) {
        
        ArrayList<CanteenUser> ret = new ArrayList<>();
        ArrayList<Course> courses = findMenu(menu.courses());        
        
        if (courses.size() > 0 ) { 
            boolean allergic;
            Iterator<Course> cit;
            HashMap<String,String> line = new HashMap<>();
            
            if (mReaders.containsKey("users")) {
                DSVReader reader = mReaders.get("users");
                while (reader.getNextLine(line)) {
                    if (line.size() > 0) {
                        // factory pattern here 
                        CanteenUser user;
                        String type = line.get("Type");
                        if (type.equals("student"))
                            user = new Student();
                        else if (type.equals("professor"))
                            user = new Professor();
                        else
                            user = new CanteenUser();
                        user.fromMap(line);
                        
                        // add user to list if allergic to at 
                        // least one ingredient of one dish on the menu
                        allergic = false;
                        cit = courses.iterator();
                        while (cit.hasNext()) {
                            Course course = cit.next();
                            for (String food : course.ingredients) {
                                if (user.isAllergicTo(food)) {
                                    ret.add(user);
                                    allergic = true;
                                }
                                if (allergic) break;
                            }
                            if (allergic) break;
                        }
                    }
                    line = new HashMap<>();
                }
                reader.reset(); // file pointer back to the top
            }
        }
        return ret;
    }
        
    
    public static void main(String[] args){

        System.out.println("--- test File Scanner (Courses) ---");
        FileDataManager scanner = new FileDataManager("../data");
        
        // ---- test findCourse(String) ---- 
        String cName = "Riso alla zucca";
        System.out.println(">> searching for course: " + cName);
        Course cFound = scanner.findCourse(cName);
        System.out.println("   found: " + cFound);
        
        // ---- test findMenu(ArrayList<String>) ---- 
        ArrayList<String> menu = new ArrayList<>(
                  Arrays.asList("Riso alla zucca", 
                                "Arrosto di tacchino al forno",
                                "Budino al cioccolato",
                                "Macedonia di frutta"));
        System.out.println(">> searching for menu: " + menu);
        ArrayList<Course> found = scanner.findMenu(menu);
        System.out.println("   found " + found.size() + " courses");
        found.forEach((s)-> {System.out.println(s);});
        
        // ---- test getCourses() ---- 
        System.out.println(">> searching all meals ");
        HashMap<String, ArrayList<String>> meals = scanner.getCourses();
        System.out.println("   found " + meals.get("First").size() + " First");
        //meals.get("First").forEach((s)-> {System.out.println(s);});
        System.out.println("   found " + meals.get("Second").size() + " Second");
        //meals.get("Second").forEach((s)-> {System.out.println(s);});        
        System.out.println("   found " + meals.get("Dessert").size() + " Dessert");
        //meals.get("Dessert").forEach((s)-> {System.out.println(s);});
        System.out.println("   found " + meals.get("Fruit").size() + " Fruit");
        //meals.get("Fruit").forEach((s)-> {System.out.println(s);});
        
        
        System.out.println("--- test File Scanner (Users) ---");
        ArrayList<CanteenUser> users = scanner.getUsers();
        System.out.println("    > found " + users.size() + " users\n");
        // for (CanteenUser user : users)
        //     System.out.println(user);
                
        Menu menu2 = new Menu("pranzo del venerdì");
        menu2.setCourse("Orzotto con zucchine e asiago", Course.Type.First);
        menu2.setCourse("Bocconcini di Vitellone in umido", Course.Type.Second);
        menu2.setCourse("Budino al cioccolato", Course.Type.Dessert);
        menu2.setCourse("Smoothie di frutta mista", Course.Type.Fruit);
        System.out.println(">> scanning users allergic to this menu: " + menu2);
        
        users = scanner.getAllergicUsers(menu2);
        System.out.println("    > found " + users.size() + " allergic users\n");
        //for (CanteenUser user : users)
        //    System.out.println(user);
    
        
    }

    
    
}
