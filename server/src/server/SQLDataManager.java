package server;

import canteen.*;
import university.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;

import java.sql.*;


public class SQLDataManager extends DataManager {

    private Connection mConnection; 
    
    public SQLDataManager(String database, String user, String pass) {
        try {
            mConnection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/" + database, user, pass);
        } catch (Exception ex ) {
            System.err.println("SQLDataManager() -> " + ex);
        }
        
    }

    public void close(){
        try {
            mConnection.close();
        } catch (Exception ex) {
            System.err.println("SQLDataManager::close -> " + ex);
        }
    }
    
    @Override
    public void finalize() throws Throwable {
        close();
        super.finalize();
    }

    /** 
     * @return Array of all courses in our databases
     */    
    @Override
    public HashMap<String, ArrayList<String>> getCourses() {
        
        HashMap<String, ArrayList<String>> ret = new HashMap<>();
        ArrayList<String> first = new ArrayList<>();
        ArrayList<String> second = new ArrayList<>();
        ArrayList<String> dessert = new ArrayList<>();
        ArrayList<String> fruit = new ArrayList<>();


        String query = "select * from courses ";
        try {
            Statement stmt = mConnection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString("name");
                Course.Type type = Course.strToType(rs.getString("type"));
                switch (type) {
                    case First:
                        first.add(name);
                        break;
                    case Second:
                        second.add(name);
                        break;
                    case Dessert:
                        dessert.add(name);
                        break;
                    case Fruit:
                        fruit.add(name);
                        break;
                }
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            System.err.println("getCourses: " + ex);
        }
        ret.put("First", first);
        ret.put("Second", second);
        ret.put("Dessert", dessert);
        ret.put("Fruit", fruit);
        return(ret);
    }
    
    /** 
     * @param name of the course (approximate) i.e. "Pasta ai funghi"
     * @return Course class corresponding to that name, including ingredients
     */
    @Override
    public Course findCourse(String name) {
        Course found =  new Course();
        String query = "select c.name, c.type, c.ingredients " + 
                        "from courses as c " +
                        "where c.name like '" + name + "%'"; // injection ! 
        try {
            Statement stmt = mConnection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                found.name = rs.getString("name");
                found.type = Course.strToType(rs.getString("type"));
                for (String ingredient : rs.getString("ingredients").split(","))
                    found.ingredients.add(ingredient);
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            System.err.println("findCourse: " + ex);
        }
        return found;
    }
    

    /** 
     * @return Set of Menus ordered by Date
     */    
    @Override
    public HashSet<Menu> getMenus() {
        HashSet<Menu> ret = new HashSet<>();
        
        String query = 
            "select m.date, m.name, " +
		"c1.name as 'first', " +
		"c2.name as 'second', " +
                "c3.name as 'dessert', " +
		"c4.name as 'fruit' " +
            "from menus as m " +
                "left join courses as c1 on c1.id = m.first " +
                "left join courses as c2 on c2.id = m.second " +
                "left join courses as c3 on c3.id = m.dessert " +
                "left join courses as c4 on c4.id = m.fruit " ;
        
        try {
            Statement stmt = mConnection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Menu menu = new Menu();
                menu.setName(rs.getString("name"));
                menu.setDate(rs.getString("date"));
                menu.setCourse(rs.getString("first"), Course.Type.First);
                menu.setCourse(rs.getString("second"), Course.Type.Second);
                
                String dessert = rs.getString("dessert");
                if (dessert != null)
                    menu.setCourse(dessert, Course.Type.Dessert);
                String fruit = rs.getString("fruit");
                if (fruit != null)
                    menu.setCourse(fruit, Course.Type.Fruit);
                ret.add(menu);
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            System.err.println("getMenus: " + ex);
        }
        
        return ret;
    }
    
