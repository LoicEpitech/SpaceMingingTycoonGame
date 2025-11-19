package entities;

public abstract class Tool {
    public String name;
    public int level = 1;
    private int baseCost = 1000;

    public Tool(String name) { this.name = name; }
    public double speedMultiplier() { return 1.0 + this.level * 0.5; }
    public double costForNext() { return baseCost * Math.pow(2, this.level); }
    public String getName() { return this.name; }
    public int getLevel() { return this.level; }
    public void upgrade() { this.level += 1; }
}
