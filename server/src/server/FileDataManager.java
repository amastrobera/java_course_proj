package server;

import io.*;
import canteen.*;
import university.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
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
        // writer first: the file must exist (or be created) before reader init
        mUserWriter = new SerialWriter<>(mDataPath + "/users.dat");
        mUserReader = new SerialReader<>(mDataPath + "/users.dat");
    }
    
    private void initCourses() {
        // writer first: the file must exist (or be created) before reader init
        mCourseWriter = new SerialWriter(mDataPath + "/courses.dat");
        mCourseReader = new SerialReader(mDataPath + "/courses.dat");
    }
    
    private void initMenus(){
        // writer first: the file must exist (or be created) before reader init
        mMenuWriter = new SerialWriter(mDataPath + "/menus.dat");
        mMenuReader = new SerialReader(mDataPath + "/menus.dat");
    }
        
    private void switchFiles(String newFilePath, String oldFilePath) {    
        File newFile = new File(newFilePath);
        newFile.renameTo(new File(oldFilePath));
    }

    
    @Override
    public boolean isReady() {
        boolean opened = true;
        
        File u = new File(mDataPath + "/users.dat");
        opened &= u.exists();
        
        File c = new File(mDataPath + "/courses.dat");
        opened &= c.exists();
        
        File m = new File(mDataPath + "/menus.dat");
        opened &= m.exists();   
        
        return opened;
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
     * @return Set of Menus ordered by Date
     */    
    @Override
    public HashSet<Menu> getMenus() {
        HashSet<Menu> ret = new HashSet<>();
        Menu menu;
        mMenuReader.reset();
        while ((menu = mMenuReader.getNextLine()) != null) 
            ret.add(menu);
        return ret;
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
        if (user.type().equals("student"))
            return saveObjet(mDataPath + "/users.dat", mUserReader, 
                     mUserWriter, (Student)user);
        else if (user.type().equals("professor"))
            return saveObjet(mDataPath + "/users.dat", mUserReader, 
                     mUserWriter, (Professor)user);
        else
            return saveObjet(mDataPath + "/users.dat", mUserReader, 
                     mUserWriter, user);
    }
       
}