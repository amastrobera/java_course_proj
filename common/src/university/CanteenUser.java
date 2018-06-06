package university;

import java.util.HashSet;

public class CanteenUser extends Person  {
    // not "abstract" to allow people other than Student or Professor
    // to eat in the canteen
    protected HashSet<String> mFoodAllergies; 
    protected String mType;

    public CanteenUser(String name, String surname) {
        super(name, surname);
        mFoodAllergies = new HashSet<>();
        mType = "user";
    }

    public CanteenUser() {
        this("", "");
    }
    
    public String type() {
        return mType;
    }
    
    public HashSet<String> allergies() {
        return mFoodAllergies;
    }

    public void setAllergies(HashSet<String> allergies) {
        mFoodAllergies = allergies;
    }
    
    public void addAllergy(String food) {
        mFoodAllergies.add(food);
    }
    
    public boolean isAllergicTo(String food) {
        return mFoodAllergies.contains(food);
    }

    @Override
    public String toString(){
        String allergies = new String();
        for (String a : mFoodAllergies)
            allergies += a + ",";
        if (allergies.length() > 1)
            allergies = allergies.substring(0, allergies.length()-1);
        return mType + "\n" + super.toString() + "\nallergies: " + allergies;
    }

    @Override 
    public boolean equals(Object obj) {
        boolean person =  super.equals(obj);
        if (person)
            return ((CanteenUser)obj).type().equals(mType);
        return false;
    }
    
    
}
