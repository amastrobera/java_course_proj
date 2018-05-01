package comm;

import java.io.Serializable;

public class Response implements Serializable {

    public static enum Status {SUCCESS, FAILURE};
    
    private final String mType;
    private Status mStatus;
    
    public Response(String type) {
        mType = type;
        mStatus = Status.SUCCESS;
    }
    
    public Status status() {
        return mStatus;
    }

    public void setStatus(Status status) {
        mStatus = status;
    }

    @Override
    public String toString(){
        return "Response " + mType + ", status: " + 
                (mStatus == Status.SUCCESS ? "SUCCESS" : "FAILURE" );
    }

    
}
