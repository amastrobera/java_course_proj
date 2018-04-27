package server;

import canteen.Course;
import io.DSVReader;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

public class FileMealScanner extends MealScanner {

    private final DSVReader mReader;
    
    public FileMealScanner(){
        mReader = new DSVReader("data/meals.csv", ",", true, true);
    }
    
    @Override
    public ArrayList<Course> findMenu(ArrayList<String> menu){
        
        ArrayList<Course> ret = new ArrayList<>();
        HashMap<String,String> line = new HashMap<>();
        while (ret.size() < 4 && mReader.getNextLine(line)) {
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
    
    public static void main(String[] args){

        System.out.println("--- test MealScaner ---");
        MealScanner scanner = new FileMealScanner();
        
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
    }
    
}