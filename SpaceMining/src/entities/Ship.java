package entities;

import game.GameState;

import java.util.*;

public abstract class Ship {
    //region --- Champs ---
    public int capacity;
    public float speed;
    public int upgradeLevel = 1;
    private float currentWeight = 0;
    private final Map<Ressource, Integer> inventory = new HashMap<>();
    private List<Tool> tools = new ArrayList<>();
    //endregion


    public Ship(int capacity, float speed) {
        this.capacity = capacity;
        this.speed = speed;
    }

    public int getCapacity() { return capacity + upgradeLevel * 40; }
    public double upgradeCost() { return 300 * Math.pow(2, upgradeLevel); }
    public float getSpeed() { return speed + upgradeLevel * 0.9f; }

    public String getType() {
        return switch (this.getClass().getSimpleName()) {
            case "SpeedShip" -> "speed";
            case "StockShip" -> "stock";
            case "DiscountShip" -> "discount";
            default -> "unknown";
        };
    }
    public Map<Ressource, Integer> getInventory() {
        return this.inventory;
    }

    public float getCurrentWeight() { return this.currentWeight; }

    public List<Tool> getTools() { return List.copyOf(tools); }

    public boolean addTool(Tool t) {
        if (t == null || tools.contains(t)) return false;
        tools.add(t);
        return true;
    }

    public boolean addRessource(Ressource r, int quantity) {
        if (quantity > 10) return false;
        if (r == null || quantity <= 0) return false;
        if (this.currentWeight >= this.capacity && (this.currentWeight + r.weight * quantity) > this.getCapacity()) return false;
        this.currentWeight += r.weight * quantity;
        inventory.put(r, inventory.getOrDefault(r, 0) + quantity);
        return true;
    }

    public boolean removeRessource(Ressource r, int quantity) {
        if (r == null || quantity <= 0) return false;
        if (!inventory.containsKey(r)) return false;
        this.currentWeight -= r.weight * quantity;
        inventory.put(r, Math.max(0, inventory.getOrDefault(r, 0) - quantity));
        return true;
    }

    public void sellAll(GameState state) {
        Ressource r;
        int quantity;
        for(Iterator var1 = this.getInventory().entrySet().iterator(); var1.hasNext(); state.credits += (double)((float)quantity * r.getPriceMax())) {
            Map.Entry<Ressource, Integer> entry = (Map.Entry)var1.next();
            r = (Ressource)entry.getKey();
            quantity = (Integer)entry.getValue();
        }

        this.clearInventory();
    }

    public void clearInventory() { inventory.clear(); }
}
