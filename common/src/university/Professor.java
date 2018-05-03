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
        return "professor\n" + super.toString();
    }
    
    //todo: remove
    public static void main(String[] args) {
        Professor p = new Professor("eugene", "wise");
        p.setPhone("12345567");
        p.setAddress(new Address("Hardstrasse 201", "8005", "Zurich"));
        
        Professor m = new Professor("michal", "jarzabek");
        m.setPhone("45678931");
        m.setAddress(new Address("250 Pentonville Rd", "N1 9JY", "London"));
        
        System.out.println(p);
        System.out.println(m);
    }
}
