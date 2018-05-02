package comm;

import canteen.Course;

public class ViewCourseInfoResponse extends Response {
    
    private Course mFound;
    
    public ViewCourseInfoResponse() {
        super("ViewCourseInfo");
        mFound = new Course();
    }
   
    public Course getCouse() {
        return mFound;
    }
    
    public void setCourse(Course course) {
        mFound = course;
    }
    
}