    /**
     * 
     * @param menu array with the name of the four course menu
     * @return Array of the corresponding Course class (including ingredients)
     */
    @Override
    public ArrayList<Course> findMenu(ArrayList<String> menu){
        
        ArrayList<Course> ret = new ArrayList<>();

        String query = "select c.name, c.type, c.ingredients " + 
                        "from courses as c " + 
                        "where ( ";
        int max = menu.size();
        for (int i = 0; i < max; ++i) {
            query +=  "c.name = \'" + menu.get(i) + "\'";
            if (i < max - 1)
                query += " or ";
        }
        query += ")";

        try {
            Statement stmt = mConnection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Course c = new Course();
                c.name = rs.getString("name");
                c.type = Course.strToType(rs.getString("type"));
                for (String ingredient : rs.getString("ingredients").split(","))
                    c.ingredients.add(ingredient);
                ret.add(c);
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            System.err.println("findMenu: " + ex);
        }
        return ret;
    }

    private CanteenUser makeUser(ResultSet rs) {
        
        try {
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            CanteenUser user = new CanteenUser(name, surname);
            String type = rs.getString("type");
            if (type != null )
                if (type.equals("student")) {
                    user = new Student(name, surname);
                    Person[] parents = new Person[2];
                    String[] arrParents = rs.getString("parents").split(", ");
                    
                    if (arrParents.length > 0 && !arrParents[0].isEmpty()){
                        String[] father = arrParents[0].split(" ");
                        if (father.length >= 2)
                            parents[0] = new Person(father[0], father[1]);
                        else 
                            parents[0] = null;
                        if (father.length >= 3)
                            parents[0].setPhone(father[2]);
                    } else 
                        parents[0] = null;
                    
                    if (arrParents.length > 1 && !arrParents[1].isEmpty()){
                        String[] mother = arrParents[1].split(" ");
                        if (mother.length >= 2)
                            parents[1] = new Person(mother[0], mother[1]);
                        else 
                            parents[1] = null;
                        if (mother.length >= 3)
                            parents[1].setPhone(mother[2]);
                    } else 
                        parents[1] = null;
                    
                    ((Student)user).setParents(parents);
                    
                } else if (type.equals("professor"))
                    user = new Professor(name, surname);

            user.setPhone(rs.getString("phone"));
            String[] address = rs.getString("address").split(",");
            user.setAddress(new Address(address[0], address[1], address[2]));

            HashSet<String> allergies = new HashSet<>();
            for (String ingredient : rs.getString("allergies").split(","))
                allergies.add(ingredient);
            user.setAllergies(allergies);
            
            return user;
            
        } catch (Exception ex) {
            System.err.println("makeUser: " + ex);
        }
        
        return new CanteenUser();
    }
    
    
    /**
    * @return Array of users in our database or file-system
    */
    @Override
    public ArrayList<CanteenUser> getUsers() {
    
        ArrayList<CanteenUser> ret = new ArrayList<>();
        
        String query = 
          "select u.name, u.surname, u.type, u.phone, " +
          "concat(a.street, ',', a.postcode, ',', a.city) as address, " +
          "concat(p1.name, ' ', p1.surname, ' ', coalesce(p1.phone,''), ', '," +
          "p2.name, ' ', p2.surname, ' ', coalesce(p2.phone,'')) as parents, "+
           "u.allergies "+
           "from users as u "+
           "left join addresses as a on u.address = a.id " +
           "left join parents as p1 on u.parent1 = p1.id "+
           "left join parents as p2 on u.parent2 = p2.id";
        
        try {
            Statement stmt = mConnection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                CanteenUser user = makeUser(rs);
                ret.add(user);
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            System.err.println("getUsers: " + ex);
        }

        return ret;
    }

    private String reformatCommas(String input) {
        String ret = new String();
        for (char c : input.toCharArray()) {
            switch (c) {
                case '\"':
                    ret += "\\\"";
                    break;
                case '\'':
                    ret += "\\\'";
                    break;
                default:
                    ret += c;
            }
        }
        return ret;
    }
    
