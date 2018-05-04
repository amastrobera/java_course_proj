package university;

import java.util.HashMap;

public class Student extends CanteenUser {
    private Person[] mParents;
    private String mNotes;
    
    public Student(String name, String surname) {
        super(name, surname);
        mParents = new Person[]{};
        mType = "student";
    }

    public Student() {
        this("", "");
    }
    
    public Person[] parents() {
        return mParents;
    }
    
    public void setParents(Person[] parents) {
        mParents = parents;
    }
    
    public String notes() {
        return mNotes;
    }
    
    public void setNotes(String notes) {
        mNotes = notes;
    }
    
    @Override 
    public void fromMap(HashMap<String,String> map) {
        super.fromMap(map);
        
        Person[] par = {new Person("", ""), new Person("", "")};
        try {
            String[] mapParents = map.get("Parents").split(",");
            if (mapParents.length > 1) {
                if (!mapParents[0].isEmpty()) {
                    String[] father = mapParents[0].split(" ");
                    if (father.length > 1)
                        par[0] = new Person(father[0], father[1]);
                    if (father.length > 2)
                        par[0].setPhone(father[2]);
                }
                if (!mapParents[1].isEmpty()) {
                    String[] mother = mapParents[1].split(" ");
                    if (mother.length > 1)
                        par[1] = new Person(mother[0], mother[1]);
                    if (mother.length > 2)
                        par[1].setPhone(mother[2]);
                }
            }
        } catch(Exception ex ) {
            System.err.println("exception Student.fromMap.parents (" 
                                + mName + mSurname+ ")");
            System.err.println(" ---> " + ex);
        } finally {
            mParents = par;
        }
        
        if (map.containsKey("Notes"))
            mNotes = map.get("Notes");
    }

    @Override 
    public HashMap<String,String> toMap() {
        HashMap<String,String> ret = super.toMap();
        
        try {
            String parents = new String();
            for (Person p : mParents) {
                parents += p.name() + " " + p.surname();
                if (!p.phone().isEmpty())
                    parents += " " + p.phone() + ",";
                else 
                    parents += ",";
            }
            if (parents.length() > 0)
                parents = parents.substring(0, parents.length()-1);
            ret.put("Parents", parents);
        } catch(Exception ex ) {
            System.err.println("exception Student.toMap.parents (" 
                                + mName + mSurname+ ")");
            System.err.println(" ---> " + ex);
            ret.put("Parents", "");
        }
        
        ret.put("Notes", mNotes);
        
        return ret;
    }
    
    @Override
    public String toString(){
        String parents = new String();
        if (mParents != null)
            for (int i = 0; i < mParents.length; ++i) 
                parents += mParents[i].name() + " " + 
                           mParents[i].surname() + " " + 
                           mParents[i].phone() + ",";
        if (parents.length() > 1) 
            parents = parents.substring(0, parents.length()-1);
        return "student\n" + super.toString() + "\nparents: " + parents  + 
                "\nnotes: " + mNotes;
    }
    
    //todo: remove
    public static void main(String[] args) {
        
        Person dad1 = new Person("tony", "manero");
        dad1.setPhone("074301243");
        Person mom1 = new Person("stephanie", "mangano");
        mom1.setPhone("093124312");
        
        Person mom2 = new Person("cherilyn", "sarkisian");
        mom2.setPhone("113430431");
        
        Student s1 = new Student("fredo", "manero");
        s1.setParents(new Person[]{dad1, mom1});
        s1.setPhone("12345567");
        s1.setAddress(
            new Address("899 Bergen St, Brooklyn", "11238", "New York"));
        s1.setNotes("dances also on Thursday night");
        
        Student s2 = new Student("chaz", "bono");
        s2.setParents(new Person[]{mom2});
        s2.setPhone("45678931");
        s2.setAddress(new Address("871 Seventh Avenue & 55th Street", 
                                    "10019", "New York"));
        s2.addAllergy("tomato");
        s2.addAllergy("chestnut");
        
        System.out.println(s1);
        System.out.println(s2);
    }
}
