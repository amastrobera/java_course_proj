package comm;

import java.util.ArrayList;
import java.util.HashMap;


public class ViewMealsResponse extends Response{
    
    private HashMap<String, ArrayList<String>> mMeals;
    
    public ViewMealsResponse() {
        super("ViewMeals");
        mMeals = new HashMap<>();
    }
   
    public HashMap<String, ArrayList<String>> getMealsLists() {
        return mMeals;
    }
    
    public void setUserList(HashMap<String, ArrayList<String>> meals) {
        mMeals = meals;
    }

    
}
