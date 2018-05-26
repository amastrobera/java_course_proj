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
        if (user.type().equals("student")) {
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

    public final void setName(String fName) {
        name.set(fName);
    }

    public String getSurname() {
        return surname.get();
    }

    public final void setSurname(String fSurname) {
        surname.set(fSurname);
    }

    public String getType() {
        return type.get();
    }

    public final void setType(String fType) {
        type.set(fType);
    }    
    
    public String getPhone() {
        return phone.get();
    }

    public final void setPhone(String fPhone) {
        phone.set(fPhone);
    }

    public Address getAddress() {
        Address ret = new Address();
        ret.fromString(address.get());
        return ret;
    }

    public final void setAddress(Address fAddress) {
        address.set(fAddress.toString());
    }

    public String getAllergies() {
        return allergies.get();
    }
    
    public final void setAllergies(HashSet<String> fAllergies) {
        String strAllergies = String.join(", ", fAllergies);
        allergies.set(strAllergies);
    }

    public String getParents() {
        return parents.get();
    }

    public final void setParents(Person[] fParents) {
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

    public String getNotes() {
        return notes.get();
    }
    
    public final void setNotes(String fNotes) {
        parents.set(fNotes);
    }
    
    @Override
    public String toString(){
        return name.get() + " " + surname.get() + " " + type.get() + 
               ", \"" + address.get() + "\", " + phone.get() + 
               allergies.get();
    }
  
}
