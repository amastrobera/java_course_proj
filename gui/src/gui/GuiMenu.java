package gui;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

import canteen.*;

public class GuiMenu {

    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty date = new SimpleStringProperty("");
    private final SimpleStringProperty first = new SimpleStringProperty("");
    private final SimpleStringProperty second = new SimpleStringProperty("");
    private final SimpleStringProperty dessert = new SimpleStringProperty("");
    private final SimpleStringProperty fruit = new SimpleStringProperty("");
    
    public GuiMenu() {
        this(new Menu());
    }

    public GuiMenu(Menu menu) {
        setName(menu.name());
        setDate(menu.date());
        setFirst(menu.getCourse(Course.Type.First));
        setSecond(menu.getCourse(Course.Type.Second));
        setDessert(menu.getCourse(Course.Type.Dessert));
        setFruit(menu.getCourse(Course.Type.Fruit));
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

    public String getFirst() {
        return first.get();
    }
    
    public final void setFirst(String rFirst) {
        first.set(rFirst);
    }

    public String getSecond() {
        return second.get();
    }
    
    public final void setSecond(String rSecond) {
        second.set(rSecond);
    }
    
    public String getDessert() {
        return dessert.get();
    }
    
    public final void setDessert(String rDessert) {
        dessert.set(rDessert);
    }
    
    public String getFruit() {
        return fruit.get();
    }
    
    public final void setFruit(String rFruit) {
        fruit.set(rFruit);
    }
    
    
    @Override
    public String toString(){
        return date.get() + " " + name.get() + ": " + first.get() + ", "  +
                second.get() + ", " + dessert.get() + ", " + fruit.get();
    }
  
}
