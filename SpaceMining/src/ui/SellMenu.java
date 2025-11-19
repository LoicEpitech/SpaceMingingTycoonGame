package ui;

import entities.Ressource;
import entities.Ship;
import game.GameState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.Map;

public class SellMenu extends GameMenu {
    private UpgradeMenu upgradeMenu;
    private final VBox itemsBox = new VBox(20);

    public SellMenu(Ship currentShip, GameState gameState, UpgradeMenu upgradeMenu) {
        super("Sell Terminal");
        this.upgradeMenu = upgradeMenu;

        this.setPrefSize(960, 1080);
        this.setAlignment(Pos.TOP_CENTER);
        this.setSpacing(30);
        this.setPadding(new Insets(30, 0, 30, 0));
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2);"
                + "-fx-border-color: rgba(0,255,255,0.25);"
                + "-fx-border-width: 2 1 2 2;"
                + "-fx-effect: dropshadow(gaussian, black, 30, 0.5, 0, 0);");

        ScrollPane scrollPane = new ScrollPane(itemsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setMaxHeight(700);
        scrollPane.setStyle("-fx-background: transparent;"
                + "-fx-background-color: transparent;"
                + "-fx-padding: 10;");

        itemsBox.setFillWidth(false);
        itemsBox.setAlignment(Pos.TOP_CENTER);

        this.getChildren().add(scrollPane);

        Button sellAll = new Button("Sell All");
        sellAll.setStyle("-fx-font-size: 20px;"
                + "-fx-padding: 10 40;"
                + "-fx-background-color: linear-gradient(to bottom, #330000, #660000);"
                + "-fx-text-fill: white;"
                + "-fx-background-radius: 20;"
                + "-fx-effect: dropshadow(gaussian, black, 10, 0.4, 0, 0);");

        sellAll.setOnAction(e -> {
            gameState.ship.sellAll(gameState);
            refresh(currentShip, gameState);
            if (upgradeMenu != null) upgradeMenu.refresh(gameState, gameState.tools);
        });

        this.getChildren().add(sellAll);

        refresh(currentShip, gameState);
    }

    public void refresh(Ship currentShip, GameState state) {
        itemsBox.getChildren().clear();

        if (countAllRessourcesSelled(state.ship) >= 50 && state.researchLevel < 3)
            state.researchLevel++;

        if (countAllRessourcesSelled(state.ship) >= 200 && state.researchLevel < 4)
            state.researchLevel++;

        Map<Ressource, Integer> inventory = currentShip.getInventory();

        if (inventory.isEmpty()) {
            Label empty = new Label("No resources to sell.");
            empty.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
            itemsBox.getChildren().add(empty);
            return;
        }

        for (Map.Entry<Ressource, Integer> entry : inventory.entrySet()) {
            addRessourceItem(entry.getKey(), entry.getValue(), currentShip, state);
        }
    }

    private void addRessourceItem(Ressource r, int quantity, Ship ship, GameState state) {
        Label label = new Label(r.getName() + " ×" + quantity + "  |  Cost: " + r.getPriceMax() + " credits/unit");
        label.setStyle("-fx-font-size: 20px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #00E5FF;"
                + "-fx-effect: dropshadow(gaussian, #003344, 5, 0.5, 0, 0);");

        Button sellBtn = new Button("Sell");
        sellBtn.setStyle("-fx-font-size: 16px;"
                + "-fx-padding: 8 25;"
                + "-fx-background-color: linear-gradient(to bottom, #003344, #00AACC);"
                + "-fx-text-fill: white;"
                + "-fx-background-radius: 15;"
                + "-fx-effect: dropshadow(gaussian, #003344, 8, 0.4, 0, 1);");

        sellBtn.setOnAction(e -> {
            state.credits += quantity * r.getPriceMax();
            ship.removeRessource(r, quantity);
            refresh(ship, state);
            if (upgradeMenu != null) upgradeMenu.refresh(state, state.tools);
        });

        VBox card = new VBox(10, label, sellBtn);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20, 20, 25, 20));
        card.setPrefWidth(760);
        card.setMaxWidth(760);
        card.setStyle("-fx-background-color: rgba(0, 20, 40, 0.6);"
                + "-fx-background-radius: 15;"
                + "-fx-border-radius: 15;"
                + "-fx-border-color: rgba(0, 255, 255, 0.3);"
                + "-fx-border-width: 1.5;"
                + "-fx-effect: dropshadow(gaussian, black, 15, 0.3, 0, 0);");

        itemsBox.getChildren().add(card);
    }

    public static int countAllRessourcesSelled(Ship ship) {
        int total = 0;
        for (Map.Entry<Ressource, Integer> entry : ship.getInventory().entrySet()) {
            total += entry.getValue();
        }
        return total;
    }
}
