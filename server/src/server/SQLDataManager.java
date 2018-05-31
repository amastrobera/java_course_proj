package server;

import canteen.*;
import university.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;


public class SQLDataManager extends DataManager {

    public SQLDataManager(String dataPath) {

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
//        while ((course = mCourseReader.getNextLine()) != null) {
//            switch (course.type) {
//                case First: 
//                    first.add(course.name);
//                    break;
//                case Second: 
//                    second.add(course.name);
//                    break;
//                case Dessert: 
//                    dessert.add(course.name);
//                    break;
//                case Fruit: 
//                    fruit.add(course.name);
//                    break;
//            }
//        }
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
        Course found =  new Course();//mCourseReader.findObj(toFind);
        return found;
    }
    

    /** 
     * @return Set of Menus ordered by Date
     */    
    @Override
    public HashSet<Menu> getMenus() {
        HashSet<Menu> ret = new HashSet<>();
        Menu menu;
//        while ((menu = mMenuReader.getNextLine()) != null) 
//            ret.add(menu);
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
        Course course;
//        while ((course = mCourseReader.getNextLine()) != null) {
//            for (String name : menu) {
//                if (course.name.equals(name)){
//                    ret.add(course);
//                    break;
//                }
//            }
//        }
        return(ret);
    }
        
    /**
    * @return Array of users in our database or file-system
    */
    @Override
    public ArrayList<CanteenUser> getUsers() {
        ArrayList<CanteenUser> ret = new ArrayList<>();
        CanteenUser user;
//        while ((user = mUserReader.getNextLine()) != null ) 
//            ret.add(user);
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
//            while ((user = mUserReader.getNextLine()) != null ) {
//                HashSet<String> allergicCourses = new HashSet<>();
//                cit = courses.iterator();
//                while (cit.hasNext()) {
//                    Course course = cit.next();
//                    for (String ingredient : course.ingredients) {
//                        if (user.isAllergicTo(ingredient)) {
//                            allergicCourses.add(course.name);
//                            break;
//                        }
//                    }
//                }
//                if (!allergicCourses.isEmpty()) {
//                    user.setAllergies(allergicCourses);
//                    ret.add(user);
//                }
//            }
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
//        while ((user = mUserReader.getNextLine()) != null) {
//            if (type.isEmpty())
//                ++num;
//            else
//                if (type.equals(user.type()))
//                    ++num;
//        }
        return num;
    }
        
    /**
    * @param menu a collection of four courses (only the name). It must
    *               contain a reference Date (menu.setDate())
    * 
    * @return true/false corresponding to the success of the operation
    */
    @Override
    public boolean saveMenu(Menu menu) {
        return false;
    }

    /**
     * 
     * @param course a course with its ingredients
     * @return true/false for the success of the operation
     */
    @Override
    public boolean saveCourse(Course course) {
        return false;
    }

    /**
     * 
     * @param user a CanteenUser (Professor or Student or generic)
     * @return true/false corresponding to the success of the operation
     */
    @Override
    public boolean saveUser(CanteenUser user) {
        return false;
    }
    
    
    public static void main(String[] args){
        // this is a module-level test (not to be used)
        
        
    }


}