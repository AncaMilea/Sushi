package common;

import java.io.Serializable;

public class Postcode extends Model implements Serializable{
    String name;
    Number distance;
    public Postcode(String name, Number distance)
    {
        this.name=name;
        this.distance=distance;
    }
    @Override
    public String getName() {
        return name;
    }
    public Number getDistance(){
        return distance;
    }
}
