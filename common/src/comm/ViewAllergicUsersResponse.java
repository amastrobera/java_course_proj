package comm;

import university.CanteenUser;
import java.util.ArrayList;

public class ViewAllergicUsersResponse extends Response {
    
    private ArrayList<CanteenUser> mUsers;
    
    public ViewAllergicUsersResponse() {
        super("ViewAllergicUsers");
        mUsers = new ArrayList<>();
    }
   
    public ArrayList<CanteenUser> getUserList() {
        return mUsers;
    }
    
    public void setUserList(ArrayList<CanteenUser> users) {
        mUsers = users;
    }
    
}
