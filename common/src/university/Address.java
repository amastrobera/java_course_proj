package university;


public class Address {
    public String street, postcode, city;
    
    public Address(String street, String postcode, String city) {
        this.street = street;
        this.postcode = postcode;
        this.city = city;
    }

    public Address() {
        this("", "", "");
    }
    
    
    @Override
    public String toString(){
        return street + "," + postcode + "," + city;
    }
}
