package game;
import entities.*;
import ships.Ship_Speed;
import entities.Tool;
import ships.Ship_Stock;
import ships.Ship_Discount;
import world.planet.Planet;

import java.util.*;

public class GameState {
    public static final boolean DEBUG = true;
    public static GameState currentState = new GameState();

    public List<Ressource> mineralList = new ArrayList<>(Arrays.asList(
            Mineral.GOLD,
            Mineral.DIAMOND,
            Mineral.ORICHALCUM
    ));

    public double credits = 100;
    public Ship ship = new Ship_Speed();
    public Map<Ressource, Integer> storage = new HashMap<>();
    public List<Tool> tools = new ArrayList<>();
    public int researchLevel = 1;
    public Planet currentPlanet;

    public void loadShip(String type) {
        switch (type.toLowerCase()) {
            case "speed": ship = new Ship_Speed(); break;
            case "stock": ship = new Ship_Stock(); break;
            case "discount": ship = new Ship_Discount(); break;
            default: ship = new Ship_Speed();
        }
    }
    public GameState() {
        for (Ressource m : mineralList) {
            storage.put(m, 0);
        }
        tools.add(new Drill());
    }

    // Getters Static \\
    public static  GameState getCurrentState() { return currentState; }
}
