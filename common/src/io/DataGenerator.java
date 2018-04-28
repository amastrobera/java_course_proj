package io;

import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;

public class DataGenerator {

    // platform independent flat file generator with data
    
    private static void generateParents(String surname,
                                         Random seed, 
                                         ArrayList<String> output) {
        String[] father = {"Pietro", "Giovanni", "Mario", "Davide", "Claudio"};
        String[] mother = {"Maria", "Carmela", "Cristina", "Franca", "Paola"};
        String[] maiden = {"Lombardi", "Barbieri", "Moretti", "Fontana", "Conti"};
        
        int numParents = seed.nextInt(100);
        
        if (numParents < 1) { // orphan (1%) 
            output.add(",");
        } else if (numParents < 4) {  // one parent only  (4%) 
            if (seed.nextBoolean()) {
                output.add(father[seed.nextInt(father.length)] + 
                                " " + surname + " " + 
                                generatePhone(seed) + ",");
            } else {
                output.add("," +
                            mother[seed.nextInt(mother.length)] + " " +
                                maiden[seed.nextInt(maiden.length)] + " " + 
                                generatePhone(seed));
            }
        } else {
            output.add(father[seed.nextInt(father.length)] + " " + surname + 
                        " " + generatePhone(seed) +  "," +  
                        mother[seed.nextInt(mother.length)] + 
                            " " + maiden[seed.nextInt(maiden.length)] + " " + 
                            generatePhone(seed));
        }
    }

    private static void generatePhone(Random seed,
                                        ArrayList<String> output) {
        output.add(generatePhone(seed));
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


    
    private static void generateAddress(Random seed, 
                                        ArrayList<String> output) {
        String[] streets = {"via Gramsci", "via Cavour", 
                        "largo Augusto", "via Ripamonti", "corso Como"};
        String[] postcode = {"20100", "10121", "16121", "15121", "31100"};
        String[] cities = {"Milano", "Torino", "Genova", "Alessandria", 
                        "Treviso"};
        int c = seed.nextInt(cities.length);
        output.add(streets[seed.nextInt(streets.length)] + " " + 
                        (seed.nextInt(99)+1) + "," +
                    postcode[c] + "," + 
                    cities[c]);
    }
    
    private static void generateAllergies(Random seed,
                                          ArrayList<String> output){
        String[] ingredients = {"cipolla", "pomodoro", "latte", "maiale", 
                                "olio di semi di girasole", "mozzarella", 
                                 "burro"};
        String allerg = new String();
        int num = seed.nextInt(3);
        for (int i = 0 ; i < num; ++i)
            allerg += ingredients[seed.nextInt(ingredients.length)] + ",";
        if (allerg.length() > 1)
            allerg = allerg.substring(0, allerg.length()-1);
        output.add(allerg);
    }
    
    
    public static void prepareMenuPlan(String filePath) {
        DSVWriter writer = new DSVWriter(filePath, ",", true);
                
        System.out.println("generated " + filePath);

        writer.close();
    }
    
    
    public static void generateMeals(String filePath) {
        
        // ---- meals.csv -----
        // Name (key)
        // Type = {primo, secondo, dessert, frutta}
        // Ingredients = "[ingrediente1, ingrediente2, ...]"
        
        String[] headers = {"Name", "Type", "Ingredients"};
        
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
        
        DSVWriter writer = new DSVWriter(filePath, ",", true);
        
        writer.writeLine(new ArrayList<>(Arrays.asList(headers)));
        
        int done = 1;
        
        for (String[] line : meals)
            if (writer.writeLine(new ArrayList<>(Arrays.asList(line))))
                ++done;
        
        System.out.println("generated " + done + " lines into " + filePath);

        writer.close();
        
    }
    
    public static void generateUsers(String filePath) {
        
        // data
        String[] names = {"Antonio", "Matteo", "Andrea", "Gabriele", "Alessandro",
                          "Sara", "Elena", "Claudia", "Teresa", "Giulia"};
        String [] surnames = {"Bianchi", "Rossi", "Verdi", "Esposito" , "Secchi",
                                "Ferrari", "Costa", "Gallo", "Fontana", "Ricci"};
        String[] types = {"student", "professor"};
        
        // random seed and variables for the loop
        Random seed = new Random();
        String name, surname, uniqueName, type;
        ArrayList<String> line;
        int todo = names.length * surnames.length +1, done = 1;
        HashSet<String> uniqueUsers = new HashSet<>(todo);
        

        // ---- people.csv -----
        // Name 
        // Surname
        // Type = {student, professor, guest}
        // Telephone
        // Address = "via, cap, città"
        // Parents = "[padre],[madre]"  (only for students)
        // Allergies = "[allergia1, allergia2, ...]"
        // Notes = "..."
        
        String[] headers = {"Name", "Surname", "Type", "Telephone", "Address",
                            "Parents", "Allergies", "Notes"};
        
        DSVWriter writer = new DSVWriter(filePath, ",", true);

        writer.writeLine(new ArrayList<>(Arrays.asList(headers)));
        
        while (done < todo) {
            name = names[seed.nextInt(names.length)];
            surname = surnames[seed.nextInt(surnames.length)];
            uniqueName = name + " " + surname;
            type = types[seed.nextInt(types.length)];
            
            if (!uniqueUsers.contains(uniqueName)) {
                uniqueUsers.add(uniqueName);
                line = new ArrayList<>();
                line.add(name);
                line.add(surname);
                line.add(type);
                generatePhone(seed, line);
                generateAddress(seed, line);
                if (type.equals("student"))
                    generateParents(surname, seed, line);
                else 
                    line.add(",");
                generateAllergies(seed, line);
                line.add(""); //notes empty
                
                writer.writeLine(line);
                ++done;

            }
        }
        
        System.out.println("generated " + done + " lines into " + filePath);
        
        writer.close();
        
    }
    
    public static void main(String[] args){
        
        System.out.println("--- DataGenerator --- ");
        
        if (args.length < 1) {
            System.err.println("missing arguments");
            System.out.println("$ java DataGenerator /path/to/data/");
            System.exit(1);
        }
        
        System.out.println("... writing data into " + args[0]);
        
        DataGenerator.generateUsers(args[0] + "/people.csv");
        
        DataGenerator.generateMeals(args[0] + "/meals.csv");
        
        DataGenerator.prepareMenuPlan(args[0] + "/menu_plan.csv");
        
    }
    
}
