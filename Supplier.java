package common;

public class Supplier extends Model {
    String name;
    Number dist;

    public Supplier(String name, Number dist)
    {
        this.name=name;
        this.dist=dist;
    }
    @Override
    public String getName() {
        return name;
    }
    public Number getDist(){
        return dist;
    }



}
