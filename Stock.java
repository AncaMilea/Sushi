package common;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Stock  {
    Map<Dish,Number> dish=new ConcurrentHashMap<>();
    Map<Ingredient, Number> ingr=new ConcurrentHashMap<>();
    List<Dish> dsh= new ArrayList<>();
    List<Supplier> spl= new ArrayList<>();
    List<Ingredient> ing= new ArrayList<>();
    public Stock(){
    }
    public void StockDish(Dish dish, Number quantity)
    {
        this.dsh.add(dish);
        this.dish.put(dish,quantity);
    }
    public void StockIngredient(Ingredient ingre, Number quantity)
    {
        this.ing.add(ingre);
        this.ingr.put(ingre,quantity);
    }

    public Stock getStock()
    {
        return this;
    }
    public Map<Dish,Number> getStockDish()
    {
        return dish;

    }
    public Map<Ingredient,Number> getStockIngredients()
    {
        return ingr;
    }
    public void setStockDish(Dish disher, Number q){

        if(dsh.size()!=0) {
            for (Dish dishSushi : dsh) {
                if (!dishSushi.equals(disher)) {
                    this.dsh.add(disher);
                    break;
                }else{
               break;}
            }
        }else{
            this.dsh.add(disher);
        }
        this.dish.put(disher,q);
    }
    public void setStockIngredients(Ingredient inge, Number q)
    {
        if(ing.size()!=0) {
            for (Ingredient i : ing) {
                if (!i.equals(inge)){
                    this.ing.add(inge);
                        break;}
                        else
                {
                    break;
                }

            }
        }else{
            this.ing.add(inge);
        }
        this.ingr.put(inge,q);
    }

    public List<Dish> getDishes()
    {
        return dsh;
    }
    public List<Ingredient> getIngredient()
    {
        return ing;
    }



}
