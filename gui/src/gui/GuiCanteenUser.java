package gui;

import java.util.HashSet;
import javafx.beans.property.SimpleStringProperty;

import university.*;

public class GuiCanteenUser {

    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty surname = new SimpleStringProperty("");
    private final SimpleStringProperty type = new SimpleStringProperty("");
    private final SimpleStringProperty phone = new SimpleStringProperty("");
    private final SimpleStringProperty address = new SimpleStringProperty("");
    private final SimpleStringProperty allergies = new SimpleStringProperty("");
    private final SimpleStringProperty parents = new SimpleStringProperty("");
    private final SimpleStringProperty notes = new SimpleStringProperty("");
    
    public GuiCanteenUser() {
        this(new CanteenUser());
    }

    public GuiCanteenUser(CanteenUser user) {
        setName(user.name());
        setSurname(user.surname());
        setType(user.type());
        setAddress(user.address());
        setPhone(user.phone());
        setAllergies(user.allergies());
        if (user.type().equals("Student")) {
            Student s = (Student)user;
            Person[] p = s.parents();
            setParents(p);
        } else {
            Person[] p = {};
            setParents(p);
        }
    }

    public String getName() {
        return name.get();
    }

    public void setName(String fName) {
        name.set(fName);
    }

    public String getSurname() {
        return surname.get();
    }

    public void setSurname(String fSurname) {
        surname.set(fSurname);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String fType) {
        type.set(fType);
    }    
    
    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String fPhone) {
        phone.set(fPhone);
    }

    public Address getAddress() {
        Address ret = new Address();
        ret.fromString(address.get());
        return ret;
    }

    public void setAddress(Address fAddress) {
        address.set(fAddress.toString());
    }

    public String getAllergies() {
        return allergies.get();
    }
    
    public void setAllergies(HashSet<String> fAllergies) {
        String strAllergies = String.join(", ", fAllergies);
        allergies.set(strAllergies);
    }

    
    public void setParents(Person[] fParents) {
        String ret = new String();
        try {
            for (Person p : fParents)
                ret += p.toString() + ", ";
            if (ret.length() > 0)
                ret = ret.substring(0, ret.length()-2);
        } catch (Exception ex ) {
            System.out.println("setParents: " + ex );
        }
        parents.set(ret);
    }

    public String getParents() {
        return parents.get();
    }

    public void setNotes(String fNotes) {
        parents.set(fNotes);
    }

    public String getNotes() {
        return notes.get();
    }
    
    @Override
    public String toString(){
        return name.get() + " " + surname.get() + " " + type.get() + 
               ", \"" + address.get() + "\", " + phone.get() + 
               allergies.get();
    }
  
}
