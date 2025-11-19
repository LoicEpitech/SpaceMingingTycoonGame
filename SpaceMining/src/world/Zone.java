package world;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import screens.GameScene;

import java.util.ArrayList;
import java.util.List;
import entities.Player;
import javafx.scene.canvas.GraphicsContext;
import java.util.Random;
import world.ZoneView;

public class Zone  {
    public enum ZoneType { PILOT, HUBLOT, DOOR }
    private static final ArrayList<Zone> zones = new ArrayList<>();

    private String name;
    private Double x, y, w, h;
    private ZoneType type;
    private String icon = "✦"; // ✦ ◎ ⚙ ⌘ ⬒

    public Zone(String name, Double x, Double y, Double w, Double h, ZoneType type, String icon){
        this(name, x, y, w, h, type);
        this.icon = icon;
    }

    public Zone(String name, Double x, Double y, Double w, Double h, ZoneType type){
        this.name = name;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.type = type;
        zones.add(this);
    }

    public String getName(){ return name; }
    public ZoneType getType(){ return type; }
    public String getIcon(){ return icon; }
    public Double getX(){ return x; }
    public Double getY(){ return y; }
    public Double getWidth(){ return w; }
    public Double getHeight(){ return h; }

    public static List<Zone> getZones(){ return zones; }

    public void setHoveredZone() { ZoneView.setHoveredZone(this); }

    public boolean contains(Double px, Double py){
        return px >= x && px <= x+w && py >= y && py <= y+h;
    }

    public  static void checkPlayerHoverZone(Double playerPositionX, Double playerPositionY) {
        for (Zone z : zones) {
            ZoneView.setHoveredZone(); // reset \\
            if (z.contains(playerPositionX, playerPositionY)) {
                z.setHoveredZone();
                break;
            }
        }
    }



}