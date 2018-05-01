package server;

import canteen.Course;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class MealScanner {
    
    // list all items in the database 
    public abstract HashMap<String, ArrayList<String>> getMeals();
    
    // finds Courses and Ingredients of a Menu    
    public abstract ArrayList<Course> findMenu(ArrayList<String> menu);

    // finds a Course given its name
    public abstract Course findCourse(String name);
    

}
