package common;
import server.Server;

import java.util.*;

public class StockManagement {

    Queue<Dish> preparing;
    Queue<Ingredient> restockingBasic;
    Object notifyStaff, notifyDrones;
    Stock stock;
    public StockManagement(Stock st){

        preparing=new ArrayDeque<Dish>();
        restockingBasic=new ArrayDeque<Ingredient>();
        notifyDrones=new Object();
        notifyStaff=new Object();
        stock=st;
    }
    public void restockDish(Dish dish){
        for(Ingredient ingredient:dish.getRecipeDish().keySet()) {
            if ((int) stock.getStockIngredients().get(ingredient) < (int) dish.getRecipeDish().get(ingredient)) {
                restockIngredient(ingredient);

            }
        }


        if(((int)(stock.getStockDish()).get(dish)-(int)dish.getRestockThresholdDish())<0){
                preparing.add(dish);
        }
    }

    public void restockIngredient(Ingredient ingr) {
        for (Ingredient ingredient : stock.getIngredient()) {
            if (ingr.equals(ingredient)) {
                int i1=(int) stock.getStockIngredients().get(ingredient);
                int i2= (int) ingredient.getRestockThresholdIngredient();
                if (i1 < i2) {
                    if (restockingBasic != null) {
                        if (!restockingBasic.contains(ingredient))
                                restockingBasic.add(ingredient);
                    } else {
                        restockingBasic.add(ingredient);
                    }


                }
            }
        }
    }

        public List<Ingredient> getIngredients()
        {
            return stock.getIngredient();
        }
        public List<Dish> getDishes()
        {
            return stock.getDishes();
        }
        public Stock getStocking()
        {
            return this.stock;
        }

    public synchronized void useDish(Dish dish, int quantity){
        while(quantity>(int)dish.getRestockThresholdDish()){
                restockDish(dish);

        }
        for(Dish dsh:stock.getStockDish().keySet())
        {
            if(dish.getName().equals(dsh.getName()))
            {
                stock.getStockDish().put(dsh,((int)(stock.getStockIngredients()).get(dsh)-quantity));
            }
        }
        notifyAll();
    }

    }

