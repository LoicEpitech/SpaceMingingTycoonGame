package utils;

import javafx.scene.shape.Rectangle;

public class InteractiveZone {
    public Rectangle rect;
    public Runnable action;

    public InteractiveZone(Rectangle rect, Runnable action) {
        this.rect = rect;
        this.action = action;
    }
}
