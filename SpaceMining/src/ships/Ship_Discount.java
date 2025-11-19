package ships;

import entities.Ship;

public class Ship_Discount extends Ship {

    public Ship_Discount() { super(100, 0.8f); }

    @Override
    public double upgradeCost() {
        return 250 * Math.pow(2, upgradeLevel);
    }
}
