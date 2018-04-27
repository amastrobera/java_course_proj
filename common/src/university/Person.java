package university;

import io.Packable;
import java.util.HashMap;

public class Person implements Packable {

    protected String mName, mSurname, mPhone;
    protected Address mAddress;
    
    public Person(String name, String surname) {
        mName = name;
        mSurname = surname;
        mPhone = null;
        mAddress = null;
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
    
    @Override
    public String toString(){
        return "name: " + mName + " " + mSurname + 
                "\ntel: " + mPhone + "\naddress: " + mAddress;
    }
    
    @Override
    public void fromMap(HashMap<String,String> map) {
        mName = map.get("Name");
        mSurname = map.get("Surname");
        mPhone = map.get("Telephone");
        String[] mapAddress = map.get("Address").split(",");
        if (mapAddress.length > 2) {
            Address address = new Address(mapAddress[0],mapAddress[1], 
                                            mapAddress[2]);
            mAddress = address;
        }
    }

    @Override
    public HashMap<String,String> toMap() {
        HashMap<String,String> ret = new HashMap<>();
        ret.put("Name", mName);
        ret.put("Surame", mSurname);
        ret.put("Telephone", mPhone);
        ret.put("Address", mAddress.toString());
        return ret;
    }
    
    //todo: remove
    public static void main(String[] args){
        Person p = new Person("marco", "maggioli");
        System.out.println(p);
        
        p = new Person("hamady", "ndiaye");
        p.setPhone("043923841");
        p.setAddress(new Address("Pala Del Mauro", "83100", "Avellino"));
        System.out.println(p);
    }
}
