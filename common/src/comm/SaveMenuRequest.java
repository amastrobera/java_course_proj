package comm;

import canteen.Menu;

public class SaveMenuRequest extends Request {
    
    private final Menu mMenu;
    
    public SaveMenuRequest(Menu menu) {
        super("SaveMenu");
        mMenu = menu;
    }
    
    public Menu getMenu() {
        return mMenu;
    }
    
    @Override
    public String toString(){
        return super.toString() + " >> menu: " + mMenu;
    }
}
