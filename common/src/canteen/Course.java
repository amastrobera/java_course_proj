package canteen;

import io.Packable;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

public class Course implements Packable, Serializable {
    
    public static enum Type {First, Second, Dessert, Fruit, Unknown};
    
    public static String typeToString(Type type) {
        switch(type){
            case First:
                return "primo";
            case Second:
                return "secondo";
            case Dessert:
                return "dolce";
            case Fruit:
                return "frutta";
        }
        return "Unknown";
    }
    
    public static Type strToType(String type) {
        String t = type.toLowerCase();
        if (t.equals("primo"))
            return Type.First;
        if (t.equals("secondo"))
            return Type.Second;
        if (t.equals("dolce"))
            return Type.Dessert;
        if (t.equals("frutta"))
            return Type.Fruit;
        return Type.Unknown;
    }
    
    public String name;
    public Type type;
    public LinkedList<String> ingredients;
    
    public Course() {
        name = new String();
        type = Type.Unknown;
        ingredients = new LinkedList<>();
    }
        
    @Override
    public String toString() {
        String ings = new String();
        for (String i : ingredients) 
            ings += i + ", ";
        if (ings.length() > 2)
            ings = ings.substring(0, ings.length()-2);
        
        return "name: " + name + "(" + typeToString(type) + ")" +  
                "\ningredients:  " + ings;
    }

    
    @Override
    public void fromMap(HashMap<String, String> args) {
        // expects this list: [name, type, ingredient1, ingredient2, ...]
        name = args.get("Name");
        type = strToType(args.get("Type"));
        ingredients = new LinkedList<>();
        String mapIngredients = args.get("Ingredients");
        for (String ingr : mapIngredients.split(","))
            ingredients.add(ingr);
    }
    
    @Override
    public HashMap<String, String> toMap(){
        HashMap<String, String> ret = new HashMap<>();
        ret.put("Name", name);
        ret.put("Type", typeToString(type));
        String mapIngredients = new String();
        for (String ingr : ingredients)
            mapIngredients += ingr + ",";
        if (mapIngredients.length() > 0) 
            mapIngredients = mapIngredients.substring(0, mapIngredients.length()-1);
        ret.put("Ingredients", mapIngredients);
        return(ret);
    }

    @Override 
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Course)) return false;
        Course comp = (Course) obj;
        return comp.type == type && comp.name.equals(name);
    }

    
    public static void main(String[] args){
        
        Course c = new Course();
        c.name = "pasta al pomodoro";
        c.type = Course.Type.First;
        c.ingredients.add("pasta");
        c.ingredients.add("pomodoro");
        
        System.out.println(c);
    }
    
}
