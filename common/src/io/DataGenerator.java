package io;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.File;

import java.util.Random;
import java.util.Arrays;

import java.util.LinkedList;
import java.util.HashSet;

import university.*;
import canteen.*;

public class DataGenerator {

    // platform independent flat file generator with data
    
    private static Person[] generateParents(String surname, Random seed) {
        String[] father = {"Pietro", "Giovanni", "Mario", "Davide", "Claudio"};
        String[] mother = {"Maria", "Carmela", "Cristina", "Franca", "Paola"};
        String[] maiden = {"Lombardi", "Barbieri", "Moretti", "Fontana", "Conti"};
        
        int numParents = seed.nextInt(100);
        Person[] parents = new Person[2];
        
        if (numParents < 1) { // orphan (1%) 
            parents[0] = null;
            parents[1] = null;
        } else if (numParents < 4) {  // one parent only  (4%) 
            if (seed.nextBoolean()) { // only the father
                parents[0] = new Person(father[seed.nextInt(father.length)],
                                        surname);
                parents[0].setPhone(generatePhone(seed));
            } else {                  // only the mother
                parents[1] = new Person(mother[seed.nextInt(mother.length)],
                                        maiden[seed.nextInt(maiden.length)]);
                parents[1].setPhone(generatePhone(seed));
            }
        } else {
            parents[0] = new Person(father[seed.nextInt(father.length)],
                                    surname);
            parents[0].setPhone(generatePhone(seed));
            
            parents[1] = new Person(mother[seed.nextInt(mother.length)],
                                    maiden[seed.nextInt(maiden.length)]);
            parents[1].setPhone(generatePhone(seed));
        }
        
        return parents;
    }


    private static String generatePhone(Random seed) {
        String phone = new String();
        String[] prefix = {"333", "338", "339", "347", "349", "391", "390"};
        phone += prefix[seed.nextInt(prefix.length)] + ".";
        phone += seed.nextInt(99) + ".";
        phone += seed.nextInt(99) + ".";
        phone += seed.nextInt(999);
        return phone;
    }


    
    private static Address generateAddress(Random seed) {
        String[] streets = {"via Gramsci", "via Cavour", 
                        "largo Augusto", "via Ripamonti", "corso Como"};
        String[] postcode = {"20100", "10121", "16121", "15121", "31100"};
        String[] cities = {"Milano", "Torino", "Genova", "Alessandria", 
                        "Treviso"};
        int c = seed.nextInt(cities.length);
        Address address = new Address(
                        streets[seed.nextInt(streets.length)] + " " + 
                        (seed.nextInt(99)+1), 
                        postcode[c],
                        cities[c]);
        return address;
    }
    
    private static HashSet<String> generateAllergies(Random seed){
        String[] ingredients = {"cipolla", "pomodoro", "latte", "maiale", 
                                "olio di semi di girasole", "mozzarella", 
                                 "burro"};
        HashSet<String> allergies = new HashSet<>();
        int num = seed.nextInt(3);
        for (int i = 0 ; i < num; ++i)
            allergies.add(ingredients[seed.nextInt(ingredients.length)]);
        return allergies;
    }


    public static void readMenuPlan(String filePath, boolean printAll) {
        
        SerialReader<Menu> reader = new SerialReader<>(filePath);
        LinkedList<Menu> menus = new LinkedList<>();
        
        Menu menu;
        while ((menu = reader.getNextLine()) != null)
            menus.add(menu);

        System.out.println("found " + menus.size() + " menus in " + filePath);
        if (printAll) 
            for (Menu m : menus)
                System.out.println(m);
    }
    
    
    public static void generateMenus(String filePath) {
        
        LinkedList<Menu> menuPlan = new LinkedList<>();

        Menu m1 = new Menu();
        m1.setName("venerdì 9 marzo 2018");
        m1.setDate("2018-03-09");
        m1.setCourse("Crema di patate e porri", Course.Type.First);
        m1.setCourse("Arrosto di tacchino al forno", Course.Type.Second);
        m1.setCourse("Crostatina alla fragola", Course.Type.Dessert); // will be removed
        m1.setCourse("Macedonia di frutta", Course.Type.Fruit);
        menuPlan.add(m1);

        Menu m2 = new Menu();
        m2.setName("lunedì 12 marzo 2018");
        m2.setDate("2018-03-12");
        m2.setCourse("Riso alla zucca", Course.Type.First);
        m2.setCourse("Bastoncini fil. merluzzo", Course.Type.Second);
        m2.setCourse("Crostatina alla fragola", Course.Type.Dessert);
        menuPlan.add(m2);
        
        int done = 0;
        try {
            FileOutputStream file = new FileOutputStream(filePath);
            ObjectOutputStream writer = new ObjectOutputStream(file);
            for (Menu m : menuPlan) {
                writer.writeObject(m);
                ++done;
            }
            writer.close();
            file.close();
            System.out.println("--- generated " + done + " lines into " + 
                                filePath + " ---");
        } catch (Exception ex) {
            System.err.println("failed to write to " + filePath + ", " + ex);
        }
    }
    

