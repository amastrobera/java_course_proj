package comm;

import canteen.Course;

public class SaveCourseRequest extends Request {
    
    private final Course mCourse;
    
    public SaveCourseRequest(Course course) {
        super("SaveCourse");
        mCourse = course;
    }
    
    public Course getCourse() {
        return mCourse;
    }
    
    @Override
    public String toString(){
        return super.toString() + " >> course: " + mCourse;
    }

}
