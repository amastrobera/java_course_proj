package comm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


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

    public boolean isEmpty() {
        boolean empty = true;
        Iterator<String> it = mMeals.keySet().iterator();
        while (it.hasNext()) {
            if (!mMeals.get(it.next()).isEmpty()) {
                empty = false;
                break;
            }
        }
        return empty;
    }
    
}
