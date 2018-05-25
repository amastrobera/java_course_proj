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
    private SerialReader<CanteenUser> mUserReader;
    private SerialWriter<CanteenUser> mUserWriter;
    private SerialReader<Course> mCourseReader;
    private SerialWriter<Course> mCourseWriter;
    private SerialReader<Menu> mMenuReader;
    private SerialWriter<Menu> mMenuWriter;
    
    public FileDataManager(String dataPath) {
        mDataPath = dataPath;       
        initUsers();
        initCourses();
        initMenus();
    }

    private void initUsers() {
        mUserReader = new SerialReader<>(mDataPath + "/users.dat");
        mUserWriter = new SerialWriter<>(mDataPath + "/users.dat");
        
    }
    
    private void initCourses() {
        mCourseReader = new SerialReader(mDataPath + "/courses.dat");
        mCourseWriter = new SerialWriter(mDataPath + "/courses.dat");
    }
    
    private void initMenus(){
        mMenuReader = new SerialReader(mDataPath + "/menus.dat");
        mMenuWriter = new SerialWriter(mDataPath + "/menus.dat");
    }
        
    private void switchFiles(String newFilePath, String oldFilePath) {    
        File newFile = new File(newFilePath);
        newFile.renameTo(new File(oldFilePath));
    }

    /** 
     * @return Array of all courses in our databases
     */    
    @Override
    public HashMap<String, ArrayList<String>> getCourses() {
        
        HashMap<String, ArrayList<String>> ret = new HashMap<>();
        ArrayList<String> first = new ArrayList<>();
        ArrayList<String> second = new ArrayList<>();
        ArrayList<String> dessert = new ArrayList<>();
        ArrayList<String> fruit = new ArrayList<>();

        Course course;
        mCourseReader.reset();
        while ((course = mCourseReader.getNextLine()) != null) {
            switch (course.type) {
                case First: 
                    first.add(course.name);
                    break;
                case Second: 
                    second.add(course.name);
                    break;
                case Dessert: 
                    dessert.add(course.name);
                    break;
                case Fruit: 
                    fruit.add(course.name);
                    break;
            }
        }
        ret.put("First", first);
        ret.put("Second", second);
        ret.put("Dessert", dessert);
        ret.put("Fruit", fruit);
        return(ret);
    }
    
    /**
     * 
     * @param name of the course
     * @return Course class corresponding to that name, including ingredients
     */
    @Override
    public Course findCourse(String name) {
        Course toFind = new Course();
        toFind.name = name;
        Course found =  mCourseReader.findObj(toFind);
        return found;
    }
    
    
    /**
     * 
     * @param menu array with the name of the four course menu
     * @return Array of the corresponding Course class (including ingredients)
     */
    @Override
    public ArrayList<Course> findMenu(ArrayList<String> menu){
        
        ArrayList<Course> ret = new ArrayList<>();
        mCourseReader.reset();
        Course course;
        while ((course = mCourseReader.getNextLine()) != null) {
            for (String name : menu) {
                if (course.name.equals(name)){
                    ret.add(course);
                    break;
                }
            }
        }
        return(ret);
    }
        
    /**
    * @return Array of users in our database or file-system
    */
    @Override
    public ArrayList<CanteenUser> getUsers() {
        ArrayList<CanteenUser> ret = new ArrayList<>();
        CanteenUser user;
        mUserReader.reset(); // file pointer back to the top
        while ((user = mUserReader.getNextLine()) != null ) 
            ret.add(user);
        return ret;
    }

    /**
    * @param menu a collection of four courses (only the name)
    * 
    * @return Array of users that are allergic to any ingredient of any of the 
    * four courses of the menu
    */
    @Override
    public ArrayList<CanteenUser> getAllergicUsers(Menu menu) {
        
        ArrayList<CanteenUser> ret = new ArrayList<>();
        ArrayList<Course> courses = findMenu(menu.courses());        
        
        if (courses.size() > 0 ) { 
            Iterator<Course> cit;
            
            CanteenUser user;
            mUserReader.reset();
            while ((user = mUserReader.getNextLine()) != null ) {
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
        }
        return ret;
    }
    
    /**
     * 
     * @param type "student", "professor", or "" for all users
     * @return count of users in the database
     */
    @Override
    public long getNumberOfUsers(String type) {
        long num = 0;
        CanteenUser user;
        mUserReader.reset();
        while ((user = mUserReader.getNextLine()) != null) {
            if (type.isEmpty())
                ++num;
            else
                if (type.equals(user.type()))
                    ++num;
        }
        return num;
    }
    
    private <T extends Object> boolean saveObjet(String filePath, 
                                                 SerialReader<T> reader, 
                                                 SerialWriter<T> writer,
                                                 T targetToSave) {
        boolean ret = false;
        long lineOriginal = reader.find(targetToSave);
        
        if (lineOriginal >= 0) {
            // 1. if the object exists, replace it with the new one
            try {
                SerialWriter<T> tmpWriter = new SerialWriter<>(filePath+".tmp");
                long cur = 0;
                // copy beginning file to the tmp
                reader.reset();
                while (cur < lineOriginal) {
                    tmpWriter.writeNextLine(reader.getNextLine());
                    ++cur;
                }
                reader.getNextLine(); // discard line to be replaced
                tmpWriter.writeNextLine(targetToSave); // add the new line

                // copy ending file to the tmp                
                T obj;
                while ((obj = reader.getNextLine()) != null)
                    tmpWriter.writeNextLine(obj);
                tmpWriter.close();
                reader.close();

                // move tmp file to original
                switchFiles(filePath+".tmp", filePath);

                // verify success
                //long lineReplaced = reader.find(targetToSave);
                //reader.close();
                //if (lineReplaced >= 0) {
                //    if (lineReplaced == lineOriginal)
                //        ret = true;
                //} else {
                //    System.err.println("could not replace with obj " + 
                //                        targetToSave);
                //    ret = false;
                //}
                ret = true;
            } catch (Exception ex) {
                System.err.println("failed to write to " + filePath + ", " + ex);
                ret = false;
            }
            
        } else {
            // 2. if the not, append it to the file 
            ret = writer.writeNextLine(targetToSave);
        }
        return ret;
    }
    
    /**
    * @param menu a collection of four courses (only the name). It must
    *               contain a reference Date (menu.setDate())
    * 
    * @return true/false corresponding to the success of the operation
    */
    @Override
    public boolean saveMenu(Menu menu) {
        return saveObjet(mDataPath + "/menus.dat", mMenuReader, 
                         mMenuWriter, menu);
    }

    /**
     * 
     * @param course a course with its ingredients
     * @return true/false for the success of the operation
     */
    @Override
    public boolean saveCourse(Course course) {
        return saveObjet(mDataPath + "/courses.dat", mCourseReader, 
                         mCourseWriter, course);
    }

    /**
     * 
     * @param user a CanteenUser (Professor or Student or generic)
     * @return true/false corresponding to the success of the operation
     */
    @Override
    public boolean saveUser(CanteenUser user) {
        return saveObjet(mDataPath + "/users.dat", mUserReader, 
                 mUserWriter, user);
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