package canteen;

import io.Packable;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;

public class Menu implements Packable{
    
    private String mName;
    private ArrayList<String> mCourseNames;
    
    public Menu(String name) {
        mName = name;
        mCourseNames = new ArrayList<>(Arrays.asList("","","",""));
    }

    public String name() {
        return mName;
    }
    
    public ArrayList<String> courses() {
        return mCourseNames;
    }
    
    public void setCourse(String name, Course.Type type) {
        int idx;
        switch(type){
            case First:
                idx = 0;
                break;
            case Second:
                idx = 1;
                break;
            case Dessert:
                idx = 2;
                break;
            case Fruit:
                idx = 3;
                break;
            default: 
                idx = -1;
                break;
        }
        if (idx >= 0)
            mCourseNames.add(idx, name);
    }

    @Override
    public String toString() {
        String ret = new String();
        ret += "Menu: " + mName;
        ret += "\nFirst: " +  mCourseNames.get(0);
        ret += "\nSecond: " + mCourseNames.get(1);
        ret += "\nDessert: " + mCourseNames.get(2);
        ret += "\nFruit: " + mCourseNames.get(3);
        return ret;
    }
    
    @Override
    public void fromMap(HashMap<String, String> args) {
        mName = args.get("Name");
        setCourse(args.get("First"), Course.Type.First);
        setCourse(args.get("Second"), Course.Type.Second);
        setCourse(args.get("Dessert"), Course.Type.Dessert);
        setCourse(args.get("Fruit"), Course.Type.Fruit);
    }
    
    @Override
    public HashMap<String, String> toMap(){
         HashMap<String, String> ret = new HashMap<>();
        ret.put("Name", mName);
        ret.put("First", mCourseNames.get(0));
        ret.put("Second", mCourseNames.get(1));
        ret.put("Dessert", mCourseNames.get(2));
        ret.put("Fruit", mCourseNames.get(3));
        return(ret);
    }
    
}

