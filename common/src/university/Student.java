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
                    parents += mParents[i].toString() + ",";
        if (parents.length() > 1) 
            parents = parents.substring(0, parents.length()-1);
        return super.toString() + "\nparents: " + parents  + 
                "\nnotes: " + mNotes;
    }
    
}
