package common;

import javax.print.attribute.standard.NumberUp;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Dish extends Model implements Serializable{
    String name;
    String description;
    Number price;
    Map<Ingredient,Number> recipe;
    Number restockThreshold;
    Number restockAmount;
    Stock stock;
    StockManagement stockManagement;


    public Dish(String name, String description, Number price, Number restockThreshold, Number restockAmount)
    {
        this.name= name;
        this.description= description;
        this.price= price;
        this.restockThreshold=restockThreshold;
        this.restockAmount=restockAmount;
        this.recipe=new HashMap<>();
    }
    public void addIngr(Ingredient ingr, Number quantity)
    {
        this.recipe.put(ingr,quantity);
        //notifyAll();
    }
    @Override
    public String getName() {
        return this.name;
    }
    public String getDescription() {
        return this.description;
    }
    public Number getPrice() {
        return this.price;
    }
    public Map<Ingredient,Number> getRecipeDish() {
        return this.recipe;
    }
    public Number getRestockThresholdDish()
    {
        return this.restockThreshold;

    }
    public Number getRestockAmountDish()
    {
        return this.restockAmount;
    }


    public void setRecipe(Map<Ingredient,Number> recipe){
        this.recipe=recipe;

    }
    public void setRestockThresholdDish(Number restockLvl){
        this.restockThreshold=restockLvl;
    }
    public void setRestockAmountDish(Number restockAmount){
        this.restockAmount=restockAmount;
    }

    public void removeIngredient(Ingredient ingredient){
        this.recipe.remove(ingredient);
    }


}
