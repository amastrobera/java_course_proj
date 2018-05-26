package canteen;


import java.io.Serializable;
import java.util.LinkedList;

public class Course implements Serializable {
    
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
        return "";
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
        
        return name + " (" + typeToString(type) + ")|" + ings;
    }

    
    @Override 
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Course)) return false;
        Course comp = (Course) obj;
        return comp.name.equals(name); 
                //&& comp.type == type;
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
