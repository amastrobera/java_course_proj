package university;


public class Professor extends CanteenUser {
        
    public Professor(String name, String surname) {
        super(name, surname);
        mType = "professor";
    }
        
    public Professor() {
        this("", "");
    }
    
    @Override
    public String toString(){
        return super.toString();
    }
    
}