    public static void readMeals(String filePath, boolean printAll) {
        
        SerialReader<Course> reader = new SerialReader<>(filePath);
        LinkedList<Course> courses = new LinkedList<>();
        
        Course course;
        while ((course = reader.getNextLine()) != null)
            courses.add(course);

        System.out.println("found " + courses.size() + 
                            " courses in " + filePath);
        if (printAll) 
            for (Course c : courses)
                System.out.println(c);
    }

    
    private static String[][] generateCourses() { 
        
        String[][] meals = {
            {"Crema di fagioli con pasta","primo","pasta,fagioli borlotti,carota,cipolla"},
            {"Crema di patate e porri","primo","patate,porro,aglio,sale"},
            {"Crema di piselli con riso","primo","piselli,patate,riso,olio d'oliva,olio di semi di girasole,sale"},
            {"Gnocchi al pomodoro","primo","gnocchi di patate,pomodoro,carota,cipolla,olio d’oliva,sale,basilico"},
            {"Pasta ai broccoli","primo","pasta,broccoli,latte,farina,sale"},
            {"Orzotto con zucchine e asiago","primo","orzo,zucchine,latte,asiago,olio di semi di girasole,farina,sale"},
            {"Pasta al ragù","primo","pasta,pomodoro,manzo,pollo,carota,cipolla,sedano,olio d'oliva,sale"},
            {"Pizza margherita","primo","farina,acqua,olio di semi di girasole,lievito,sale,zucchero,pomodoro,mozzarella,olio d'oliva,origano,sale"},
            {"Riso alla milanese","primo","riso,olio d'oliva,burro,zafferano,sale"},
            {"Riso alla zucca","primo","riso,zucca,latte,farina,olio d'oliva,sale"},
            {"Riso alle zucchine","primo","riso,zucchine,latte,farina,olio d'oliva,sale"},
            {"Arrosto di tacchino al forno","secondo","tacchino,carota,sedano,cipolla,olio di semi di girasole,patate,sale"},
            {"Bastoncini fil. merluzzo","secondo","merluzzo,farina,olio di semi di girasole,grano,mais,lievito,sale,zucchero,spezie,sali minerali"},
            {"Bocconcini di Vitellone in umido","secondo","vitello,pomodoro,carote,sedano,cipolla,olio d'oliva,sale,patate,erbe aromatiche"},
            {"Fettina di maiale alla piastra","secondo","maiale,olio d'oliva,sale,pepe"},
            {"Fettina di tacchino alla piastra","secondo","fesa di tacchino,olio di semi di girasole,olio d'oliva,sale"},
            {"Filetto di merluzzo alla pizzaiola","secondo","merluzzo,pomodoro,olio di semi di girasole,olio d'oliva,erbe aromatiche,sale"},
            {"Petto di pollo alla piastra","secondo","pollo,olio d'oliva,sale,pepe"},
            {"Sovracosce di pollo al forno","secondo","pollo,olio di semi di girasole,erbe aromatiche"},
            {"Tonno sott’olio","secondo","tonno"},
            {"Formaggio caciotta","secondo","latte,fermenti,caglio,sale"},
            {"Yogurt","dolce","latte"},
            {"Brownies fatti in casa","dolce","cacao,burro,cioccolato,farina,lievito"},
            {"Crostatina alla fragola","dolce","farina,burro,zucchero,uova,limone,latte,pana,vaniglia,fragole"},
            {"Strudel","dolce","limone,cannella,uvetta,mela,rum,pangrattato,pinoli,olio di semi di girasole "},
            {"Budino al cioccolato","dolce","cacao,farina,burro,latte,vaniglia,rum"},
            {"Macedonia di frutta","frutta","banana,mela,arancia,uva bianca"},
            {"Mela","frutta","mela"},
            {"Banana","frutta","banana"},
            {"Pera","frutta","pera"},
            {"Smoothie di frutta mista","frutta","arancia,sedano,cetriolo,menta,zucchero di canna"},
        };
        return meals;
    }
    