    /**
    * @param menu a collection of four courses (only the name)
    * 
    * @return Array of users that are allergic to any ingredient of any of the 
    * four courses of the menu
    */
    @Override
    public ArrayList<CanteenUser> getAllergicUsers(Menu menu) {
        
        ArrayList<CanteenUser> ret = new ArrayList<>();
        
        ArrayList<Course> courses = findMenu(menu.courses());
        ArrayList<String> ingredients = new ArrayList<>();
        for (Course course : courses )
            for (String ingredient : course.ingredients)
            ingredients.add(ingredient);
        
        if (ingredients.isEmpty())
            return ret;
        
        String query = 
                "select u.name, u.surname, u.type, u.phone, " +
                "concat(a.street, ',', a.postcode, ',', a.city) as address, " +
          "concat(p1.name, ' ', p1.surname, ' ', coalesce(p1.phone,''), ', ', "+
          "p2.name, ' ', p2.surname, ' ', coalesce(p2.phone,'')) as parents, " +
                "u.allergies " +
                "from users as u " + 
                "left join addresses as a on u.address = a.id " +
                "left join parents as p1 on u.parent1 = p1.id " +
                "left join parents as p2 on u.parent2 = p2.id " +
                "where " +
                "( ";
                int max = ingredients.size();
                for (int i = 0; i < max; ++i) {
                    query += " u.allergies like '%"+ 
                            reformatCommas(ingredients.get(i)) +"%' ";
                    if (i < max -1)
                        query += " or ";
                }
                query += ")";

        try {
            Statement stmt = mConnection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                CanteenUser user = makeUser(rs);
                ret.add(user);
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            System.err.println("getAllergicUsers: " + ex);
        }

        return ret;
    }


