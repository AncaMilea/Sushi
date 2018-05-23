package server;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client.*;
import common.*;

import javax.crypto.NullCipher;


public class Config {

    String current ;
    List<Supplier> sup= new ArrayList<>();
    List<User> user= new ArrayList<>();
    List<Postcode> pass= new ArrayList<>();
    List<Ingredient> ingr= new ArrayList<>();
    List<Dish> dsh= new ArrayList<>();
    List<Order> ord= new ArrayList<>();
    List<Drone> drn= new ArrayList<>();
    List<Staff> stff= new ArrayList<>();
    Stock st=new Stock();
    StockManagement stM=new StockManagement(st);


    public Config(String Filename) throws FileNotFoundException{

        File finstream= new File(Filename);
        BufferedReader brIn= new BufferedReader(new FileReader(finstream));

        try {
            while ((current = brIn.readLine()) != null) {
                 if (current.contains("SUPPLIER")) {
                    String[] parts = current.split(":");
                        Supplier i= new Supplier(parts[1], Integer.parseInt(parts[2]));
                        sup.add(i);
                } else {
                    if (current.contains("INGREDIENT")) {
                        String[] parts = current.split(":");
                         Ingredient in;
                            for (Supplier s : sup) {
                                if (s.getName().equals(parts[3])) {
                                    in = new Ingredient(parts[1], parts[2], s, Integer.parseInt(parts[4]), Integer.parseInt(parts[5]));
                                    ingr.add(in);
                                    st.setStockIngredients(in,0);
                                }
                            }


                    } else {
                        if (current.contains("DISH")) {
                            String[] parts = current.split(":");

                            int j = 0;
                            Dish dishHere = new Dish(parts[1], parts[2], Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5]));


                                String[] p1 = parts[6].split(",");
                                while (j > -1) {

                                        String[] p2 = p1[j].split(" \\* ");
                                        for (Ingredient ingre : ingr) {
                                        if (Integer.parseInt(p2[0]) != 0) {
                                            if (ingre.getName().equals(p2[1])) {
                                                dishHere.addIngr(ingre, Integer.parseInt(p2[0]));
                                                j = j + 1;
                                            } else {
                                                j = -1;
                                            }
                                        }
                                    }
                                }
                                dsh.add(dishHere);
                                st.setStockDish(dishHere,0);


                        } else {
                            if (current.contains("POSTCODE")) {
                                String[] parts = current.split(":");

                                   Postcode ps= new Postcode(parts[1], Integer.parseInt(parts[2]));
                                   pass.add(ps);


                            } else {

                                if (current.contains("USER")) {
                                    String[] parts = current.split(":");
                                    User u;
                                        for (Postcode post : pass) {
                                            if (post.getName().equals(parts[4])) {
                                                u = new User(parts[1], parts[2], parts[3], post);
                                                user.add(u);
                                            }

                                        }


                                } else {
                                    if (current.contains("ORDER")) {
                                        int i = 0;
                                        Map<Dish, Number> newM= new HashMap<>();
                                        String[] parts = current.split(":");
                                            for (User us :user) {
                                                if (us.getName().equals(parts[1])) {
                                                    String[] p1 = parts[2].split(",");
                                                    while (i > -1) {

                                                        String[] p2 = p1[i].split(" \\* ");
                                                        for (Dish dish : dsh) {
                                                            if (dish.getName().equals(p2[1])) {
                                                                newM.put( dish, Integer.parseInt(p2[0]));
                                                                i = i + 1;
                                                            } else {
                                                                i = -1;
                                                            }
                                                        }

                                                    }
                                                  Order o=  new Order(us, newM);
                                                    ord.add(o);
                                                }

                                            }
                                    } else {
                                        if (current.contains("STOCK")) {
                                            String[] parts = current.split(":");

                                                for (Dish dish : dsh) {
                                                    if (dish.getName().equals(parts[1])) {
                                                       st.setStockDish(dish, Integer.parseInt(parts[2]));
                                                    }


                                                }
                                                for (Ingredient ingre : ingr) {
                                                    if (ingre.getName().equals(parts[1])) {
                                                        st.setStockIngredients(ingre, Integer.parseInt(parts[2]));
                                                    }
                                                }


                                        } else {
                                            if (current.contains("STAFF")) {
                                                String[] parts = current.split(":");
                                                     //stM=new StockManagement(st);
                                                     Staff s= new Staff(parts[1],stM);

                                                     stff.add(s);
                                            } else {
                                                if (current.contains("DRONE")) {
                                                    String[] parts = current.split(":");
                                                     //stM=new StockManagement(st);
                                                     Drone d= new Drone(Integer.parseInt(parts[1]),stM,0);

                                                     drn.add(d);

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }


            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<Supplier> getSupplier()
    {
        return sup;
    }

    public List<Ingredient> getIngredient()
    {
        return ingr;
    }

    public List<Dish> getDishes()
    {
        return dsh;
    }

    public List<Staff> getStaff()
    {
        return stff;
    }
    public List<Drone> getDrone()
    {
        return drn;
    }
    public Stock getStocks()
    {
        return st;
    }
    public List<User> getUsers()
    {
        return user;
    }
    public List<Postcode> getPost()
    {
        return pass;
    }
    public List<Order> getOrders()
    {
        return ord;
    }


}