    public static void generateMeals(String filePath) {
                
        String[][] meals = generateCourses();
        
        int done = 0;
        try {
            FileOutputStream file = new FileOutputStream(filePath);
            ObjectOutputStream writer = new ObjectOutputStream(file);
            
            for (String[] line : meals) {
                Course course = new Course();
                course.name = line[0];
                course.type = Course.strToType(line[1]);
                course.ingredients = new LinkedList<>(Arrays.asList(
                                Arrays.copyOfRange(line, 2, line.length)));
                writer.writeObject(course);
                ++done;
            }
            
            writer.close();
            file.close();
            System.out.println("--- generated " + done + " lines into " 
                                + filePath + " ---");
        } catch (Exception ex) {
            System.err.println("failed to write to " + filePath + ", " + ex);
        }        
    }
    
    public static void readUsers(String filePath, boolean printAll) {
        
        SerialReader<CanteenUser> reader = new SerialReader<>(filePath);
        LinkedList<CanteenUser> users = new LinkedList<>();
        
        CanteenUser user;
        while ((user = reader.getNextLine()) != null)
            users.add(user);

        System.out.println("found " + users.size() + " users in " + filePath);
        if (printAll) 
            for (CanteenUser u : users)
                System.out.println(u);
    }
    
    private static void switchFiles(String newFilePath, String oldFilePath) {    
        File newFile = new File(newFilePath);
        newFile.renameTo(new File(oldFilePath));
    }
    
    private static CanteenUser getUserAtLine(String filePath, int lineNo) {   
        SerialReader<CanteenUser> reader = new SerialReader<>(filePath);
        return reader.findAt(lineNo);
    }
    
    public static void replaceARandomUser(String filePath) {

        // generate a random user until there is one that exists in the file
        Random seed = new Random();
        SerialReader<CanteenUser> reader = new SerialReader<>(filePath);

        int lineMax = (generateNames().length-2)*(generateSurnames().length-2);
        long lineUser = seed.nextInt(lineMax);
        CanteenUser userToFind = reader.findAt(lineUser);
        
        System.out.println("found " + userToFind.type() + " " + 
                           userToFind.name() + " " + userToFind.surname() +
                           " at line " + lineUser);
        
        // generate a random user to replace at that line
        CanteenUser userToReplace = generateUser(seed);
        
        try {
            SerialWriter<CanteenUser> writer = 
                                        new SerialWriter<>(filePath+".tmp");
            
            long cur = 0;
            reader.reset();
            // copy beginning file to the tmp
            while (cur < lineUser) {
                writer.writeNextLine(reader.getNextLine());
                ++cur;
            }
            reader.getNextLine(); // discard line to be replaced
            writer.writeNextLine(userToReplace); // add the new line

            // copy ending file to the tmp
            try {
                CanteenUser user;
                while ((user = reader.getNextLine()) != null)
                    writer.writeNextLine(user);
            } catch (Exception ex) {
                //end of file to read
            }
            writer.close();
            
            // move tmp file to original
            switchFiles(filePath+".tmp", filePath);
            long lineReplaced = reader.find(userToReplace);
            reader.close();
            
            // communicate to console
            String msgReplaced = "replaced with  " + userToReplace.type() + " " + 
               userToReplace.name() + " " + userToReplace.surname();
            if (lineReplaced >= 0)
                if (lineReplaced == lineUser)
                    System.out.println(msgReplaced + " successfully at" + 
                              " original line " + lineUser);
                else
                    System.err.println(msgReplaced + " at line " + 
                              lineReplaced + ", original line was " + lineUser);
            else
                System.err.println("could not replace with user " + 
                       userToReplace.type() + " " + userToReplace.name() + " " +
                       userToReplace.surname());
        } catch (Exception ex) {
            System.err.println("failed to write to " + filePath + ", " + ex);
        }
    }
    

