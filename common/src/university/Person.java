package university;

import java.io.Serializable;

public class Person implements Serializable {

    protected String mName, mSurname, mPhone;
    protected Address mAddress;
    
    public Person(String name, String surname) {
        mName = name;
        mSurname = surname;
        mPhone = new String();
        mAddress = new Address();
    }
    
    public void setPhone(String phone) {
        mPhone = phone;
    }
    
    public void setAddress(Address address) {
        mAddress = address;
    }
    
    public String name() { return mName; }
    public String surname() { return mSurname; }
    public String phone() { return mPhone; }
    public Address address() { return mAddress; }
    
    @Override
    public String toString(){
        String ret = mName + " " + mSurname;
        if (!mPhone.isEmpty())
            ret += " " + mPhone;
        if (!mAddress.isEmpty())
            ret += " " + mAddress;
        return ret;
    }
    
    @Override 
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Person)) return false;
        Person comp = (Person) obj;
        return    comp.name().equals(mName) 
               && comp.surname().equals(mSurname);
               //&& comp.address().equals(mAddress);
    }
    
    public static void main(String[] args){
        // this is a module-level test (not to be used)
        Person p = new Person("marco", "maggioli");
        System.out.println(p);
        
        p = new Person("hamady", "ndiaye");
        p.setPhone("043923841");
        p.setAddress(new Address("Pala Del Mauro", "83100", "Avellino"));
        System.out.println(p);
    }
}
