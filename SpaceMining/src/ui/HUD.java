package ui;

import entities.Ressource;
import entities.Tool;
import game.GameState;
import javafx.animation.FadeTransition;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.*;
import world.planet.Planet;
import static game.GameState.currentState;

public class HUD {

    private static HUD currentHud;

    public static HUD getHud() { return currentHud; }

    public static void setHud(HUD hud) { HUD.currentHud = hud; }

    public static void staticStartMining() {
        if (currentHud != null) currentHud.startMining();
    }

    private static final Color COLOR_TEXT = Color.rgb(180, 220, 255);
    private static final Color COLOR_FRAME = Color.rgb(60, 130, 255, 0.4);
    private static final Color COLOR_BG = Color.rgb(5, 15, 30, 0.55);
    private static final Font FONT_TITLE = Font.font("Consolas", 18);
    private static final Font FONT_NORMAL = Font.font("Consolas", 14);
    private static final Font FONT_SMALL = Font.font("Consolas", 12);

    private static class Notification {
        String text;
        long startTime;
        final long duration = 3000;
        float opacity = 1f;

        Notification(String t) {
            this.text = t;
            this.startTime = System.currentTimeMillis();
        }
    }

    private final List<Notification> notifications = new ArrayList<>();


    private boolean mining = false;
    private float miningProgress = 0;

    private long gameStartTime = System.currentTimeMillis();


    public void addNotification(String msg) {
        notifications.add(new Notification(msg));
    }

    public void startMining() {
        mining = true;
        miningProgress = 0;
    }

    public void updateMiningProgress(float p) {
        miningProgress = Math.max(0, Math.min(1, p));
        if (miningProgress >= 1) mining = false;
    }

    public void stopMining() {
        mining = false;
        miningProgress = 0;
    }

    public void render(GraphicsContext gc) {
        renderInfoPanel(gc);
        renderTools(gc);
        renderInventory(gc);
        renderNotifications(gc);
        renderMiningBar(gc);
    }

    private void renderInfoPanel(GraphicsContext gc) {
        gc.setFill(COLOR_BG);
        gc.fillRoundRect(15, 15, 420, 140, 15, 15);

        gc.setStroke(COLOR_FRAME);
        gc.strokeRoundRect(15, 15, 420, 140, 15, 15);

        gc.setFont(FONT_TITLE);
        gc.setFill(COLOR_TEXT);

        gc.fillText("⟢ SYSTEM STATUS", 30, 40);

        gc.setFont(FONT_NORMAL);
        gc.fillText("Planet : " + currentState.currentPlanet.getName(), 30, 70);
        gc.fillText("Player level : " + currentState.researchLevel, 30, 95);

        //long timePlayedSec = (System.currentTimeMillis() - gameStartTime) / 1000;
        //gc.fillText("Playtime : " + timePlayedSec + "s", 30, 120);

        gc.fillText("Credits : " + (int) currentState.credits, 250, 70);

        float w = currentState.ship.getCurrentWeight();
        float max = currentState.ship.getCapacity();
        gc.fillText("Cargo : " + w + " / " + max, 250, 95);
    }

    private void renderTools(GraphicsContext gc) {

        List<Tool> tools = currentState.ship.getTools();

        // Calcul dynamique de la hauteur
        int baseHeight = 60;    // marge + header
        int lineHeight = 22;    // hauteur par outil
        int itemCount = Math.max(1, tools.size());
        int dynamicHeight = baseHeight + (itemCount * lineHeight) + 20; // padding bas

        double x = 470;
        double y = 15;
        double w = 300;
        double h = dynamicHeight;

        // Fond du panneau
        gc.setFill(COLOR_BG);
        gc.fillRoundRect(x, y, w, h, 15, 15);

        // Bordure
        gc.setStroke(COLOR_FRAME);
        gc.strokeRoundRect(x, y, w, h, 15, 15);

        // Titre
        gc.setFont(FONT_TITLE);
        gc.setFill(COLOR_TEXT);
        gc.fillText("🛠 TOOLS", x + 20, y + 30);

        // Contenu
        gc.setFont(FONT_NORMAL);
        int drawY = (int) (y + 65);

        if (tools.isEmpty()) {
            gc.fillText("(none)", x + 20, drawY);
            return;
        }

        for (Tool t : tools) {
            gc.fillText("- " + t.getName() + " [LVL " + t.getLevel() + "]",
                    x + 20, drawY);
            drawY += lineHeight;
        }
    }

