package university;

import java.util.LinkedList;

public class Parent extends Person {
    private final LinkedList<String> mIdChildren;
    
    public Parent(String name, String surname) {
        super(name, surname);
        mIdChildren = new LinkedList<>();
    }
    
    public void addChild(String child) {
        mIdChildren.add(child);
    }
    
    @Override
    public String toString() {
        String children = new String();
        for (String id : mIdChildren ) children += id + ",";
        if (children.length() > 1) 
            children = children.substring(0, children.length()-1);
        return "parent\n" + super.toString() + "\nchildren: " + children;
    }
    
    //todo: remove
    public static void main(String[] args){
        Parent p = new Parent("antonio", "maggioli");
        p.setPhone("122391312");
        p.addChild("marco");
        p.addChild("federica");
        System.out.println(p);
    }
}
