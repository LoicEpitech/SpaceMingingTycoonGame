package ui;

import entities.Tool;
import game.GameState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class UpgradeMenu extends GameMenu {

    private final VBox toolsBox = new VBox(20);

    public UpgradeMenu(GameState state, List<Tool> tools) {
        super("Upgrade Terminal");

        this.setPrefSize(960, 1080);
        this.setAlignment(Pos.TOP_CENTER);
        this.setSpacing(30);
        this.setPadding(new Insets(30, 0, 30, 0));
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2);"
                + "-fx-border-color: rgba(0,255,255,0.3);"
                + "-fx-border-width: 2 2 2 0;"
                + "-fx-effect: dropshadow(gaussian, black, 30, 0.5, 0, 0);");

        ScrollPane scrollPane = new ScrollPane(toolsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setMaxHeight(700);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        toolsBox.setFillWidth(false);
        toolsBox.setAlignment(Pos.TOP_CENTER);

        this.getChildren().add(scrollPane);

        refresh(state, tools);
    }

    public void refresh(GameState state, List<Tool> tools) {
        toolsBox.getChildren().clear();

        for (Tool tool : tools) {
            addToolCard(tool, state);
        }
    }

    private void addToolCard(Tool tool, GameState state) {

        Label name = new Label(tool.getName() + "  (Level: " + tool.getLevel() + ")");
        name.setStyle("-fx-font-size: 22px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #00E5FF;"
                + "-fx-effect: dropshadow(gaussian, #003344, 5, 0.5, 0, 0);");

        double cost = tool.costForNext();
        Label price = new Label("Cost: " + cost + " credits");
        price.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        Button upgradeBtn = new Button("Upgrade");
        upgradeBtn.setStyle("-fx-font-size: 18px;"
                + "-fx-padding: 8 25;"
                + "-fx-background-color: linear-gradient(to bottom, #003344, #00AACC);"
                + "-fx-text-fill: white;"
                + "-fx-background-radius: 15;"
                + "-fx-effect: dropshadow(gaussian, #003344, 8, 0.4, 0, 1);");

        if (state.credits < cost) {
            upgradeBtn.setDisable(true);
            upgradeBtn.setStyle("-fx-font-size: 18px;"
                    + "-fx-padding: 8 25;"
                    + "-fx-background-color: #222;"
                    + "-fx-text-fill: gray;"
                    + "-fx-background-radius: 15;");
        }

        upgradeBtn.setOnAction(e -> {
            if (state.credits >= cost) {
                state.credits -= cost;
                tool.upgrade();
                refresh(state, state.tools);
            }
        });

        VBox card = new VBox(10, name, price, upgradeBtn);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setPrefWidth(760);
        card.setMaxWidth(760);
        card.setStyle("-fx-background-color: rgba(0, 20, 40, 0.6);"
                + "-fx-background-radius: 15;"
                + "-fx-border-radius: 15;"
                + "-fx-border-color: rgba(0, 255, 255, 0.3);"
                + "-fx-border-width: 1.5;"
                + "-fx-effect: dropshadow(gaussian, black, 15, 0.3, 0, 0);");

        toolsBox.getChildren().add(card);
    }
}
