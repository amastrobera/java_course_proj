package server;

import canteen.Course;

import java.util.ArrayList;

public abstract class MealScanner {
    // finds Courses and Ingredients of a Menu
    
    public abstract ArrayList<Course> findMenu(ArrayList<String> menu);
    
}
