package game;

import entities.*;
import javafx.scene.input.KeyCode;
import ships.Ship_Speed;
import ships.Ship_Stock;
import ships.Ship_Discount;
import world.planet.Planet;

import javax.swing.text.JTextComponent;
import java.util.*;

public class GameState {

    public static final boolean DEBUG = false;
    public static GameState currentState = new GameState();

    public Map<String, KeyCode> keyBindings = new HashMap<>();

    // Ressources disponibles
    public List<Ressource> mineralList = new ArrayList<>(Arrays.asList(
            Mineral.GOLD,
            Mineral.DIAMOND,
            Mineral.ORICHALCUM
    ));

    public double credits = 100;
    public Ship ship = new Ship_Speed();
    public int researchLevel = 1;
    public Planet currentPlanet;

    // Gestion des vaisseaux disponibles
    public void loadShip(String type) {
        switch (type.toLowerCase()) {
            case "stock": ship = new Ship_Stock(); break;
            case "discount": ship = new Ship_Discount(); break;
            default: ship = new Ship_Speed();
        }
    }

    // Style d'intérieur du vaisseau sélectionné
    public enum ShipInteriorStyle { CLASSIC, INDUSTRIAL, FUTURISTIC }
    public ShipInteriorStyle interiorStyle = ShipInteriorStyle.CLASSIC;

    public GameState() {
        // Key bindings par défaut (modifiables en jeu + sauvegardables)
        keyBindings.put("UP", KeyCode.Z);
        keyBindings.put("DOWN", KeyCode.S);
        keyBindings.put("LEFT", KeyCode.Q);
        keyBindings.put("RIGHT", KeyCode.D);
        keyBindings.put("INTERACT", KeyCode.E);
        keyBindings.put("MENU", KeyCode.ESCAPE);
        keyBindings.put("SAVE", KeyCode.F2);
    }

    public static GameState getCurrentState() { return currentState; }
}
