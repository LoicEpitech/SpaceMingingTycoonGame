package ships;

import entities.Ship;

public class Ship_Stock extends Ship {
    public Ship_Stock() { super(150, 0.9f); }


    @Override
    public float getSpeed() { return capacity + upgradeLevel * 60; }
}
