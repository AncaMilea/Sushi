package common;

import server.Server;

import java.util.Random;

public class Staff extends Model implements Runnable {

    private String name;
    Stock stock;
    Server server;
    StockManagement stockManagement;
    String status="IDLE";
    private Random r=new Random();
    private int low=2000;
    private int high=6000;

    public Staff(String name,StockManagement stM) {

        this.name = name;
        this.stockManagement=stM;
        this.stock=stM.stock;
    }@Override
    public String getName() {
        return name;
    }
    @Override
    public void run() {
        Dish sushiD;
        Integer quantity=0;

       while(true){
           for(Dish dsh:stockManagement.getDishes())
            {
           Integer i1=(int)dsh.getRestockThresholdDish();
           Integer i2=(int)(stock.getStockDish()).get(dsh);
           if(i1>i2) {
               stockManagement.restockDish(dsh);
               quantity = (int) dsh.getRestockThresholdDish() + (int) dsh.getRestockAmountDish() - (int) stock.getStockDish().get(dsh);
            }

            } synchronized (stock.getDishes()) {
               sushiD = stockManagement.preparing.poll();
               if (sushiD == null) {

                   while (sushiD == null) {
                       sushiD = stockManagement.preparing.poll();
                   }

               }else{
                   quantity = (int) sushiD.getRestockThresholdDish() + (int) sushiD.getRestockAmountDish() - (int) (stock.getStockDish()).get(sushiD);
               }

               while(quantity != 0) {
                   cooking(sushiD);
                   setStatus("BUSY");
                   quantity--;
               }
           }
            setStatus("IDLE");

        }

    }
    public void cooking(Dish d){
        for(Ingredient ingredient:d.getRecipeDish().keySet()){
            int i1=(int)d.getRecipeDish().get(ingredient);
            int i2=(int)(stock.getStockIngredients()).get(ingredient);
            if(i1>i2){
                    stockManagement.restockDish(d);
                setStatus("BUSY");

            }else {
                useIngredient(ingredient,(int) d.getRecipeDish().get(ingredient));
                setStatus("BUSY");
            }
        }
        try{
            Thread.sleep(this.getTime());
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        stockManagement.getStocking().getStockDish().put(d,(int)(stock.getStockDish()).get(d)+1);
    }
    public synchronized void useIngredient(Ingredient i,int quantity){
        while(quantity>(int)(stock.getStockIngredients()).get(i)){
            stockManagement.restockIngredient(i);


        }
        for(Ingredient ing:stock.getStockIngredients().keySet())
        {
            if(i.getName().equals(ing.getName()))
            {

                (stockManagement.getStocking().getStockIngredients()).put(ing,(int)(stock.getStockIngredients()).get(ing)-quantity);
            }
        }
        notifyAll();
    }
    public int getTime(){
        int result =r.nextInt(high-low)+low;
        return result;
    }
    public String getStatus()
    {
        return status;
    }
    public void setStatus(String sts){this.status=sts;}


}

