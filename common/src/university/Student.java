package university;


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
    public String toString(){
        String parents = new String();
        if (mParents != null)
            for (int i = 0; i < mParents.length; ++i) 
                if (mParents[i] != null)
                    parents += mParents[i].name() + " " + 
                               mParents[i].surname() + " " + 
                               mParents[i].phone() + ",";
        if (parents.length() > 1) 
            parents = parents.substring(0, parents.length()-1);
        return "student\n" + super.toString() + "\nparents: " + parents  + 
                "\nnotes: " + mNotes;
    }
    
    public static void main(String[] args) {
        // this is a module-level test (not to be used)    
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
