package comm;

public class ViewNumberOfUsersResponse extends Response {
    
    private long mNumber;
    private String mType;
    
    public ViewNumberOfUsersResponse() {
        super("ViewNumberOfUsers");
        mNumber = 0;
        mType = new String();
    }
   
    public long getNumber() {
        return mNumber;
    }
    
    public void setNumber(long num) {
        mNumber = num;
    }

    public String getType() {
        return mType;
    }
    
    public void setType(String type) {
        mType = type;
    }    
}
