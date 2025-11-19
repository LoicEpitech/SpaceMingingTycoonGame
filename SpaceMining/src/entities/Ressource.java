package entities;

public class Ressource {
    //region Champs
    private static int count = 0;
    protected final int id = count++;
    protected String name;
    protected float priceMin;
    protected float priceMax;
    protected float weight;
    protected String necessaryTool;
    protected float wearingImpact;
    //endregion
    //region Constructeur

    public Ressource(String name){
        this.name = name;
    }
    public Ressource(String name,float priceMin,float priceMax){
        this(name);
        this.priceMin = priceMin;
        this.priceMax = priceMax;
    }
    public Ressource(String name,float priceMin,float priceMax,float weight){
        this(name, priceMin, priceMax);
        this.weight = weight;
    }
    public Ressource(String name,float priceMin,float priceMax,float weight,String necessaryTool){
        this(name, priceMin, priceMax, weight);
        this.necessaryTool = necessaryTool;
    }
    public Ressource(String name,float priceMin,float priceMax,float weight,String necessaryTool,float wearingImpact){
        this(name, priceMin, priceMax, weight, necessaryTool);
        this.wearingImpact = wearingImpact;
    }
    //endregion

    //region getteur setteur
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public float getPriceMin(){
        return priceMin;
    }
    public void setPriceMin(float priceMin){
        this.priceMin = priceMin;
    }
    public float getPriceMax(){
        return priceMax;
    }
    public void setPriceMax(float priceMax){
        this.priceMax = priceMax;
    }
    public String getPrice(){
        return "Minimum price = "+ priceMin + ", Maximum price = " + priceMax;
    }
    public float getWeight(){
        return weight;
    }
    public void setWeight(float weight){
        this.weight = weight;
    }
    public String getNecessaryTool(){
        return necessaryTool;
    }
    public void setNecessaryTool(String necessaryTool){
        this.necessaryTool = necessaryTool;
    }
    public float getWearingImpact(){
        return wearingImpact;
    }
    public void setWearingImpact(float wearingImpact){
        this.wearingImpact = wearingImpact;
    }
    //endregion




}
