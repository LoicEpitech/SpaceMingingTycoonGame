package world;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import entities.Player;
import javafx.scene.canvas.GraphicsContext;
import java.util.Random;
import static game.GameState.DEBUG;

public class ZoneView {
    private static double pulseTimer = 0;
    private static Random rnd = new Random();
    private static Zone hoveredZone = null;

    public static Zone getHoveredZone() { return hoveredZone; }

    public static void setHoveredZone() { hoveredZone = null; }
    public static void setHoveredZone(Zone z) { hoveredZone = z; }

    public static void render(GraphicsContext gc, Player player) {
        renderZoneBox(gc);
        renderZoneEffects(1/60.0, gc, player);   // GESTION VISUELLE DES ZONE \\
    }

    private static void renderZoneBox(GraphicsContext gc) {
        if (!DEBUG) return;
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(2);
        for (Zone z : Zone.getZones()) {
            gc.strokeRect(z.getX(), z.getY(), z.getWidth(), z.getHeight());
        }

        if (getHoveredZone() != null) {
            gc.setStroke(Color.BLUE);
            gc.setLineWidth(2);
            gc.strokeRect(hoveredZone.getX(), hoveredZone.getY(), hoveredZone.getWidth(), hoveredZone.getHeight());
        }
    }

    private static void renderZoneEffects(double dt, GraphicsContext gc, Player player) { renderZoneEffects(dt, gc, player, false); }
    private static void renderZoneEffects(double dt, GraphicsContext gc, Player player, boolean activeParticuleEffect) {
        pulseTimer += dt;
        for (Zone z : Zone.getZones()) {
            double centerX = z.getX() + z.getWidth() / 2;
            double centerY = z.getY() + z.getHeight() / 2;

            // Halo \\
            double pulse = 0.5 + Math.sin(pulseTimer * 2) * 0.5;  // 0 - 1 \\
            double haloRadius = Math.max(z.getWidth(), z.getHeight()) * 0.9; // Taille du halo \\

            gc.setFill(Color.color(1, 1, 0, 0.10 * pulse)); // Couleur halo
            gc.fillOval(centerX - haloRadius / 2, centerY - haloRadius / 2, haloRadius, haloRadius);

            // Icon \\
            gc.setFill(Color.color(1, 1, 0, 0.8));
            gc.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 26));
            gc.fillText(z.getIcon(), centerX - 10, centerY + 10);

            // Particule effet si le joueur est trop loin \\
            if (!activeParticuleEffect) continue;
            double dist = player.getPosition().distance(centerX, centerY);

            if (dist > 200) drawGuidingParticles(centerX, centerY, dist, gc);
        }
    }

    private static void drawGuidingParticles(double zx, double zy, double dist, GraphicsContext gc) {
        int count = 10;

        for (int i = 0; i < count; i++) {
            double angle = rnd.nextDouble() * Math.PI * 2;
            double radius = 80 + rnd.nextDouble() * 40;

            double px = zx + Math.cos(angle) * radius;
            double py = zy + Math.sin(angle) * radius;

            double dx = (zx - px) * 0.07;
            double dy = (zy - py) * 0.07;

            // Couleur \\
            gc.setFill(Color.color(1, 1, 0, 0.4));
            gc.fillOval(px + dx, py + dy, 4, 4);
        }
    }
}