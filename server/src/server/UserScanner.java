package server;

import university.*;
import canteen.Menu;

import java.util.ArrayList;

public abstract class UserScanner {
    
    /**
    * @param <none> 
    * 
    * @return Array of users in our database or file-system
    */
    public abstract ArrayList<CanteenUser> getUsers();
    
    /**
    * @param <menu> a collection of four courses (only the name)
    * 
    * @return Array of users that are allergic to any ingredient of any of the 
    * four courses of the menu
    */
    public abstract ArrayList<CanteenUser> getAllergicUsers(Menu menu);
    
}