    private long countItems(String table) {
        long num = 0;        
        String query = "select count(*) as num from " + table;
        try {
            Statement stmt = mConnection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next())
                num = rs.getLong("num");
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            System.err.println("getNumberOfUsers: " + ex);
        }
        return num;
    }

    
    /**
     * 
     * @param type "student", "professor", or "" for all users
     * @return count of users in the database
     */
    @Override
    public long getNumberOfUsers(String type) {
        
        long num = 0;
        
        String query = "select count(*) as num from users";
        if (!type.isEmpty())
            query += " where type = '" + type +"'";
        
        try {
            Statement stmt = mConnection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next())
                num = rs.getLong("num");
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            System.err.println("getNumberOfUsers: " + ex);
        }
        
        return num;
    }
        
    /**
    * @param menu a collection of four courses (only the name). It must
    *               contain a reference Date (menu.setDate())
    * 
    * @return true/false corresponding to the success of the operation
    */
    @Override
    public boolean saveMenu(Menu menu) {
        
        int idFirst = 0, idSecond = 0, idDessert = 0, idFruit = 0;

        try {
            String first = reformatCommas(menu.getCourse(Course.Type.First));
            String second = reformatCommas(menu.getCourse(Course.Type.Second));
            String dessert = reformatCommas(menu.getCourse(Course.Type.Dessert));
            String fruit = reformatCommas(menu.getCourse(Course.Type.Fruit));

            if (first.isEmpty() || second.isEmpty()) {
                System.err.println("saveMenu: incomplete argumets " + 
                                   "(missing first or second, both needed)");
                return false;
            }
            
            String query = "select c1.id as first, c2.id as second, ";            
            if (!dessert.isEmpty() && !fruit.isEmpty()){
                query += " c3.id as dessert, c4.id as fruit " +
                         "from courses c1, courses c2, courses c3, courses c4 "+
                         "where " +
                         "(c1.name = '" + reformatCommas(
                                 menu.getCourse(Course.Type.First)) +"' and " +
                         "c2.name = '"+ reformatCommas(
                                 menu.getCourse(Course.Type.Second)) +"' and "+
                         "c3.name = '" + reformatCommas(
                            menu.getCourse(Course.Type.Dessert)) + "' and " +
                         "c4.name = '" + reformatCommas(
                            menu.getCourse(Course.Type.Fruit)) + "')";                
            } else if (!dessert.isEmpty() && fruit.isEmpty()) {
                query += " c3.id as dessert " +
                         "from courses c1, courses c2, courses c3 " +
                         "where " +
                         "(c1.name = '" + reformatCommas(
                                 menu.getCourse(Course.Type.First)) +"' and " +
                         "c2.name = '"+ reformatCommas(
                                 menu.getCourse(Course.Type.Second)) +"' and "+
                         "c3.name = '" + reformatCommas(
                            menu.getCourse(Course.Type.Dessert)) + "')";
            } else if (dessert.isEmpty() && !fruit.isEmpty()) {
                query += " c4.id as fruit " +
                         "from courses c1, courses c2, courses c4 " +
                         "where " +
                         "(c1.name = '" + reformatCommas(
                                 menu.getCourse(Course.Type.First)) +"' and " +
                         "c2.name = '"+ reformatCommas(
                                 menu.getCourse(Course.Type.Second)) +"' and "+
                         "c4.name = '" + reformatCommas(
                            menu.getCourse(Course.Type.Fruit)) + "')";
            } else {
                System.err.println("saveMenu: incomplete argumets " + 
                                   "(missing dessert and fruit, one needed)");
                return false;
            }
            Statement stmt = mConnection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) { 
                idFirst = rs.getInt("first");
                idSecond = rs.getInt("second");
                if (!dessert.isEmpty())
                    idDessert = rs.getInt("dessert");
                else if (!fruit.isEmpty())
                    idFruit = rs.getInt("fruit");
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            System.err.println("saveMenu: " + ex);
            return false;
        }

        
        try {
            String query = "insert into menus " + 
                                 "(date, name, first, second ";
            if (idDessert > 0) {
                query += ", dessert) values ( '" +
                                menu.date() + "', '" + menu.name() + "', " +
                                idFirst + ", " + idSecond + ", " + 
                                idDessert + ")";
            } else if (idFruit > 0) {
                query += ", fruit) values ( '" +
                                menu.date() + "', '" + menu.name() + "', " +
                                idFirst + ", " + idSecond  + ", " + 
                                idFruit + ")";
            } else {
                System.err.println("saveMenu: bad input " +
                                   "(missing dessert of fruit id)");
                return false;
            }
            
            mConnection.setAutoCommit(false);
            Statement stmt = mConnection.createStatement();
            stmt.executeUpdate(query);
            mConnection.commit();
            mConnection.setAutoCommit(true);
        }
        catch (Exception ex) {
            try { 
                mConnection.rollback();
                mConnection.setAutoCommit(true);
            } catch (SQLException sqx) {}
            System.err.println("saveMenu: " + ex);
            return false;
        }
        return true;
    }

    /**
     * 
     * @param course a course with its ingredients
     * @return true/false for the success of the operation
     */
    @Override
    public boolean saveCourse(Course course) {
        
        // the id is a the hidden primary key.
        // first find if a course with the same name exists: 
        //      if yes, this function will edit an existing record
        //      if not, this function will save a new record

        int idCourse = 0;
        
        try {
            String query = "select id from courses where name = '" + 
                            reformatCommas(course.name) + "'";
            Statement stmt = mConnection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) { 
                idCourse = rs.getInt("id");
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            System.err.println("saveMenu: " + ex);
            return false;
        }        
        
        try {
            String ingredients = new String();
            for (String ingredient : course.ingredients)
                ingredients += reformatCommas(ingredient) + ",";
            if (!ingredients.isEmpty())
                ingredients = ingredients.substring(0, ingredients.length()-1);
            
            
            if (idCourse > 0) { // update existing record 
                String typeUpdate =  "update courses " + 
                     "set type = '" + Course.typeToString(course.type) + "' " +
                        "where id = " + idCourse;
                String ingredientsUpdate = "update courses " + 
                        "set ingredients = '" + ingredients + "' " + 
                        "where id = " + idCourse;
                mConnection.setAutoCommit(false);
                Statement stmt = mConnection.createStatement();
                stmt.executeUpdate(typeUpdate);
                stmt.executeUpdate(ingredientsUpdate);                
            } else { // save new record
                String query = 
                          "insert into courses(name, type, ingredients) " + 
                          "values('" + reformatCommas(course.name) + "','" +
                             Course.typeToString(course.type) + "','" +
                             ingredients + "')";
                mConnection.setAutoCommit(false);
                Statement stmt = mConnection.createStatement();
                stmt.executeUpdate(query);
            }
            mConnection.commit();
            mConnection.setAutoCommit(true);
        }
        catch (Exception ex) {
            try { 
                mConnection.rollback();
                mConnection.setAutoCommit(true);
            } catch (SQLException sqx) {}
            System.err.println("saveCourse: " + ex);
            return false;
        }
        return true;
    }

    private long findUser(CanteenUser user) {
        long index = 0;
        try {
            String query = "select id from users where " +
                "name = '" + reformatCommas(user.name()) + "' and " +
                "surname = '" + reformatCommas(user.surname()) + "' and " +
                "type = '" + reformatCommas(user.type()) + "'";
            Statement stmt = mConnection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next())
                index = rs.getInt("id");
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            System.err.println("saveMenu: " + ex);
        }
        return index;
    }
    
    private long findAddress(Address address) {
        long index = 0;
        try {
            String query = "select id from addresses where " +
                "street = '" + reformatCommas(address.street) + "' and " +
                "postcode = '" + reformatCommas(address.postcode) + "' and " +
                "city = '" + reformatCommas(address.city) + "'";
            Statement stmt = mConnection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next())
                index = rs.getInt("id");
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            System.err.println("saveMenu: " + ex);
        }
        return index;
    }

    private long findLastId(String table) {        
        long index = 0;
        try {
            String query = "select id from "+ table +" order by id desc limit 1";
            Statement stmt = mConnection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next())
                index = rs.getInt("id");
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            System.err.println("saveMenu: " + ex);
        }
        return index;
    }
    
    private long findParent(Person parent) {
        long index = 0;
        try {
            String query = "select id from parents where " +
                "name = '" + reformatCommas(parent.name()) + "' and " +
                "surname = '" + reformatCommas(parent.surname()) + "'";
            Statement stmt = mConnection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next())
                index = rs.getInt("id");
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            System.err.println("saveMenu: " + ex);
        }
        return index;
    }
    
    private boolean saveAddress(Address address) {

        try {
            String query =  "insert into addresses (street, postcode, city) " + 
                          "values ('" + reformatCommas(address.street) + "','"+
                          reformatCommas(address.postcode) + "','" +
                          reformatCommas(address.city) + "')";
            mConnection.setAutoCommit(false);
            Statement stmt = mConnection.createStatement();
            stmt.executeUpdate(query);
            mConnection.commit();
            mConnection.setAutoCommit(true);
        }
        catch (Exception ex) {
            try { 
                mConnection.rollback();
                mConnection.setAutoCommit(true);
            } catch (SQLException sqx) {}
            System.err.println("saveAddress: " + ex);
            return false;
        }
        return true;
    }

    
    private boolean editParent(Person parent, long id) {
        try {
            String query =  "update parents " +
                            "set phone = '" + parent.phone() + "' " +
                            "where id = " + id;
            mConnection.setAutoCommit(false);
            Statement stmt = mConnection.createStatement();
            stmt.executeUpdate(query);
            mConnection.commit();
            mConnection.setAutoCommit(true);
        }
        catch (Exception ex) {
            try { 
                mConnection.rollback();
                mConnection.setAutoCommit(true);
            } catch (SQLException sqx) {}
            System.err.println("saveParent: " + ex);
            return false;
        }
        return true;
    }

    
    private boolean saveParent(Person parent) {
        try {
            String query =  "insert into parents (name,surname,phone) " + 
                         "values ('" + reformatCommas(parent.name()) + "','"+
                         reformatCommas(parent.surname()) + "','" +
                         reformatCommas(parent.phone()) + "')";
            mConnection.setAutoCommit(false);
            Statement stmt = mConnection.createStatement();
            stmt.executeUpdate(query);
            mConnection.commit();
            mConnection.setAutoCommit(true);
        }
        catch (Exception ex) {
            try { 
                mConnection.rollback();
                mConnection.setAutoCommit(true);
            } catch (SQLException sqx) {}
            System.err.println("saveParent: " + ex);
            return false;
        }
        return true;
    }


    private long addAddress(Address address) {
        long idAddress;
        idAddress = findAddress(address);
        if (idAddress == 0) {
            saveAddress(address);
            idAddress = findLastId("addresses");
        }
        return idAddress;
    }
    
    private long[] addParents(Person[] parents) {
        long[] idParents = new long[2];
        
        if (parents.length < 2 ) {
            idParents[0] = idParents[1] = 0;
            
        } else {
            idParents[0] = findParent(parents[0]);
            if (idParents[0] == 0) {
                saveParent(parents[0]); // new father
                idParents[0] = findLastId("parents");
            } else
                editParent(parents[0], idParents[0]);

            idParents[1] = findParent(parents[1]);
            if (idParents[1] == 0) {
                saveParent(parents[1]); // new mother
                idParents[1] = findLastId("parents");
            } else 
                editParent(parents[1], idParents[1]);
            
        }
        
        return  idParents;
    }
    
    private boolean saveNewUser(CanteenUser user) {

        long idAddress = addAddress(user.address());
        
        long[] idParents = new long[2];
        String type = user.type();
        if (type.equals("student")) {
            idParents = addParents(((Student)user).parents());
        }
        
        // string of allergies
        String allergies = new String();
        for (String a : user.allergies())
            allergies += reformatCommas(a) + ",";
        if (allergies.length() > 1)
            allergies = allergies.substring(0, allergies.length()-1);
        
        try {
            String query;
            if (type.equals("student")) {
                query = "insert into users(name,surname,type,phone," +
                        "address,parent1,parent2,allergies) " +
                        "values('" + reformatCommas(user.name()) + "','" +
                        reformatCommas(user.surname()) + "','" +
                        user.type() + "','" +
                        reformatCommas(user.phone()) + "'," +
                        idAddress + "," +
                        idParents[0] + "," + idParents[1] + ",'" +
                        allergies + "')";
            } else {
                query = "insert into users(name,surname,type,phone," +
                        "address,allergies) " +
                        "values('" + reformatCommas(user.name()) + "','" +
                        reformatCommas(user.surname()) + "','" +
                        user.type() + "','" +
                        reformatCommas(user.phone()) + "'," +
                        idAddress + ",'" +
                        allergies + "')";
            }
            mConnection.setAutoCommit(false);
            Statement stmt = mConnection.createStatement();
            stmt.executeUpdate(query);
            mConnection.commit();
            mConnection.setAutoCommit(true);
        }
        catch (Exception ex) {
            try { 
                mConnection.rollback();
                mConnection.setAutoCommit(true);
            } catch (SQLException sqx) {}
            System.err.println("saveNewUser: " + ex);
            return false;
        }        
        return true;
    }
    
    
    private boolean updateExistingUser(CanteenUser user, long id) {
                    
        long idAddress = addAddress(user.address());
        
        long[] idParents = new long[2];
        String type = user.type();
        if (type.equals("student")) {
            idParents = addParents(((Student)user).parents());
        }
        
        // string of allergies
        String allergies = new String();
        for (String a : user.allergies())
            allergies += reformatCommas(a) + ",";
        if (allergies.length() > 1)
            allergies = allergies.substring(0, allergies.length()-1);
        
        try {
            
            String phoneUpdate = "update users " + 
                 "set phone = '" + reformatCommas(user.phone()) + "' " +
                 "where id = " + id;
            String addressUpdate = "update users " + 
                 "set address = " + idAddress + " " +
                 "where id = " + id;
            String parent1Update = new String(), parent2Update = new String();
            if (type.equals("student")) {
                parent1Update = "update users " + 
                     "set parent1 = " + idParents[0] + " " +
                     "where id = " + id;
                parent2Update = "update users " + 
                     "set parent1 = " + idParents[1] + " " +
                     "where id = " + id;
            }
            String allergiesUpdate = "update users " + 
                 "set allergies = '" + allergies + "' " +
                 "where id = " + id;
            
            mConnection.setAutoCommit(false);
            Statement stmt = mConnection.createStatement();
            stmt.executeUpdate(phoneUpdate);
            stmt.executeUpdate(addressUpdate);
            if (type.equals("student")) {
                stmt.executeUpdate(parent1Update);
                stmt.executeUpdate(parent2Update);
            }
            stmt.executeUpdate(allergiesUpdate);
            mConnection.commit();
            mConnection.setAutoCommit(true);
        }
        catch (Exception ex) {
            try { 
                mConnection.rollback();
                mConnection.setAutoCommit(true);
            } catch (SQLException sqx) {}
            System.err.println("saveNewUser: " + ex);
            return false;
        }        
        return true;
    }
    
    /**
     * 
     * @param user a CanteenUser (Professor or Student or generic)
     * @return true/false corresponding to the success of the operation
     */
    @Override
    public boolean saveUser(CanteenUser user) {
        long userId = findUser(user);        
        if (userId > 0 )
            return updateExistingUser(user, userId);
        return saveNewUser(user);
    }
    
    
    public static void main(String[] args){
        // this is a module-level test (not to be used)
        SQLDataManager manager = new SQLDataManager("meals_and_allergies",
                                             "angelo", "angelo");
        
        
        Course c = manager.findCourse("Crema di fagioli");
        System.out.println("--- test: findCourse ----\n" + c);
        
        
        ArrayList<Course> courseList = manager.findMenu(new ArrayList<String>(
                    Arrays.asList("Riso alle zucchine", 
                                  "Fettina di tacchino alla piastra",
                                  "Strudel",
                                  "Macedonia di frutta")));
        System.out.println("--- test: findMenu ----");
        for (Course course : courseList) {
            System.out.println(course);
        }
        
        HashSet<Menu> menus = manager.getMenus();
        System.out.println("--- test: getMenus ----");
        for (Menu m : menus) {
            System.out.println(m);
        }

        HashMap<String, ArrayList<String>> allcourses = manager.getCourses();
        System.out.println("--- test: getCourses ----");
        if (allcourses.containsKey("First"))
            System.out.println("         " + 
                allcourses.get("First").size() + " first");
        if (allcourses.containsKey("Second"))
            System.out.println("         " + 
                allcourses.get("Second").size() + " second");
        if (allcourses.containsKey("Dessert"))
            System.out.println("         " + 
                allcourses.get("Dessert").size() + " dessert");
        if (allcourses.containsKey("Fruit"))
            System.out.println("         " + 
                allcourses.get("Fruit").size() + " fruit");
        
        
        ArrayList<CanteenUser> allUsers = manager.getUsers();
        System.out.println("--- test: getUsers ----");
        System.out.println("         found  " + allUsers.size() + " users");
        System.out.println("         1.   " + allUsers.get(0));
        System.out.println("         2.  " + allUsers.get(1));
        System.out.println("         ... " );


        Menu menu = new Menu();
        menu.setCourse("Crema di fagioli con pasta", Course.Type.First);
        menu.setCourse("Bocconcini di Vitellone in umido", Course.Type.Second);
        menu.setCourse("Strudel", Course.Type.Dessert);
        ArrayList<CanteenUser> allergicUsers = manager.getAllergicUsers(menu);
        System.out.println("--- test: getAllergicUsers ----");
        System.out.println("         found  " + allergicUsers.size() +" users");
        System.out.println("         1.   " + allergicUsers.get(0));
        System.out.println("         2.  " + allergicUsers.get(1));
        System.out.println("         ... " );
        
        System.out.println("--- test: getNumberOfUsers ----");
        long tot = manager.getNumberOfUsers("");
        long stud = manager.getNumberOfUsers("student");
        long prof = manager.getNumberOfUsers("professor");
        System.out.println("         found " + tot +" totals");
        System.out.println("         found " + stud +" student");
        System.out.println("         found " + prof +" professors");
        
        
        System.out.println("--- test: saveMenu ----");
        // using a menu previously created
        menu.setDate("2017-12-20");
        menu.setName("pre-natale");
        boolean retMenu = manager.saveMenu(menu); 
        System.out.println("         ret: " + retMenu);
        System.out.println(manager.getMenus());
        
        System.out.println("--- test: saveCourse ----");
        Course newCourse = new Course();
        newCourse.name = "Pasta e patate";
        newCourse.type = Course.Type.First;
        newCourse.ingredients.add("pasta");
        newCourse.ingredients.add("patata");
        newCourse.ingredients.add("cipolla");
        newCourse.ingredients.add("olio d'oliva");
        boolean retCourse = manager.saveCourse(newCourse);
        System.out.println("         ret: " + retCourse);

        System.out.println("--- test: saveUser ----");
        CanteenUser user = new Student("Mario", "Panero");
        user.setAddress(new Address("via Nappi 21", "83100", "Avellino"));
        user.setPhone("212301204");
        Person[] parents = new Person[2];
        parents[0] = new Person("Franco", "Panero");
        parents[0].setPhone("0932132423");
        parents[1] = new Person("Antonietta", "Davanzo");
        ((Student)user).setParents(parents);
        user.addAllergy("cipolla");
        user.addAllergy("patata");
        user.addAllergy("burro");
        boolean retUser = manager.saveUser(user);
        System.out.println("         ret: " + retUser);
        
    }


}