package io;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.File;

import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashSet;

import university.*;
import canteen.*;
import com.sun.tracing.dtrace.DependencyClass;

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


    public static void readMenuPlan(String filePath) {
        
        LinkedList<Menu> menus = new LinkedList<>();
        
        try {
            FileInputStream file = new FileInputStream(filePath);
            ObjectInputStream reader = new ObjectInputStream(file);
            int read = 0;
            while (true) {
                try { 
                    Menu menu = (Menu)reader.readObject();
                    menus.add(menu);
                    ++read;
                } catch (Exception ex) {
                    System.err.println("end of reader file " + filePath + 
                                        ", read : " + read + " menus");
                    break;
                }
            }
        } catch (Exception ex) {
            System.err.println("failed to read serialized file " + filePath +
                                ", "+ ex);
        }
        
        System.out.println("found " + menus.size() + " menus in " + filePath);
        for (Menu menu : menus) 
            System.out.println(menu);
        
    }

    
    
    public static void prepareMenuPlan(String filePath) {
        
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
            System.out.println("generated " + done + " lines into " + filePath);
        } catch (Exception ex) {
            System.err.println("failed to write to " + filePath + ", " + ex);
        }
    }
    

    public static void readMeals(String filePath) {
        
        LinkedList<Course> courses = new LinkedList<>();
        
        try {
            FileInputStream file = new FileInputStream(filePath);
            ObjectInputStream reader = new ObjectInputStream(file);
            int read = 0;
            while (true) {
                try { 
                    Course course = (Course)reader.readObject();
                    courses.add(course);
                    ++read;
                } catch (Exception ex) {
                    System.err.println("end of reader file " + filePath + 
                                        ", read : " + read + " courses");
                    break;
                }
            }
        } catch (Exception ex) {
            System.err.println("failed to read serialized file " + filePath +
                                ", "+ ex);
        }
        
        System.out.println("found " + courses.size() + " course in " + filePath);
        for (Course course : courses) 
            System.out.println(course);
        
    }


    
    public static void generateMeals(String filePath) {
                
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
            System.out.println("generated " + done + " lines into " + filePath);
        } catch (Exception ex) {
            System.err.println("failed to write to " + filePath + ", " + ex);
        }
        
        System.out.println("generated " + done + " lines into " + filePath);
        
    }
    
    public static void readUsers(String filePath) {
        
        LinkedList<CanteenUser> users = new LinkedList<>();
        
        try {
            FileInputStream file = new FileInputStream(filePath);
            ObjectInputStream reader = new ObjectInputStream(file);
            int read = 0;
            while (true) {
                try { 
                    CanteenUser user = (CanteenUser)reader.readObject();
                    users.add(user);
                    ++read;
                } catch (Exception ex) {
                    System.err.println("end of reader file " + filePath + 
                                        ", read : " + read + " users");
                    break;
                }
            }
        } catch (Exception ex) {
            System.err.println("failed to read serialized file " + filePath +
                                ", "+ ex);
        }
        
        System.out.println("found " + users.size() + " users in " + filePath);
        for (CanteenUser user : users) 
            System.out.println(user);
        
    }
    
    private static void switchFiles(String newFilePath, String oldFilePath) {    
        File oldFile = new File(oldFilePath);
        oldFile.delete();
        File newFile = new File(newFilePath);
        newFile.renameTo(new File(oldFilePath));
    }
    
    public static void replaceUser(String filePath) {
        
        CanteenUser userToFind;
        CanteenUser userToReplace;
        Random seed = new Random();
        
        userToFind = new Student("Alessandro", "Esposito");
        long found = findUser(filePath, userToFind);
        if (found >= 0) {
            System.out.println("found " + userToFind.type() + " " + 
                               userToFind.name() + " " + userToFind.surname() +
                               " at line " + found);
            userToReplace = generateUser("Mario", "Rossi", "professor", seed);
            try {
                FileOutputStream output = new FileOutputStream(filePath + ".tmp");
                ObjectOutputStream writer = new ObjectOutputStream(output);
                
                FileInputStream input = new FileInputStream(filePath);
                ObjectInputStream reader = new ObjectInputStream(input);
                
                long cur = 0;
                // copy beginning file to the tmp
                while (cur < found) {
                    writer.writeObject((CanteenUser)reader.readObject());
                    ++cur;
                }
                reader.readObject(); // discard line to be replaced
                writer.writeObject(userToReplace); // add the new line
                
                // copy ending file to the tmp
                try {
                    while (true){
                        writer.writeObject((CanteenUser)reader.readObject());
                    }
                } catch (Exception ex) {
                    //end of file to read
                }
                writer.close();
                reader.close();
                
                // move tmp file to original
                switchFiles(filePath+".tmp", filePath);

                System.out.println("replaced with  " + userToReplace.type() + " " + 
                               userToReplace.name() + " " + userToReplace.surname());
                found = findUser(filePath, userToReplace);
                if (found >= 0) 
                    System.out.println("found  at line " + found);
                
            } catch (Exception ex) {
                System.err.println("failed to write to " + filePath + ", " + ex);
            }

        } 
    }
    
    /**
     * 
     * @param <filePath> the file where users have been serialised
     * @param <userToFind> Student, Professor or generic CanteenUser will be 
     *                  searched by name, surname and type
     * @return the line (number of object serialised) at which the user stands
     *          or "-1" in case the user hasn't been found
     */
    public static long findUser(String filePath, CanteenUser userToFind) {
        long line = -1;
        try {
            long cur = 0;
            FileInputStream file = new FileInputStream(filePath);
            ObjectInputStream reader = new ObjectInputStream(file);
            while (true) {
                try { 
                    CanteenUser user = (CanteenUser)reader.readObject();
                    if (user.equals(userToFind)) {
                        line = cur;
                        break;
                    }
                    ++cur;
                } catch (Exception ex) {
                    System.err.println("end of reader file " + filePath + 
                                        ", read : " + cur + " users");
                    break;
                }
            }
        } catch (Exception ex) {
            System.err.println("failed to read serialized file " + filePath +
                                ", "+ ex);
        }
        return line;
    }

    public static CanteenUser generateUser(String name, 
                                           String surname, 
                                           String type,
                                           Random seed) {
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
        
        // data
        String[] names = {"Antonio", "Matteo", "Andrea", "Gabriele", "Alessandro", "Franco",
                          "Sara", "Elena", "Claudia", "Teresa", "Giulia", "Maria"};
        String [] surnames = {"Bianchi", "Rossi", "Verdi", "Esposito" , "Secchi", "Capone",
                                "Ferrari", "Costa", "Gallo", "Fontana", "Ricci", "Mastri"};
        String[] types = {"student", "professor"};
        
        // random seed and variables for the loop
        Random seed = new Random();
        String name, surname, uniqueName, type;
        ArrayList<String> line;
        

        int todo = (names.length-1) * (surnames.length-1), done = 0;
        HashSet<String> uniqueUsers = new HashSet<>(todo);
        LinkedList<CanteenUser> usersToWrite = new LinkedList<>();
        
        while (done < todo) {
            name = names[seed.nextInt(names.length)];
            surname = surnames[seed.nextInt(surnames.length)];
            uniqueName = name + " " + surname;
            type = types[seed.nextInt(types.length)];
            
            if (!uniqueUsers.contains(uniqueName)) {
                uniqueUsers.add(uniqueName);
                CanteenUser user = generateUser(name, surname, type, seed);
                usersToWrite.add(user);
                ++done;
            }
        }

        try {
            FileOutputStream file = new FileOutputStream(filePath);
            ObjectOutputStream writer = new ObjectOutputStream(file);
            for (CanteenUser user : usersToWrite) {
                writer.writeObject(user);
            }
            writer.close();
            file.close();
            System.out.println("generated " + done + " lines into " + filePath);
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
        
        System.out.println("... writing data into " + args[0]);
        
//        DataGenerator.generateUsers(args[0] + "/users.dat");
//        DataGenerator.readUsers(args[0] + "/users.dat"); // data check
//        DataGenerator.replaceUser(args[0] + "/users.dat");
//
//        DataGenerator.generateMeals(args[0] + "/courses.dat");
//        DataGenerator.readMeals(args[0] + "/courses.dat");
        
        DataGenerator.prepareMenuPlan(args[0] + "/menus.dat");
        DataGenerator.readMenuPlan(args[0] + "/menus.dat");
        
    }
    
}
