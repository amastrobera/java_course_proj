package server;

import university.*;
import canteen.Menu;
import canteen.Course;
import io.DSVReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class FileUserScanner extends UserScanner{
    
    private final String mDataPath;
    private final DSVReader mReader;
    
    public FileUserScanner(String dataPath) {
        mDataPath = dataPath;
        mReader = new DSVReader(mDataPath + "/people.csv", ",", true, true);
    }
    
    @Override
    public ArrayList<CanteenUser> getUsers() {
        ArrayList<CanteenUser> ret = new ArrayList<>();
                
        HashMap<String,String> line = new HashMap<>();
        
        while (mReader.getNextLine(line)) {
            if (line.size() > 0) {
                // factory pattern here 
                CanteenUser user;
                String type = line.get("Type");
                if (type.equals("student"))
                    user = new Student();
                else if (type.equals("professor"))
                    user = new Professor();
                else
                    user = new CanteenUser();
                user.fromMap(line);
                ret.add(user);
            }
            line = new HashMap<>();
        }
        
        mReader.reset(); // file pointer back to the top
        
        return ret;
    }

    @Override
    public ArrayList<CanteenUser> getAllergicUsers(Menu menu) {
        
        // get all users, remove the non allergic, return the rest
        
        ArrayList<CanteenUser> users = getUsers();
        
        MealScanner meals = new FileMealScanner(mDataPath);
        
        ArrayList<Course> courses = meals.findMenu(menu.courses());
        
        int allergies;
        Iterator<CanteenUser> it = users.iterator();
        while (it.hasNext()) {
            CanteenUser user = it.next();
            
            allergies = 0;
            for (Course course : courses) {
                for (String food : course.ingredients) {
                    if (user.isAllergicTo(food)) {
                        allergies += 1;
                        break;
                    }
                }
                if (allergies > 0) break;
            }
            if (allergies == 0) 
                it.remove();
        }
        
        return users;
    }
    
    public static void main(String[] args) {
        
        FileUserScanner scanner = new FileUserScanner("../data");
        
        System.out.println("--- scanning data file ---");
        ArrayList<CanteenUser> users = scanner.getUsers();
        System.out.println("    > found " + users.size() + " users\n");
//        for (CanteenUser user : users)
//            System.out.println(user);
        
        System.out.println("--- scanning users allergic to this menu ---");
        Menu menu = new Menu("pranzo del venerdì");
        menu.setCourse("Orzotto con zucchine e asiago", Course.Type.First);
        menu.setCourse("Bocconcini di Vitellone in umido", Course.Type.Second);
        menu.setCourse("Budino al cioccolato", Course.Type.Dessert);
        menu.setCourse("Smoothie di frutta mista", Course.Type.Fruit);
        System.out.println(menu);
        
        users = scanner.getAllergicUsers(menu);
        System.out.println("    > found " + users.size() + " users\n");
//        for (CanteenUser user : users)
//            System.out.println(user);
  
    }
    
}