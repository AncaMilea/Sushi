package common;

public class Ingredient extends Model {
    String name;
    String unit;
    Supplier s;
    Number restockThreshold;
    Number restockAmount;

    public Ingredient(String name, String unit, Supplier s, Number restockThreshold, Number restockAmount)
    {
        this.name= name;
        this.unit= unit;
        this.s= s;
        this.restockThreshold=restockThreshold;
        this.restockAmount=restockAmount;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getUnit()
    {
        return unit;
    }

    public Supplier getSupplier()
    {
        return s;
    }

    public Number getRestockThresholdIngredient()
    {
        return restockThreshold;
    }

    public Number getRestockAmountIngredient()
    {
        return restockAmount;
    }

    public void setRestockThresholdIngredient(Number restockLvl)
    {
        notifyUpdate("restock level",this.restockThreshold,restockLvl);
        this.restockThreshold=restockLvl;
    }
    public void setRestockAmountIngredient(Number restockAmount)
    {
        notifyUpdate("restock level",this.restockAmount,restockAmount);
        this.restockAmount=restockAmount;
    }



}
