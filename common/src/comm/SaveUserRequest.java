package comm;

import university.CanteenUser;

public class SaveUserRequest extends Request {
    
    private final CanteenUser mUser;
    
    public SaveUserRequest(CanteenUser user) {
        super("SaveUser");
        mUser = user;
    }
    
    public CanteenUser getUser() {
        return mUser;
    }
    
    @Override
    public String toString(){
        return super.toString() + " >> user: " + mUser;
    }
    
}