    private static String[] generateNames() {
        // data
        String[] names = {"Antonio", "Matteo", "Andrea", "Gabriele", 
                          "Alessandro", "Franco",
                          "Sara", "Elena", "Claudia", "Teresa", "Giulia", 
                          "Maria"};
        return names;
    }
    
    private static String[] generateSurnames(){
        String [] surnames = {"Bianchi", "Rossi", "Verdi", "Esposito" , 
                                "Secchi", "Capone","Ferrari", "Costa", 
                                "Gallo", "Fontana", "Ricci", "Mastri"};
        return surnames;
    }
    
    private static String[] generateTypes() {
        String[] types = {"student", "professor"};
        return types;
    }
    
    
    private static CanteenUser generateUser(Random seed) {
        
        String[] names = generateNames();
        String[] surnames = generateSurnames();
        String[] types = generateTypes();
        
        String name = names[seed.nextInt(names.length)];
        String surname = surnames[seed.nextInt(surnames.length)];
        String type = types[seed.nextInt(types.length)];
        
        CanteenUser user;
        
        if (type.equals("student"))
            user = new Student(name, surname);
        else
            user = new Professor(name, surname);

        user.setPhone(generatePhone(seed));
        user.setAddress(generateAddress(seed));
        if (type.equals("student")) {
            ((Student)user).setParents(generateParents(surname, seed));
            ((Student)user).setNotes("");
        }
        user.setAllergies(generateAllergies(seed));
        return user;
    }
    
    public static void generateUsers(String filePath) {
        
        // random seed and variables for the loop
        Random seed = new Random();
        String uniqueName;
        CanteenUser user;

        int todo = (generateNames().length-2) * (generateSurnames().length-2);
        int done = 0;
        HashSet<String> uniqueUsers = new HashSet<>(todo);
        LinkedList<CanteenUser> usersToWrite = new LinkedList<>();
        
        while (done < todo) {
            user = generateUser(seed);
            uniqueName = user.name() + " " + user.surname();
            
            if (!uniqueUsers.contains(uniqueName)) {
                uniqueUsers.add(uniqueName);
                usersToWrite.add(user);
                ++done;
            }
        }

        try {
            int num = 0;
            FileOutputStream file = new FileOutputStream(filePath);
            ObjectOutputStream writer = new ObjectOutputStream(file);
            for (CanteenUser u : usersToWrite) {
                writer.writeObject(u);
                ++num;
            }
            writer.close();
            file.close();
            System.out.println("--- generated " + num + " lines into " 
                                + filePath + " ---");
        } catch (Exception ex) {
            System.err.println("failed to write to " + filePath + ", " + ex);
        }
        
    }
    
    public static void main(String[] args){
        
        System.out.println("--- DataGenerator ---");
        
        if (args.length < 1) {
            System.err.println("missing arguments");
            System.out.println("$ java DataGenerator /path/to/data/");
            System.exit(1);
        }
        
        System.out.println("... writing to " + args[0]);
        
        DataGenerator.generateUsers(args[0] + "/users.dat");
        DataGenerator.readUsers(args[0] + "/users.dat", false); // data check
        DataGenerator.replaceARandomUser(args[0] + "/users.dat");
        DataGenerator.readUsers(args[0] + "/users.dat", false); // data check

        DataGenerator.generateMeals(args[0] + "/courses.dat");
        DataGenerator.readMeals(args[0] + "/courses.dat", false); // data check
        
        DataGenerator.generateMenus(args[0] + "/menus.dat");
        DataGenerator.readMenuPlan(args[0] + "/menus.dat", false); // data check
        
    }
    
}
