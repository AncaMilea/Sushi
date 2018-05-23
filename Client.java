package client;

import common.*;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Client extends Comms implements ClientInterface{
    List<UpdateListener> list=new ArrayList<>();


    public Client()
    {
        try {
            Socket serverSocket = new Socket("localhost", Comms.getPort());
           ObjectOutputStream out = new ObjectOutputStream(serverSocket.getOutputStream());

           ObjectInputStream in = new ObjectInputStream(serverSocket.getInputStream());


        } catch (UnknownHostException e) {

            System.err.println(e);

            System.exit(1);
        } catch (IOException e) {

            System.err.println(e);
            System.exit(1);
        }

    }

    @Override
    public User register(String username, String password, String address, Postcode postcode) {
        User newU= new User(username,password,address,postcode);
        sendMessage(new Message(MessageType.REGISTER,newU));
        Message m=receiveMessage();
        return (User) m.getObject();

    }
    @Override
    public User login(String username, String password) {

        sendMessage(new Message(MessageType.LOGIN,username,password));
        Message m=receiveMessage();
        return (User) m.getObject();
    }

    @Override
    public List<Postcode> getPostcodes() {
        List<Postcode> p=new ArrayList<>();
        sendMessage(new Message(MessageType.POSTCODE,p));
        Message m= receiveMessage();
        return (List<Postcode>) m.getObject();
    }

    @Override
    public List<Dish> getDishes() {
        List<Dish> d= new ArrayList<>();
        sendMessage(new Message(MessageType.DISH,d));
        Message m= receiveMessage();
        return (List<Dish>) m.getObject();
    }

    @Override
    public String getDishDescription(Dish dish) {
        sendMessage(new Message(MessageType.DISH_DESCRIPTION,dish));
        Message m= receiveMessage();
        return (String) m.getObject();
    }

    @Override
    public Number getDishPrice(Dish dish) {
        sendMessage(new Message(MessageType.DISH_PRICE,dish));
        Message m= receiveMessage();
        return (Number) m.getObject();
    }

    @Override
    public Map<Dish, Number> getBasket(User user) {

        sendMessage(new Message(MessageType.BASKET,user));
        Message m= receiveMessage();
        return (Map<Dish,Number>) m.getObject();
    }

    @Override
    public Number getBasketCost(User user) {

        sendMessage(new Message(MessageType.BASKET_COST,user));
        Message m= receiveMessage();
        return (Number) m.getObject();
    }

    @Override
    public void addDishToBasket(User user, Dish dish, Number quantity) {
        sendMessage(new Message(MessageType.BASKET_ADD_DISH,user,dish,quantity));


    }

    @Override
    public void updateDishInBasket(User user, Dish dish, Number quantity) {
        sendMessage(new Message(MessageType.BASKET_UPDATE_DISH,user,dish,quantity));


    }

    @Override
    public Order checkoutBasket(User user) {

        sendMessage(new Message(MessageType.BASKET_CHECKOUT,user));
        Message m= receiveMessage();
        return (Order) m.getObject();
    }

    @Override
    public void clearBasket(User user) {
        sendMessage(new Message(MessageType.BASKET_CLEAR,user));

    }

    @Override
    public List<Order> getOrders(User user) {

        sendMessage(new Message(MessageType.ORDER,user));
        Message m= receiveMessage();
        return (List<Order>) m.getObject();
    }

    @Override
    public boolean isOrderComplete(Order order) {
        sendMessage(new Message(MessageType.ORDER_COMPLETE,order));
        Message m= receiveMessage();
        return (boolean) m.getObject();
    }

    @Override
    public String getOrderStatus(Order order)
    {
        sendMessage(new Message(MessageType.ORDER_STATUS,order));
        Message m= receiveMessage();
       return (String) m.getObject();
    }

    @Override
    public Number getOrderCost(Order order) {
        sendMessage(new Message(MessageType.ORDER_COST,order));
        Message m= receiveMessage();
        return (Number) m.getObject();
    }

    @Override
    public void cancelOrder(Order order) {
        sendMessage(new Message(MessageType.ORDER_CANCEL,order));

    }

    @Override
    public void addUpdateListener(UpdateListener listener) {
        list.add(listener);
    }

    @Override
    public void notifyUpdate() {

        for(UpdateListener listener:list)
            listener.updated(new UpdateEvent());
    }
}
