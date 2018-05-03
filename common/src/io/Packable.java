package io;

import java.util.HashMap;

public interface Packable {
    
    /**
    * @param <args> a map of fields to fill the object from 
    * 
    * @return void
    */
    public void fromMap(HashMap<String, String> args);


    /**
    * @param <void>
    * @return HashMap of fields with the object's properties; if LinkedLists or 
    * Arrays, the map Value will contain a comma separated string
    */
    public HashMap<String, String> toMap();

}
