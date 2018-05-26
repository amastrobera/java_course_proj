package canteen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Menu implements Serializable, Comparable {
    
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

    // in order to guarantee that "only one" between desset and fruit
    // is available, setCourse() shoud delete the fruit (if present) when
    // the dessert is set (and viceversa)    
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
        
        // guarantee there is only a dessert or fruit
        if (!name.isEmpty() && 
            (type == Course.Type.Dessert || type == Course.Type.Fruit)) {
            if (type == Course.Type.Dessert && !mCourseNames.get(3).isEmpty())
                mCourseNames.set(3, "");
            else if (type == Course.Type.Fruit && !mCourseNames.get(2).isEmpty())
                mCourseNames.set(2, "");
        }
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
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Menu)) return false;
        Menu comp = (Menu) obj;
        return comp.date().equals(date());
    }

    @Override 
    public int compareTo(Object obj) {
        if (obj == this) return 0;
        if (!(obj instanceof Menu)) return -1;
        Menu comp = (Menu) obj;
        String dtComp = comp.date();
        return comp.date().compareTo(date());
    }
    
}

