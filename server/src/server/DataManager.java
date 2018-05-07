package server;

import university.*;
import canteen.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Base class for accessing the data. It was built to extensible by using 
 * either a File-System or a Data Base.
 */
public abstract class DataManager {
    /**
    * @param <none> 
    * 
    * @return Array of users in our database or file-system
    */
    public abstract ArrayList<CanteenUser> getUsers();
    
    /**
    * @param <menu> a collection of four courses (only the name)
    * 
    * @return Array of users that are allergic to any ingredient of any of the 
    * four courses of the menu
    */
    public abstract ArrayList<CanteenUser> getAllergicUsers(Menu menu);

    /**
     * 
     * @param <type> "student", "professor", or "" for all users
     * @return count of users in the database
     */
    public abstract long getNumberOfUsers(String type);
    
    /** 
     * @param <none>
     * @return Array of all courses in our databases
     */
    public abstract HashMap<String, ArrayList<String>> getCourses();
    
    /**
     * 
     * @param <menu> array with the name of the four course menu
     * @return Array of the corresponding Course class (including ingredients)
     */
    public abstract ArrayList<Course> findMenu(ArrayList<String> menu);

    /**
     * 
     * @param <name> of the course
     * @return Course class corresponding to that name, including ingredients
     */
    public abstract Course findCourse(String name);

    /**
    * @param <menu> a collection of four courses (only the name). It must
    *               contain a reference Date (menu.setDate())
    * 
    * @return true/false corresponding to the success of the operation
    */
    public abstract boolean saveMenu(Menu menu);
    
    
    /**
     * 
     * @param <course> a course with its ingredients
     * @return true/false for the success of the operation
     */
    public abstract boolean saveCourse(Course course);

    /**
     * 
     * @param <user> a user
     * @return true/false corresponding to the success of the operation
     */
    public abstract boolean saveUser(CanteenUser user);
    
}
