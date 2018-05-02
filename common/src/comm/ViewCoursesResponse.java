package comm;

import java.util.ArrayList;
import java.util.HashMap;


public class ViewCoursesResponse extends Response{
    
    private HashMap<String, ArrayList<String>> mMeals;
    
    public ViewCoursesResponse() {
        super("ViewCoures");
        mMeals = new HashMap<>();
    }
   
    public HashMap<String, ArrayList<String>> getCouresLists() {
        return mMeals;
    }
    
    public void setUserList(HashMap<String, ArrayList<String>> meals) {
        mMeals = meals;
    }

    
}
