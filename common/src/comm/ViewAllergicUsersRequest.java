package comm;

import canteen.Menu;

public class ViewAllergicUsersRequest extends Request {
    
    private final Menu mMenu;
    
    public ViewAllergicUsersRequest(Menu menu) {
        super("ViewAllergicUsers");
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
