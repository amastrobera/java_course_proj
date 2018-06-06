package comm;

import java.io.Serializable;

public class Response implements Serializable {

    public static enum Status {SUCCESS, FAILURE};
    
    private final String mType;
    private Status mStatus;
    private String mError;
    
    public Response(String type) {
        mType = type;
        mStatus = Status.SUCCESS;
        mError = new String();
    }
    
    public String type() {
        return mType;
    }
    
    public Status status() {
        return mStatus;
    }

    public void setStatus(Status status) {
        mStatus = status;
    }

    public String error() {
        return mError;
    }
    
    public void setError(String msg) {
        mError = msg;
    }
    
    @Override
    public String toString(){
        return "Response " + mType + ", status: " + 
                (mStatus == Status.SUCCESS ? "SUCCESS" : "FAILURE" );
    }

    
}
