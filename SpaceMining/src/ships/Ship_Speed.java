package ships;

import entities.Ship;

public class Ship_Speed extends Ship {
    public Ship_Speed() { super(70, 1.5f); }

    @Override
    public float getSpeed() { return speed + upgradeLevel * 0.3f; }
}
