package comm;

import canteen.Menu;
import java.util.HashSet;

public class ViewMenusResponse extends Response{
    
    private HashSet<Menu> mMenus;
    
    public ViewMenusResponse() {
        super("ViewMenus");
        mMenus = new HashSet<>();
    }
   
    public HashSet<Menu> getMenus() {
        return mMenus;
    }
    
    public void setMenus(HashSet<Menu> menus) {
        mMenus = menus;
    }
 
    public boolean isEmpty() {
        return mMenus.isEmpty();
    }
    
}
