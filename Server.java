package server;

import common.*;
import client.*;
import javafx.geometry.Pos;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server extends Comms implements ServerInterface {
    List<Dish> DishList=new ArrayList<>();
    List<Ingredient> IngrList=new ArrayList<>();
    List<Supplier> SuppList=new ArrayList<>();
    List<Drone> DroneList=new ArrayList<>();
    List<Staff> StaffList=new ArrayList<>();
    Stock stk=new Stock();
    StockManagement stockManage=new StockManagement(stk);
    Map<User,Map<Dish,Number>> basket;
    Order OrderBasket;
    List<Order> orderList=new ArrayList<>();
    List<Postcode> PostList=new ArrayList<>();
    List<User> UserList=new ArrayList<>();
    List<UpdateListener> list=new ArrayList<>();
    Config con;
    public Boolean RestockingIngredientsSts=true;
    public Boolean RestockingDishesSts=true;




    public void ServerS() {


        new Thread(new Runnable() {
            public void run() {
                try {

                    ServerSocket serverSocket = new ServerSocket(Comms.getPort());

                    Socket clientSocket = serverSocket.accept();
                    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());


                    Message m = receiveMessage();
                    if (m != null) {
                        switch (m.getType()) {
                            case REGISTER:
                                User u = (User) m.getObject();
                                User u2 = register(u.getName(), u.getPassword(), u.getLocation(), u.getPostcode());
                                sendMessage(new Message(MessageType.REGISTER, u2));
                            case LOGIN:
                                String username = (String) m.getObject1();
                                String password = (String) m.getObject2();
                                User us = login(username, password);
                                sendMessage(new Message(MessageType.LOGIN, us));

                            case DISH:
                                receiveMessage();
                                sendMessage(new Message(MessageType.DISH, getDishes()));

                            case DISH_PRICE:
                                Dish d = (Dish) m.getObject();
                                sendMessage(new Message(MessageType.DISH_PRICE, getDishPrice(d)));

                            case DISH_DESCRIPTION:
                                Dish di = (Dish) m.getObject();
                                sendMessage(new Message(MessageType.DISH_PRICE, getDishDescription(di)));
                            case POSTCODE:
                                receiveMessage();
                                sendMessage(new Message(MessageType.POSTCODE, getPostcodes()));

                            case BASKET:
                                User u3 = (User) m.getObject();
                                Map<Dish, Number> mB = getBasket(u3);
                                sendMessage(new Message(MessageType.BASKET, mB));

                            case BASKET_ADD_DISH:
                                User u4 = (User) m.getObject();
                                Dish d1 = (Dish) m.getObject();
                                Number n = (Number) m.getObject();
                                addDishToBasket(u4, d1, n);

                            case BASKET_UPDATE_DISH:
                                User u5 = (User) m.getObject();
                                Dish d2 = (Dish) m.getObject();
                                Number n2 = (Number) m.getObject();
                                updateDishInBasket(u5, d2, n2);

                            case BASKET_COST:
                                User u6 = (User) m.getObject();
                                Number n3 = getBasketCost(u6);
                                sendMessage(new Message(MessageType.BASKET_COST, n3));

                            case BASKET_CHECKOUT:
                                User u7 = (User) m.getObject();
                                Order o1 = checkoutBasket(u7);
                                sendMessage(new Message(MessageType.BASKET_CHECKOUT, o1));

                            case BASKET_CLEAR:
                                User u8 = (User) m.getObject();
                                clearBasket(u8);

                            case ORDER:
                                User u9 = (User) m.getObject();
                                List<Order> o = getOrdersUser(u9);
                                sendMessage(new Message(MessageType.ORDER, o));

                            case ORDER_COST:
                                Order o2 = (Order) m.getObject();
                                sendMessage(new Message(MessageType.ORDER_COST, getOrderCost(o2)));

                            case ORDER_STATUS:
                                Order o3 = (Order) m.getObject();
                                sendMessage(new Message(MessageType.ORDER_STATUS, getOrderStatus(o3)));

                            case ORDER_COMPLETE:
                                Order o4 = (Order) m.getObject();
                                sendMessage(new Message(MessageType.ORDER_COMPLETE, getOrderStatus(o4)));

                            case ORDER_CANCEL:
                                Order o5 = (Order) m.getObject();
                                cancelOrder(o5);
                        }


                    }

                } catch (IOException e) {

                    System.out.println(e.getMessage());
                }
            }
        }).start();


    }
    public User register(String username, String password, String address, Postcode postcode) {

        for(Postcode pst:getPostcodes())
        {
            if(pst.getName()==postcode.getName())
            {
                getUsers().add( new User(username, password,address,postcode));
                return new User(username, password,address,postcode);
            }
        }
        return null;

    }
    public User login(String username, String password) {

        for(User usr: getUsers())
        {
            if(usr.getName()==username && usr.getPassword()==password)
                return usr;
        }

        return null;
    }

    public String getDishDescription(Dish dish) {
        return dish.getDescription();
    }

    public Number getDishPrice(Dish dish) {
        return dish.getPrice();
    }

    public Map<Dish, Number> getBasket(User user) {

        if(basket!=null)
        return basket.get(user);
        else
            return new HashMap<Dish, Number>(){};

    }

    public Number getBasketCost(User user) {
        int cost=0;
        for(Dish dish: getBasket(user).keySet()) {

            cost=cost+(int)getDishPrice(dish);

        }
        return cost;
    }

    public void addDishToBasket(User user, Dish dish, Number quantity) {
        Map<Dish,Number> miniBasket = null;
        for(Dish dsh:stk.getStockDish().keySet())
        {
            if(dsh.getName().equals(dish.getName()))
            {
                stockManage.useDish(dsh,(int)quantity);
                miniBasket.put(dish,quantity);
            }
        }

        basket.put(user,miniBasket);

    }

    public void updateDishInBasket(User user, Dish dish, Number quantity) {


                for(Dish dishUpdate: getBasket(user).keySet())
                {
                    if(dishUpdate.getName().equals(dish.getName()))
                    {
                        if((int)quantity==0) {
                            basket.remove(user);
                        }
                        else {
                            getBasket(user).put(dish, quantity);
                        }
                    }
                }
    }

    public Order checkoutBasket(User user) {
        OrderBasket=new Order(user,getBasket(user));
        getOrders().add(OrderBasket);
        return OrderBasket;
    }

    public void clearBasket(User user) {
        getBasket(user).clear();
    }

    public List<Order> getOrdersUser(User user) {
        List<Order> newList = null;
        for(Order ord:getOrders())
        {
            if(ord.user==user)
            {
                newList.add(ord);
            }

        }
        return newList;
    }

    public void cancelOrder(Order order) {
        for (Order ord : getOrders()) {
            if (ord == order) {
                getOrders().remove(ord);
            }
        }
    }


    @Override
    public void loadConfiguration(String filename) throws FileNotFoundException {

           con=new Config(filename);
           SuppList=con.getSupplier();
           IngrList=con.getIngredient();
           DishList=con.getDishes();
           UserList=con.getUsers();
           PostList=con.getPost();
           orderList=con.getOrders();
           stk=con.getStocks();
           stockManage=new StockManagement(stk);
           //StaffList=con.getStaff();
           //DroneList=con.getDrone();
           for(Staff s:con.getStaff())
           {
               this.addStaff(s.getName());
           }
           for(Drone d:con.getDrone())
           {
               this.addDrone(d.getSpeed());
           }

    }

    @Override
    public void setRestockingIngredientsEnabled(boolean enabled) {

        RestockingIngredientsSts=enabled;
    }

    @Override
    public void setRestockingDishesEnabled(boolean enabled) {
        RestockingDishesSts=enabled;
    }

    @Override
    public void setStock(Dish dish, Number stock) {
        stk.setStockDish(dish,stock);
    }

    @Override
    public void setStock(Ingredient ingredient, Number stock) {
        stk.setStockIngredients(ingredient,stock);
    }

    @Override
    public List<Dish> getDishes() {
        if(DishList!=null)
        return DishList;
        else
            return new ArrayList<>();
    }

    @Override
    public Dish addDish(String name, String description, Number price, Number restockThreshold, Number restockAmount) {
        Dish d=new Dish(name,description,price,restockThreshold,restockAmount);
        getDishes().add(d);
        stk.StockDish(d,0);

        return new Dish(name,description,price,restockThreshold,restockAmount);
    }

    @Override
    public void removeDish(Dish dish) throws UnableToDeleteException {
        getDishes().remove(dish);
    }

    @Override
    public void addIngredientToDish(Dish dish, Ingredient ingredient, Number quantity) {
        for(Dish dsh:getDishes())
        {
            if(dsh.getName().equals(dish.getName()))
                dsh.addIngr(ingredient,quantity);
        }

    }

    @Override
    public void removeIngredientFromDish(Dish dish, Ingredient ingredient) {
        for(Dish dsh:getDishes()) {
            if (dsh.getName().equals(dish.getName()))
                dsh.removeIngredient(ingredient);
        }
    }

    @Override
    public void setRecipe(Dish dish, Map<Ingredient, Number> recipe) {
        for(Dish dsh:getDishes()) {
            if (dsh.getName().equals(dish.getName()))
                dsh.setRecipe(recipe);
        }
    }

    @Override
    public void setRestockLevels(Dish dish, Number restockThreshold, Number restockAmount) {

        for(Dish dsh:getDishes()) {

            if(dsh.getName().equals(dish.getName())) {
                dsh.setRestockThresholdDish(restockThreshold);
                dsh.setRestockAmountDish(restockAmount);
            }
        }


    }

    @Override
    public Number getRestockThreshold(Dish dish) {
        for(Dish dsh:getDishes()) {
            if (dsh.getName().equals(dish.getName()))
                return dsh.getRestockThresholdDish();
        }
        return null;
    }

    @Override
    public Number getRestockAmount(Dish dish) {
        for(Dish dsh:getDishes()) {
            if (dsh.getName().equals(dish.getName()))
                return dsh.getRestockAmountDish();
        }
        return null;
    }

    @Override
    public Map<Ingredient, Number> getRecipe(Dish dish) {
        for(Dish dsh:getDishes()) {
            if (dsh.getName().equals(dish.getName()))
                return dsh.getRecipeDish();
        }
        return null;
    }

    @Override
    public Map<Dish, Number> getDishStockLevels() {
        return stk.getStockDish();

    }

    @Override
    public List<Ingredient> getIngredients() {
        if(IngrList!=null)
        return IngrList;
        else
            return IngrList=new ArrayList<>();
    }

    @Override
    public Ingredient addIngredient(String name, String unit, Supplier supplier, Number restockThreshold, Number restockAmount) {
        Ingredient i=new Ingredient(name,unit,supplier,restockThreshold,restockAmount);
        IngrList.add(i);
        stk.StockIngredient(i,0);
        return i;
    }

    @Override
    public void removeIngredient(Ingredient ingredient) throws UnableToDeleteException {
        IngrList.remove(ingredient);
    }

    @Override
    public void setRestockLevels(Ingredient ingredient, Number restockThreshold, Number restockAmount) {


                ingredient.setRestockThresholdIngredient(restockThreshold);
                ingredient.setRestockAmountIngredient(restockAmount);


    }

    @Override
    public Number getRestockThreshold(Ingredient ingredient) {
        return ingredient.getRestockThresholdIngredient();
    }

    @Override
    public Number getRestockAmount(Ingredient ingredient) {
        return ingredient.getRestockAmountIngredient();
    }

    @Override
    public Map<Ingredient, Number> getIngredientStockLevels() {

          return stk.getStockIngredients();

    }

    @Override
    public List<Supplier> getSuppliers() {
        return SuppList;
    }

    @Override
    public Supplier addSupplier(String name, Number distance) {
        SuppList.add(new Supplier(name,distance));
        return new Supplier(name,distance);
    }

    @Override
    public void removeSupplier(Supplier supplier) throws UnableToDeleteException {
        SuppList.remove(supplier);
    }

    @Override
    public Number getSupplierDistance(Supplier supplier) {
        return supplier.getDist();
    }

    @Override
    public List<Drone> getDrones() {
        return DroneList;
    }

    @Override
    public Drone addDrone(Number speed) {

        Drone dnow=new Drone(speed,stockManage,(DroneList.size()+1));
        DroneList.add(dnow);

        (new Thread(dnow)).start();
        return dnow;
    }

    @Override
    public void removeDrone(Drone drone) throws UnableToDeleteException {
        //stop thread
        DroneList.remove(drone);
    }

    @Override
    public Number getDroneSpeed(Drone drone) {
        return drone.getSpeed();
    }

    @Override
    public String getDroneStatus(Drone drone) {
        String sts=drone.getStatus();
        notifyAll();
        return sts;

    }

    @Override
    public List<Staff> getStaff() {
        return StaffList;
    }

    @Override
    public Staff addStaff(String name) {

        Staff snow=new Staff(name,stockManage);
        getStaff().add(snow);
        (new Thread(snow)).start();
        return snow;
    }

    @Override
    public void removeStaff(Staff staff) throws UnableToDeleteException {
        //stop thread here
    getStaff().remove(staff);
    }

    @Override
    public String getStaffStatus(Staff staff) {
        String sts=null;
        for(Staff st:getStaff())
        {
            if(st.getName().equals(staff.getName()))
                sts=st.getStatus();
        }
        notifyAll();
        return sts;
    }

    @Override
    public List<Order> getOrders() {
        if(orderList!=null)
        return orderList;
        else
            return new ArrayList<>();
    }

    @Override
    public void removeOrder(Order order) throws UnableToDeleteException {
        getOrders().remove(order);
    }

    @Override
    public Number getOrderDistance(Order order) {

        return order.getUser().getPostcode().getDistance();

    }

    @Override
    public boolean isOrderComplete(Order order) {
        for(Order ord:getOrders())
        {
            if(ord.getName().equals(order.getName()))
                return true;
        }
        return false;
    }

    @Override
    public String getOrderStatus(Order order) {
        if(isOrderComplete(order)==true)
            return "Done";
        else
            return "Not yet";
    }

    @Override
    public Number getOrderCost(Order order) {
        int cost=0;
        for(Order ord:getOrders())
        {
            if(ord.getUser().equals(order.getUser()))
            {
                cost=(int)getBasketCost(ord.getUser());

            }
        }
        return cost;
    }

    @Override
    public List<Postcode> getPostcodes() {
        if(PostList!=null)
            return PostList;
        return new ArrayList<>();
    }

    @Override
    public void addPostcode(String code, Number distance) {
        getPostcodes().add(new Postcode(code,distance));
    }

    @Override
    public void removePostcode(Postcode postcode) throws UnableToDeleteException {
        getPostcodes().remove(postcode);
    }

    @Override
    public List<User> getUsers() {
        if(UserList!=null)
            return UserList;
        return new ArrayList<>();
    }

    @Override
    public void removeUser(User user) throws UnableToDeleteException {
        getUsers().remove(user);
    }

    @Override
    public void addUpdateListener(UpdateListener listener) {
        list.add(listener);
    }

    @Override
    public void notifyUpdate() {
        for(UpdateListener l:list)
            l.updated(new UpdateEvent());
    }


}
