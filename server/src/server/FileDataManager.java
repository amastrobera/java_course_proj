package server;

import io.*;
import canteen.*;
import university.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.io.File;


public class FileDataManager extends DataManager {

    private final String mDataPath;
    private final HashMap<String, DSVReader> mReaders;
    private final HashMap<String, DSVWriter> mWriters;
    
    
    public FileDataManager(String dataPath) {
        mDataPath = dataPath;
        
        mReaders = new HashMap<>();
        mWriters = new HashMap<>();
        
        initUsers();
        initCourses();
        initMenus();
    }

    private void initUsers() {
        mReaders.put("users", 
                new DSVReader(mDataPath + "/users.csv", ",", true, true));

        mWriters.put("users", 
                new DSVWriter(mDataPath + "/users.csv", ",", true, true, false));
        mWriters.get("users").setHeaders(new ArrayList<String>(
            Arrays.asList("Name","Surname","Type","Telephone","Address",
                          "Parents","Allergies","Notes")));
    }
    
    private void initCourses() {
        mReaders.put("courses", 
                new DSVReader(mDataPath + "/courses.csv", ",", true, true));

        mWriters.put("courses", 
                new DSVWriter(mDataPath + "/courses.csv", ",", true, true, false));
        mWriters.get("courses").setHeaders(new ArrayList<String>(
            Arrays.asList("Name","Type","Ingredients")));
    }
    
    private void initMenus(){
        mReaders.put("menus", 
        new DSVReader(mDataPath + "/menus.csv", ",", true, true));

        mWriters.put("menus", 
                new DSVWriter(mDataPath + "/menus.csv", ",", true, true, false));
        mWriters.get("menus").setHeaders(new ArrayList<String>(
            Arrays.asList("Date","Name","First","Second","Dessert","Fruit")));
    }
    
    
    private void switchFiles(String newFilePath, String oldFilePath) {    
        File oldFile = new File(oldFilePath);
        oldFile.delete();
        File newFile = new File(newFilePath);
        newFile.renameTo(new File(oldFilePath));
    }

    /** 
     * @param <none>
     * @return Array of all courses in our databases
     */    
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
    
    /**
     * 
     * @param <name> of the course
     * @return Course class corresponding to that name, including ingredients
     */
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
    
    
    /**
     * 
     * @param <menu> array with the name of the four course menu
     * @return Array of the corresponding Course class (including ingredients)
     */
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
    
    private CanteenUser userFactoryFromMap(HashMap<String, String> line) {
        CanteenUser user;
        try {
            String type = line.get("Type");
            if (type.equals("student"))
                user = new Student();
            else if (type.equals("professor"))
                user = new Professor();
            else
                user = new CanteenUser();
            user.fromMap(line);
        } catch(Exception ex) {
            System.err.println("createUserFromMap: " + ex);
            user = new CanteenUser();
        }
        return user;
    }
    
    /**
    * @param <none> 
    * 
    * @return Array of users in our database or file-system
    */
    @Override
    public ArrayList<CanteenUser> getUsers() {
        ArrayList<CanteenUser> ret = new ArrayList<>();
                
        HashMap<String,String> line = new HashMap<>();

        if (mReaders.containsKey("users")) {
            DSVReader reader = mReaders.get("users");
        
            while (reader.getNextLine(line)) {
                if (line.size() > 0) {
                    CanteenUser user = userFactoryFromMap(line);
                    ret.add(user);
                }
                line = new HashMap<>();
            }
            reader.reset(); // file pointer back to the top
        }
        return ret;
    }

