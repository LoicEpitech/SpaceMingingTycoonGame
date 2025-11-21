package ui;

import entities.Tool;
import game.GameState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class UpgradeMenu extends GameMenu {

    private final VBox toolsBox = new VBox(20);
    private final GameState gameState;

    public UpgradeMenu(GameState state) {
        super("Upgrade Terminal");
        this.gameState = state;

        this.setPrefSize(960, 1080);
        this.setAlignment(Pos.TOP_CENTER);
        this.setSpacing(30);
        this.setPadding(new Insets(30, 0, 30, 0));
        this.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.2);" +
                        "-fx-border-color: rgba(0,255,255,0.3);" +
                        "-fx-border-width: 2 2 2 0;" +
                        "-fx-effect: dropshadow(gaussian, black, 30, 0.5, 0, 0);"
        );

        ScrollPane scrollPane = new ScrollPane(toolsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setMaxHeight(700);
        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background: transparent;"
        );

        // Assurer que la viewport est transparente
        toolsBox.setStyle("-fx-background-color: transparent;");
        toolsBox.setFillWidth(false);
        toolsBox.setAlignment(Pos.TOP_CENTER);

        this.getChildren().add(scrollPane);

        refresh();
    }

    public void refresh() {
        toolsBox.getChildren().clear();

        for (Tool tool : Tool.getTools()) {
            addToolCard(tool);
        }
    }

    private void addToolCard(Tool tool) {
        boolean owned = gameState.ship.getTools().contains(tool);
        float cost = owned ? (float) (tool.costForNext() * gameState.ship.getPourcentageRemise()) : tool.getPrixAchat()* gameState.ship.getPourcentageRemise();

        Label name = new Label(tool.getName() + (owned ? " (Level: " + tool.getLevel() + ")" : " (Locked)"));
        name.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #00E5FF;" +
                        "-fx-effect: dropshadow(gaussian, #003344, 5, 0.5, 0, 0);"
        );

        Label price = new Label((owned ? "Upgrade cost: " : "Buy cost: ") + cost + " credits");
        price.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        Button actionBtn = new Button(owned ? "Upgrade" : "Buy");
        styleButton(actionBtn, cost);

        actionBtn.setOnAction(e -> {
            if (gameState.credits >= cost) {
                gameState.credits -= cost;
                if (!owned) {
                    gameState.ship.addTool(tool);
                } else {
                    tool.upgrade();
                }
                refresh();
            }
        });

        VBox card = new VBox(10, name, price, actionBtn);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setPrefWidth(760);
        card.setMaxWidth(760);
        card.setStyle(
                "-fx-background-color: rgba(0, 20, 40, 0.6);" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-radius: 15;" +
                        "-fx-border-color: rgba(0, 255, 255, 0.3);" +
                        "-fx-border-width: 1.5;" +
                        "-fx-effect: dropshadow(gaussian, black, 15, 0.3, 0, 0);"
        );

        toolsBox.getChildren().add(card);
    }

    private void styleButton(Button btn, double cost) {
        btn.setStyle("-fx-font-size: 18px; -fx-padding: 8 25; -fx-background-radius: 15;");
        if (gameState.credits < cost) {
            btn.setDisable(true);
            btn.setStyle(
                    "-fx-background-color: #222;" +
                            "-fx-text-fill: gray;" +
                            "-fx-font-size: 18px;" +
                            "-fx-padding: 8 25;" +
                            "-fx-background-radius: 15;"
            );
        } else {
            btn.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #003344, #00AACC);" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 18px;" +
                            "-fx-padding: 8 25;" +
                            "-fx-background-radius: 15;"
            );
        }
    }
}
