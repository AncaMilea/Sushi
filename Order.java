package common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Order extends Model {
    Map<Dish, Number> order;
    public User user;

    public Order(User name, Map<Dish, Number> order)
    {
     this.user=name;
     this.order= order;
    }
    @Override
    public String getName() {
        return user.getLocation();
    }
    public User getUser(){
        return user;
    }
    public Map<Dish,Number> getOrder()
    {
        return order;
    }
}
