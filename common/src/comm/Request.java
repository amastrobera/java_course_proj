package comm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;

public class Request implements Serializable {
    
    private final String mType;    
    protected HashMap<String,String> mParams;
    
    public Request(String type) {
        mType = type;
        mParams = new HashMap<>();
    }
        
    public String type() {
        return mType;
    }
    
    public void setParam(String k, String v) {
        mParams.put(k, v);
    }

    public void setParams(HashMap<String,String> packed) {
        mParams = packed;
    }
    
    public String getParam(String k) {
        String p = new String();
        try {
            p = mParams.get(k);
        } catch(Exception ex) {
            return "";
        }
        return p;
    }

    public HashMap<String,String> params() {
        return mParams;
    }
    
    /**
     * @param k name of a parameters containing a list compressed into a 
     *                string, of this type "[a, b, c, ...]"
     * @return an ArrayList of the type [a,b,c,...]
    */
    public ArrayList<String> getList(String k) {
        String strList = mParams.get(k);
        ArrayList<String> list = new ArrayList<>(Arrays.asList(
            strList
                .substring(1, strList.length()-1)
                .split(",")
            ));
        list.replaceAll(String::trim);
        return list;
    }

    /**
     * @param k name of the parameter in the map 
     * @param v list of type [a,b,c,...] to the compressed into a string
     * @return void
    */
    public void setList(String k, ArrayList<String> v) {
        mParams.put(k, v.toString());
    }
    
    
    @Override
    public String toString(){
        return "Request " + mType + ", params: " + mParams;
    }
    
}