    private void renderInventory(GraphicsContext gc) {

        Map<Ressource, Integer> inv = currentState.ship.getInventory();

        // Calcul dynamique de la hauteur
        int baseHeight = 80;     // espace pour le header + marge
        int lineHeight = 22;     // hauteur par ligne d'item
        int itemCount = Math.max(1, inv.size());  // min 1 pour "(empty)"
        int dynamicHeight = baseHeight + (itemCount * lineHeight) + 20; // padding bas

        double x = 1550;
        double y = 15;
        double w = 350;
        double h = dynamicHeight;

        // Fond dynamique
        gc.setFill(COLOR_BG);
        gc.fillRoundRect(x, y, w, h, 15, 15);

        gc.setStroke(COLOR_FRAME);
        gc.strokeRoundRect(x, y, w, h, 15, 15);

        // Titre
        gc.setFont(FONT_TITLE);
        gc.setFill(COLOR_TEXT);
        gc.fillText("CARGO HOLD", x + 20, y + 30);

        // Contenu
        gc.setFont(FONT_NORMAL);
        int drawY = (int) (y + 65);

        if (inv.isEmpty()) {
            gc.fillText("(empty)", x + 20, drawY);
            return;
        }

        for (Map.Entry<Ressource, Integer> e : inv.entrySet()) {
            gc.fillText(e.getKey().getName() + " : " + e.getValue(), x + 20, drawY);
            drawY += lineHeight;
        }
    }


    private void renderNotifications(GraphicsContext gc) {

        int y = 850;
        Iterator<Notification> iter = notifications.iterator();

        while (iter.hasNext()) {
            Notification n = iter.next();
            long alive = System.currentTimeMillis() - n.startTime;

            if (alive > n.duration) {
                iter.remove();
                continue;
            }

            float alpha = 1f;
            if (alive > n.duration - 800) {
                alpha = (n.duration - alive) / 800f;
            }

            // Mesure automatique du texte
            javafx.scene.text.Text textMeasure = new javafx.scene.text.Text(n.text);
            textMeasure.setFont(FONT_NORMAL);
            double textWidth = textMeasure.getLayoutBounds().getWidth();

            double padding = 20;
            double boxWidth = textWidth + padding * 2;
            double boxHeight = 30;

            double x = 20;
            double boxY = y - 20;

            gc.setGlobalAlpha(alpha);

            // Rectangle auto-largeur
            gc.setFill(Color.rgb(20, 60, 130, 0.7));
            gc.fillRoundRect(x, boxY, boxWidth, boxHeight, 10, 10);

            gc.setFill(Color.WHITE);
            gc.setFont(FONT_NORMAL);
            gc.fillText(n.text, x + padding, y);

            gc.setGlobalAlpha(1);
            y -= 40;
        }
    }


    private void renderMiningBar(GraphicsContext gc) {
        if (!mining) return;

        double x = 600;
        double y = 950;
        double w = 700;
        double h = 20;

        gc.setFill(Color.rgb(40, 80, 150, 0.4));
        gc.fillRoundRect(x, y, w, h, 10, 10);

        gc.setFill(Color.CYAN);
        gc.fillRoundRect(x, y, w * miningProgress, h, 10, 10);

        gc.setStroke(Color.WHITE);
        gc.strokeRoundRect(x, y, w, h, 10, 10);

        gc.setFont(FONT_NORMAL);
        gc.setFill(Color.WHITE);
        gc.fillText("Mining " + (int) (miningProgress * 100) + "%", x, y - 8);
    }
}