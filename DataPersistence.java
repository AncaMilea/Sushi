package server;

import common.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

public class DataPersistence extends Thread {
    Server server;
    Stock stock;
    public DataPersistence(Server s){
    this.server=s;
    this.stock=s.stk;
    }

    @Override
    public void run() {
    while(!Thread.currentThread().isInterrupted())
    {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {

            BufferedWriter writeOut = new BufferedWriter(new FileWriter("fileout.txt"));

            for (Supplier spl : server.getSuppliers()) {
                String joinedString = String.join(":", "SUPPLIER", spl.getName(), spl.getDist().toString());
                writeOut.write(joinedString);
                writeOut.newLine();
            }
            writeOut.newLine();
            for (Ingredient ing : server.getIngredients()) {
                String joinedString = String.join(":", "INGREDIENT", ing.getName(), ing.getUnit(), ing.getSupplier().getName(), ing.getRestockThresholdIngredient().toString(), ing.getRestockAmountIngredient().toString());
                writeOut.write(joinedString);
                writeOut.newLine();
            }
            writeOut.newLine();
            for (Dish dsh : server.getDishes()) {
                List<String> strings = new LinkedList<>();
                for (Ingredient ing : dsh.getRecipeDish().keySet()) {
                    String miniString = String.join(" * ", dsh.getRecipeDish().get(ing).toString(), ing.getName());
                    strings.add(miniString);

                }
                String recipe = String.join(",", strings);
                String joinedString = String.join(":", "DISH", dsh.getName(), dsh.getDescription(), dsh.getPrice().toString(), dsh.getRestockThresholdDish().toString(), dsh.getRestockAmountDish().toString(), recipe);
                writeOut.write(joinedString);
                writeOut.newLine();
            }
            writeOut.newLine();

            for (Postcode pst : server.getPostcodes()) {
                String joinedString = String.join(":", "POSTCODE", pst.getName(), pst.getDistance().toString());
                writeOut.write(joinedString);
                writeOut.newLine();
            }
            writeOut.newLine();

            for (User usr : server.getUsers()) {
                String joinedString = String.join(":", "USER", usr.getName(), usr.getPassword(), usr.getLocation(), usr.getPostcode().getName());
                writeOut.write(joinedString);
                writeOut.newLine();
            }
            writeOut.newLine();

            for (Order ord : server.getOrders()) {
                for (User usr : server.getUsers()) {
                    if (usr.getName() == ord.getUser().getName()) {
                        List<String> strings = new LinkedList<>();
                        for (Dish dsh : ord.getOrder().keySet()) {
                            String miniString = String.join(" * ", ord.getOrder().get(dsh).toString(), dsh.getName());
                            strings.add(miniString);

                        }
                        String ordering = String.join(",", strings);
                        String joinedString = String.join(":", "ORDER", usr.getName(), ordering);
                        writeOut.write(joinedString);
                        writeOut.newLine();
                    }
                }
            }
            writeOut.newLine();

            for (Dish d : stock.getStockDish().keySet()) {
                String joinedString = String.join(":", "STOCK", d.getName(), stock.getStockDish().get(d).toString());
                writeOut.write(joinedString);
                writeOut.newLine();
            }
            writeOut.newLine();

            for (Ingredient i : stock.getStockIngredients().keySet()) {
                String joinedString = String.join(":", "STOCK", i.getName(), stock.getStockIngredients().get(i).toString());
                writeOut.write(joinedString);
                writeOut.newLine();
            }
            writeOut.newLine();

            for(Staff st:server.getStaff())
            {
                String joinedString = String.join(":","STAFF",st.getName().toString());
                writeOut.write(joinedString);
                writeOut.newLine();
            }

            for (Drone dr : server.getDrones()) {
                String joinedString = String.join(":", "DRONE", dr.getSpeed().toString());
                writeOut.write(joinedString);
                writeOut.newLine();
            }
            writeOut.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    }
}
