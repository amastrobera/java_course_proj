package canteen;

import io.Packable;
import java.io.Serializable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Menu implements Packable, Serializable{
    
    private String mName;
    private Date mDate;
    private ArrayList<String> mCourseNames;
    
    public Menu() {
        mName = new String();
        mCourseNames = new ArrayList<>(Arrays.asList("", "", "", ""));
        mDate = null;
    }
    
    public String name() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    
    public String date() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dtString = new String();
        try {
            dtString = sdf.format(mDate);
        } catch (Exception ex){
            return "";
        }
        return dtString;
    }
    
    public boolean setDate(String date) {
        return setDate(date, "yyyy-MM-dd");
    }
    
    public boolean setDate(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try { 
            mDate = sdf.parse(date);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
    
    public ArrayList<String> courses() {
        return mCourseNames;
    }
    
    public String getCourse(Course.Type type) {
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
                return "";
        }
        return mCourseNames.get(idx);
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
                return;
        }
        mCourseNames.set(idx, name);
    }

    @Override
    public String toString() {
        String ret = new String();
        ret += "Date: " + date() + ", Menu: " + mName;
        ret += "\nFirst: " +  mCourseNames.get(0);
        ret += "\nSecond: " + mCourseNames.get(1);
        ret += "\nDessert: " + mCourseNames.get(2);
        ret += "\nFruit: " + mCourseNames.get(3);
        return ret;
    }
    
    @Override
    public void fromMap(HashMap<String, String> args) {
        mName = args.get("Name");
        setDate(args.get("Date"));
        setCourse(args.get("First"), Course.Type.First);
        setCourse(args.get("Second"), Course.Type.Second);
        setCourse(args.get("Dessert"), Course.Type.Dessert);
        setCourse(args.get("Fruit"), Course.Type.Fruit);
    }
    
    @Override
    public HashMap<String, String> toMap(){
         HashMap<String, String> ret = new HashMap<>();
        ret.put("Name", mName);
        ret.put("Date", date());
        ret.put("First", mCourseNames.get(0));
        ret.put("Second", mCourseNames.get(1));
        ret.put("Dessert", mCourseNames.get(2));
        ret.put("Fruit", mCourseNames.get(3));
        return(ret);
    }
    
    @Override 
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Menu)) return false;
        Menu comp = (Menu) obj;
        return comp.date().equals(date());
    }
    
}

