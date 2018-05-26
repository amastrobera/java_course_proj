package gui;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

import canteen.*;

public class GuiMenu {

    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty date = new SimpleStringProperty("");
    private final SimpleStringProperty courses = new SimpleStringProperty("");
    
    public GuiMenu() {
        this(new Menu());
    }

    public GuiMenu(Menu menu) {
        setName(menu.name());
        setDate(menu.date());
        setCourses(menu.courses());
    }

    public String getName() {
        return name.get();
    }

    public final void setName(String fName) {
        name.set(fName);
    }

    public String getDate() {
        return date.get();
    }

    public final void setDate(String fDate) {
        date.set(fDate);
    }

    public String getCourses() {
        return courses.get();
    }
    
    public final void setCourses(ArrayList<String> fCourses) {
        String strCoures = String.join(", ", fCourses);
        courses.set(strCoures);
    }
    
    @Override
    public String toString(){
        return date.get() + " " + name.get() + ": " + courses.get();
    }
  
}
