package comm;

import canteen.Menu;
import java.util.Date;
import java.text.Format;
import java.util.TreeMap;

public class ViewMenusResponse extends Response{
    
    private TreeMap<Date, Menu> mMenus;
    
    public ViewMenusResponse() {
        super("ViewMenus");
        mMenus = new TreeMap<>();
    }
   
    public TreeMap<Date, Menu> getMenus() {
        return mMenus;
    }
    
    public void setMenus(TreeMap<Date, Menu> menus) {
        mMenus = menus;
    }
    
}
