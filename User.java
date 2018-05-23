package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User extends Model implements Serializable {
    String username;
    String password;
    String location;
    Postcode postcode;
    List<Postcode> posting=new ArrayList<>();
    public User(String name, String password, String location, Postcode postcode)
    {
        this.username=name;
        this.password=password;
        this.location=location;
        this.postcode=postcode;
        posting.add(postcode);
    }
    @Override
    public String getName() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getLocation(){
        return location;
    }
    public Postcode getPostcode()
    {
        return postcode;
    }

}
