package university;

import java.util.HashMap;
import java.util.HashSet;

public class CanteenUser extends Person  {
    // not "abstract" to allow people other than Student or Professor
    // to eat in the canteen
    
    protected final HashSet<String> mFoodAllergies; 
    protected String mType;

    public CanteenUser(String name, String surname) {
        super(name, surname);
        mFoodAllergies = new HashSet<>();
        mType = "User";
    }

    public CanteenUser() {
        this("", "");
    }
    
    public String type() {
        return mType;
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
        return super.toString() + "\nallergies: " + allergies;
    }

    @Override
    public void fromMap(HashMap<String, String> map) {
        super.fromMap(map);
        String[] mapAllergies = map.get("Allergies").split(",");
        if (mapAllergies.length > 0 && !mapAllergies[0].isEmpty())
            for (String allergy : mapAllergies)
                mFoodAllergies.add(allergy);
    }
    
    @Override
    public HashMap<String, String> toMap(){
        // prepares this list: [name surname, allergy1, allergy2, ...]
        HashMap<String, String> ret = super.toMap();
        ret.put("Type", mType);
        String allergies = new String();
        for (String allergy : mFoodAllergies)
            allergies += allergy + ",";
        if (allergies.length() > 0)
            allergies = allergies.substring(0, allergies.length()-1);
        ret.put("Allergies", allergies);
        return(ret);
    }

    
}
