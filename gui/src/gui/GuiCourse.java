package gui;

import java.util.LinkedList;
import javafx.beans.property.SimpleStringProperty;

import canteen.*;

public class GuiCourse {

    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty type = new SimpleStringProperty("");
    private final SimpleStringProperty ingredients = new SimpleStringProperty("");
    
    public GuiCourse() {
        this(new Course());
    }

    public GuiCourse(Course course) {
        setName(course.name);
        setType(Course.typeToString(course.type));
        setIngredients(course.ingredients);
    }

    public String getName() {
        return name.get();
    }

    public final void setName(String fName) {
        name.set(fName);
    }
    
    public String getType() {
        return type.get();
    }
    
    public final void setType(String fType) {
        type.set(fType);
    }
    
    public String getIngredients() {
        return ingredients.get();
    }
    
    public final void setIngredients(LinkedList<String> fIngredients) {
        String strIngredients = String.join(", ", fIngredients);
        ingredients.set(strIngredients);
    }
    
    @Override
    public String toString(){
        return name.get() + " (" + type.get() + ") " + ingredients.get();
    }
  
}
