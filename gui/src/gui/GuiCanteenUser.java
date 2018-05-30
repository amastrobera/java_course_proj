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
    private final SimpleStringProperty father = new SimpleStringProperty("");
    private final SimpleStringProperty mother = new SimpleStringProperty("");
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
            setParents(s.parents());
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

    public final String getFather() {
        return father.get();
    }

    public final void setFather(Person rFather) {
        if (rFather != null)
            father.set(rFather.name() + " " + rFather.surname() + " " + 
                        rFather.phone());
    }
    
    public final String getMother() {
        return mother.get();
    }
    
    public final void setMother(Person rMother) {
        if (rMother != null)
            mother.set(rMother.name() + " " + rMother.surname() + " " + 
                        rMother.phone());
    }
    
    public final void setParents(Person[] fParents) {
        if (fParents.length > 0)
            setFather(fParents[0]);
        if (fParents.length > 1)
            setMother(fParents[1]);
    }

    public String getNotes() {
        return notes.get();
    }
    
    public final void setNotes(String fNotes) {
        notes.set(fNotes);
    }
    
    @Override
    public String toString(){
        return name.get() + " " + surname.get() + " " + type.get() + 
               ", \"" + address.get() + "\", " + phone.get() + 
               allergies.get();
    }
  
}
