package comm;

import canteen.Course;

public class FindMealResponse extends Response {
    
    private Course mFound;
    
    public FindMealResponse() {
        super("FindMeal");
        mFound = new Course();
    }
   
    public Course getCouse() {
        return mFound;
    }
    
    public void setCourse(Course course) {
        mFound = course;
    }
    
}
