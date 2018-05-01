package server;

import canteen.Course;
import io.DSVReader;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

public class FileMealScanner extends MealScanner {

    private final String mDataPath;
    private final DSVReader mReader;
    
    public FileMealScanner(String dataPath){
        mDataPath = dataPath;
        mReader = new DSVReader(mDataPath + "/meals.csv", ",", true, true);
    }
    
    @Override
    public ArrayList<Course> findMenu(ArrayList<String> menu){        
        ArrayList<Course> ret = new ArrayList<>();
        HashMap<String,String> line = new HashMap<>();
        while (mReader.getNextLine(line)) {
            if (line.size() > 0) {
                Course c = new Course();
                c.fromMap(line);
                for (String course : menu) {
                    if (c.name.equals(course)){
                        ret.add(c);
                        break;
                    }
                }
                line = new HashMap<>();
            }
        }
        mReader.reset(); // the file pointer goes back to the top
        return(ret);
    }

    @Override
    public HashMap<String, ArrayList<String>> getMeals() {
        HashMap<String, ArrayList<String>> ret = new HashMap<>();
        ArrayList<String> first = new ArrayList<>();
        ArrayList<String> second = new ArrayList<>();
        ArrayList<String> dessert = new ArrayList<>();
        ArrayList<String> fruit = new ArrayList<>();
        HashMap<String,String> line = new HashMap<>();
        while (mReader.getNextLine(line)) {
            if (line.size() > 0) {
                Course c = new Course();
                c.fromMap(line);
                switch (c.type) {
                    case First: 
                        first.add(c.name);
                        break;
                    case Second: 
                        second.add(c.name);
                        break;
                    case Dessert: 
                        dessert.add(c.name);
                        break;
                    case Fruit: 
                        fruit.add(c.name);
                        break;
                }
                line = new HashMap<>();
            }
        }
        mReader.reset(); // the file pointer goes back to the top
        
        ret.put("First", first);
        ret.put("Second", second);
        ret.put("Dessert", dessert);
        ret.put("Fruit", fruit);
        return(ret);
    }
    
    @Override
    public Course findCourse(String name) {
        HashMap<String,String> line = new HashMap<>();
        while (mReader.getNextLine(line)) {
            if (line.size() > 0) {
                Course c = new Course();
                c.fromMap(line);
                if (c.name.equals(name)) {
                    mReader.reset();
                    return c;
                }
                line = new HashMap<>();
            }
        }
        mReader.reset(); // the file pointer goes back to the top
        return new Course();
    }
    
    
    public static void main(String[] args){

        System.out.println("--- test MealScaner ---");
        MealScanner scanner = new FileMealScanner("../data");
        
        ArrayList<String> menu = new ArrayList<>(
                  Arrays.asList("Riso alla zucca", 
                                "Arrosto di tacchino al forno",
                                "Budino al cioccolato",
                                "Macedonia di frutta"));
        
        System.out.println(">> searching for menu: " + menu);
        ArrayList<Course> found = scanner.findMenu(menu);
        System.out.println("   found " + found.size() + " courses");
        found.forEach((s)-> {System.out.println(s);});
        
        
        menu = new ArrayList<>(
                  Arrays.asList("Pizza margherita", 
                                "Tonno sott’olio",
                                "Strudel",
                                "Smoothie di frutta mista"));
        System.out.println(">> searching for menu: " + menu);
        found = scanner.findMenu(menu);
        System.out.println("   found " + found.size() + " courses");
        found.forEach((s)-> {System.out.println(s);});
        
        
        System.out.println(">> searching all meals ");
        HashMap<String, ArrayList<String>> meals = scanner.getMeals();
        System.out.println("   == First ==   ");
        meals.get("First").forEach((s)-> {System.out.println(s);});
        System.out.println("   == Second ==   ");
        meals.get("Second").forEach((s)-> {System.out.println(s);});        
        System.out.println("   == Dessert ==   ");
        meals.get("Dessert").forEach((s)-> {System.out.println(s);});
        System.out.println("   == Fruit ==   ");
        meals.get("Fruit").forEach((s)-> {System.out.println(s);});
    }
    
}