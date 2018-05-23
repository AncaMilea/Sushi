package common;

import server.Server;

public class Drone extends Model implements Runnable {
    Number speed;
    StockManagement stockManagement;
    Stock stock;
    Server server=new Server();
    String status;


    public Drone(Number speed, StockManagement stM, int name) {
        this.speed = speed;
        status = "IDLE";
        stockManagement=stM;
        stock=stM.getStocking();
        String strI = Integer.toString(name);
        this.name= strI;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public Number getSpeed() {
        return speed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String sts) {
        this.status = sts;
    }

    public void run() {

        Ingredient ingredientBasic;

        int order = 0;
        while (true) {
            for (Ingredient i : stockManagement.getIngredients()) {
                Integer i1 = (int) i.getRestockThresholdIngredient();
                Integer i2 = (int) (stock.getStockIngredients()).get(i);
                if (i1 > i2) {
                    stockManagement.restockIngredient(i);

                }
            }

            synchronized (stockManagement.getIngredients()) {
                ingredientBasic = stockManagement.restockingBasic.poll();


                if (ingredientBasic == null) {
                    for (Order ord : server.getOrders()) {
                        if (server.getOrderStatus(ord) == "Done") {
                            order++;
                        }
                    }


                            while (ingredientBasic == null && order == 0) {

                                    setStatus("IDLE");
                                ingredientBasic = stockManagement.restockingBasic.poll();

                                if (ingredientBasic == null) {
                                    for (Order ording : server.getOrders()) {
                                        if (server.getOrderStatus(ording) == "Done") {
                                            order++;
                                        }
                                    }
                                }
                            }

                    } else {
                    if (ingredientBasic != null) {
                        try {
                            setStatus("BUSY");
                            Thread.sleep((int) ingredientBasic.getSupplier().getDist()*2000 / (int) speed);


                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        restockBasic(ingredientBasic);
                        setStatus("IDLE");

                    } else {
                        for (Order ord : server.getOrders()) {
                            if (server.getOrderStatus(ord) == "Done") {
                                try {
                                    setStatus("BUSY");
                                    Thread.sleep((int) server.getOrderDistance(ord) * 2000 / (int) speed);

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                setStatus("IDLE");

                            }
                        }
                    }
                }
            }
        }


            }



    public synchronized void restockBasic(Ingredient i)
    {
        int quantity = (int) i.getRestockThresholdIngredient() + (int) i.getRestockAmountIngredient() - (int) stock.getStockIngredients().get(i);
        stockManagement.getStocking().getStockIngredients().put(i,quantity);
        notifyAll();
    }
        }




