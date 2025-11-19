package entities;

import javafx.geometry.Point2D;
import world.Wall;
import javafx.scene.input.KeyCode;
import java.util.Set;

import world.Wall;

public class Player {
    // Physique du player \\
    private Point2D velocity = new Point2D(0, 0);
    private final double acceleration = 1200;
    private final double friction = 0.85;
    private final double maxSpeed = 300;

    private Point2D position;

    private Set<KeyCode> keys;     // inputs

    public Player(float x, float y) {
        position = new Point2D(x, y);
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D newPosition) {
        this.position = newPosition;
    }

    public void setPosition(double x, double y) { this.position = new Point2D(x, y); }


    public void setInputs(Set<KeyCode> keys) { this.keys = keys; }

    public void move(double dt) {
        double dx = 0, dy = 0;

        if (keys.contains(KeyCode.Z)) dy -= 1;
        if (keys.contains(KeyCode.S)) dy += 1;
        if (keys.contains(KeyCode.Q)) dx -= 1;
        if (keys.contains(KeyCode.D)) dx += 1;

        Point2D dir = new Point2D(dx, dy);

        if (dir.magnitude() > 0) {
            dir = dir.normalize();
            velocity = velocity.add(dir.multiply(acceleration * dt));
        } else velocity = velocity.multiply(friction);

        if (velocity.magnitude() > maxSpeed) velocity = velocity.normalize().multiply(maxSpeed);

        Point2D nextPos = position.add(velocity.multiply(dt));

        // Collisions séparées X | Y \\
        if (Wall.getWalls().isInsideMainPolygon(nextPos.getX(), position.getY(), 64, 64)) position = new Point2D(nextPos.getX(), position.getY());
        if (Wall.getWalls().isInsideMainPolygon(position.getX(), nextPos.getY(), 64, 64)) position = new Point2D(position.getX(), nextPos.getY());
    }
}
