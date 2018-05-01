package university;

import java.io.Serializable;


public class Address implements Serializable {
    public String street, postcode, city;
    
    public Address(String street, String postcode, String city) {
        this.street = street;
        this.postcode = postcode;
        this.city = city;
    }

    public Address() {
        this("", "", "");
    }
    
    public void fromString(String rAddress) {
        String[] strAddress = rAddress.split(", ");
        if (strAddress.length > 0)
            street = strAddress[0];
        if (strAddress.length > 1)
            postcode = strAddress[1];
        if (strAddress.length > 2)
            city = strAddress[2];
    }
    
    boolean isEmpty() {
        return street.isEmpty() && postcode.isEmpty() && city.isEmpty();
    }
    
    @Override
    public String toString(){
        return street + ", " + postcode + ", " + city;
    }
}