    /**
    * @param <menu> a collection of four courses (only the name)
    * 
    * @return Array of users that are allergic to any ingredient of any of the 
    * four courses of the menu
    */
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
                        CanteenUser user = userFactoryFromMap(line);
                        // add user to list if allergic to at 
                        // least one ingredient of one dish on the menu
                        // and make its "allergies" map contain the dishes he 
                        // is allergic to
                        HashSet<String> allergicCourses = new HashSet<>();
                        cit = courses.iterator();
                        while (cit.hasNext()) {
                            Course course = cit.next();
                            for (String ingredient : course.ingredients) {
                                if (user.isAllergicTo(ingredient)) {
                                    allergicCourses.add(course.name);
                                    break;
                                }
                            }
                        }
                        if (!allergicCourses.isEmpty()) {
                            user.setAllergies(allergicCourses);
                            ret.add(user);
                        }
                    }
                    line = new HashMap<>();
                }
                reader.reset(); // file pointer back to the top
            }
        }
        return ret;
    }
    
    /**
     * 
     * @param <type> "student", "professor", or "" for all users
     * @return count of users in the database
     */
    @Override
    public long getNumberOfUsers(String type) {
        long num = 0;
        if (type.isEmpty()) {
            if (mReaders.containsKey("users")) {
                DSVReader reader = mReaders.get("users");
                HashMap<String,String> line = new HashMap<>();
                while (reader.getNextLine(line)) {
                    if (line.size() > 0)
                        ++num;
                    line = new HashMap<>();
                }
                reader.reset(); // file pointer back to the top
            } else {
                return -1;
            }
        } else {
            if (mReaders.containsKey("users")) {
                DSVReader reader = mReaders.get("users");
                HashMap<String,String> line = new HashMap<>();
                while (reader.getNextLine(line)) {
                    if (line.size() > 0) {
                        CanteenUser user = userFactoryFromMap(line);
                        if (user.type().equals(type))
                            ++num;
                    }
                    line = new HashMap<>();
                }
                reader.reset(); // file pointer back to the top
            } else {
                return -1;
            }
        }
        return num;
    }
    
    private boolean saveObjet(Packable entry, String fileName) {
        
        // function usable only for Menu, User, and Course
        String cName = entry.getClass().getName();
        if (!cName.equals(Menu.class.getName()) && 
            !cName.equals(Course.class.getName()) && 
            !(cName.equals(CanteenUser.class.getName()) || 
             cName.equals(Student.class.getName()) || 
             cName.equals(Professor.class.getName())))
            return false; // cannot assess the type

        if (!fileName.equals("menus") && 
            !fileName.equals("courses") && 
            !fileName.equals("users"))
            return false; // cannot assess the file source
        
        if (mReaders.containsKey(fileName)) {
            DSVReader reader = mReaders.get(fileName);
            reader.reset(false);
            long linesToKey = 0;
            boolean mustEdit = false;
            HashMap<String,String> line = new HashMap<>();
            while (reader.getNextLine(line)) {
                Packable curData;
                if (cName.equals(Menu.class.getName())) {
                    curData = new Menu();
                } else if (cName.equals(Course.class.getName())) {
                    curData = new Course();
                } else if (cName.equals(CanteenUser.class.getName()) || 
                           cName.equals(Student.class.getName()) || 
                           cName.equals(Professor.class.getName())) {
                    if (line.get("Type").equals("professor"))
                        curData = new Professor();
                    else if (line.get("Type").equals("student"))
                        curData = new Student();
                    else
                        curData = new CanteenUser();
                } else
                    return false; // cannot assess the type
                
                curData.fromMap(line);
                if (curData.equals(entry)) {
                    System.err.println("a record with the same key already " + 
                                  "exists, and will be overwritten:\n"+curData);
                    mustEdit = true;
                    break;
                }
                ++linesToKey;
                line = new HashMap<>();
            }
            
            // edit: copy lines before in a temp file, write your new line, then
            //       copy the lines after, and save temp as the original file
            if (mustEdit) {
                if (mWriters.containsKey(fileName)) {
                    DSVWriter writer = mWriters.get(fileName);
                    
                    reader.reset(false);
                    DSVWriter tempWriter = 
                                new DSVWriter(mDataPath + "/" + fileName 
                                             +".temp", ",", true, true, false);
                    tempWriter.setHeaders(writer.headers());
                    
                    // write previous line
                    StringBuffer prevLine = new StringBuffer();
                    while (--linesToKey >= 0) {
                        reader.getNextLine(prevLine);
                        tempWriter.writeLine(prevLine.toString());
                        prevLine = new StringBuffer();
                    }
                    
                    // write new record
                    reader.getNextLine(prevLine); // discard current record
                    HashMap<String, String> pack = entry.toMap();
                    tempWriter.writeMap(pack);
                    
                    // write the rest of the file
                    StringBuffer followLine = new StringBuffer();
                    while (reader.getNextLine(followLine)) {
                        tempWriter.writeLine(followLine.toString());
                        followLine = new StringBuffer();
                    }
                    
                    // switch files (temp -> original)
                    tempWriter.close();
                    writer.close();
                    mReaders.remove(fileName);
                    mWriters.remove(fileName);
                    switchFiles(mDataPath + "/" + fileName + ".temp", 
                                mDataPath + "/" + fileName + ".csv");
                    if (fileName.equals("menus"))
                        initMenus();
                    else if(fileName.equals("courses"))
                        initCourses();
                    else if(fileName.equals("users"))
                        initUsers();
                    System.out.println("record updated in "+fileName+".csv\n"+
                                        entry);
                    return true;
                } else 
                    return false;
            }
            
        } else
            return false;
        
        // .. if not (new data) append it to the file
        if (mWriters.containsKey(fileName)) {
            DSVWriter writer = mWriters.get(fileName);
            HashMap<String, String> pack = entry.toMap();
            return writer.writeMap(pack);
        }
        return false;
    }
    
    /**
    * @param <menu> a collection of four courses (only the name). It must
    *               contain a reference Date (menu.setDate())
    * 
    * @return true/false corresponding to the success of the operation
    */
    @Override
    public boolean saveMenu(Menu menu) {
        return saveObjet(menu, "menus");
    }

    /**
     * 
     * @param <course> a course with its ingredients
     * @return true/false for the success of the operation
     */
    @Override
    public boolean saveCourse(Course course) {
        return saveObjet(course, "courses");
    }

    /**
     * 
     * @param <user> a user
     * @return true/false corresponding to the success of the operation
     */
    @Override
    public boolean saveUser(CanteenUser user) {
        return saveObjet(user, "users");
    }
    
    
    public static void main(String[] args){
        // this is a module-level test (not to be used)
        
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
                
        Menu menu2 = new Menu();
        menu2.setName("pranzo del venerdì");
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