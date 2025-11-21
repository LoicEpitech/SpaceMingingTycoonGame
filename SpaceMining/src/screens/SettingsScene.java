package screens;

import game.GameState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SettingsScene {

    private final Scene scene;

    public SettingsScene(Stage stage) {
        VBox root = new VBox(30);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #0d1b2a, #1b263b);"); // bleu spatial

        GameState state = GameState.getCurrentState();

        Label title = new Label("Paramètres");
        title.setStyle("-fx-font-size: 48px; -fx-text-fill: #ffffff; -fx-font-weight: bold;");

        // --- Key Bindings Section ---
        VBox keysBox = new VBox(15);
        keysBox.setAlignment(Pos.CENTER);
        keysBox.setPadding(new Insets(20));
        keysBox.setStyle("-fx-background-color: #162447; -fx-background-radius: 10; -fx-border-color: #1f4287; -fx-border-width: 2;");

        Label keysTitle = new Label("Touches de contrôle");
        keysTitle.setStyle("-fx-font-size: 24px; -fx-text-fill: #ffffff; -fx-font-weight: bold;");
        keysBox.getChildren().add(keysTitle);

        String[] actions = {"UP", "DOWN", "LEFT", "RIGHT", "INTERACT", "MENU"};
        for (String action : actions) {
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER);

            Label actionLabel = new Label(action + " : ");
            actionLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 18px;");

            KeyCode code = state.keyBindings.get(action);
            String keyName = (code != null) ? code.getName() : "Non assigné";
            Button changeBtn = new Button(keyName);
            changeBtn.setStyle("-fx-background-color: #1f4287; -fx-text-fill: #ffffff; -fx-font-size: 16px;");
            changeBtn.setOnMouseEntered(e -> changeBtn.setStyle("-fx-background-color: #3a6ea5; -fx-text-fill: #ffffff; -fx-font-size: 16px;"));
            changeBtn.setOnMouseExited(e -> changeBtn.setStyle("-fx-background-color: #1f4287; -fx-text-fill: #ffffff; -fx-font-size: 16px;"));

            changeBtn.setOnAction(e -> {
                changeBtn.setText("Appuyez sur une touche...");
                root.setOnKeyPressed(keyEvent -> {
                    state.keyBindings.put(action, keyEvent.getCode());
                    changeBtn.setText(keyEvent.getCode().getName());
                    root.setOnKeyPressed(null);
                });
            });

            row.getChildren().addAll(actionLabel, changeBtn);
            keysBox.getChildren().add(row);
        }

        // --- Ship Style Section ---
        VBox shipBox = new VBox(15);
        shipBox.setAlignment(Pos.CENTER);
        shipBox.setPadding(new Insets(20));
        shipBox.setStyle("-fx-background-color: #162447; -fx-background-radius: 10; -fx-border-color: #1f4287; -fx-border-width: 2;");

        Label shipTitle = new Label("Style du vaisseau");
        shipTitle.setStyle("-fx-font-size: 24px; -fx-text-fill: #ffffff; -fx-font-weight: bold;");
        shipBox.getChildren().add(shipTitle);

        HBox shipsPreview = new HBox(30);
        shipsPreview.setAlignment(Pos.CENTER);

        String[] shipTypes = {"CLASSIC", "INDUSTRIAL", "FUTURISTIC"};
        String[] shipImages = {
                "assets/ship/spaceship.jpg",
                "assets/ship/spaceship1.jpg",
                "assets/ship/spaceship2.jpg"
        };

        StackPane[] shipPanes = new StackPane[3];

        for (int i = 0; i < shipTypes.length; i++) {
            int index = i;

            ImageView shipView = new ImageView(new Image(shipImages[i]));
            shipView.setFitWidth(200);
            shipView.setFitHeight(120);

            StackPane shipPane = new StackPane(shipView);
            shipPane.setStyle("-fx-border-color: transparent; -fx-border-width: 4; -fx-border-radius: 5;");

            if (state.interiorStyle.name().equals(shipTypes[i])) {
                shipPane.setStyle("-fx-border-color: #00ffff; -fx-border-width: 4; -fx-border-radius: 5;");
            }

            shipPane.setOnMouseClicked(e -> {
                state.interiorStyle = GameState.ShipInteriorStyle.valueOf(shipTypes[index]);
                for (StackPane p : shipPanes) {
                    p.setStyle("-fx-border-color: transparent; -fx-border-width: 4; -fx-border-radius: 5;");
                }
                shipPane.setStyle("-fx-border-color: #00ffff; -fx-border-width: 4; -fx-border-radius: 5;");
            });

            shipPanes[i] = shipPane;
            shipsPreview.getChildren().add(shipPane);
        }

        shipBox.getChildren().add(shipsPreview);

        // --- Back Button ---
        Button backBtn = new Button("Retour");
        backBtn.setStyle("-fx-background-color: #1f4287; -fx-text-fill: white; -fx-font-size: 18px;");
        backBtn.setOnMouseEntered(e -> backBtn.setStyle("-fx-background-color: #3a6ea5; -fx-text-fill: white; -fx-font-size: 18px;"));
        backBtn.setOnMouseExited(e -> backBtn.setStyle("-fx-background-color: #1f4287; -fx-text-fill: white; -fx-font-size: 18px;"));
        backBtn.setOnAction(e -> stage.setScene(new MainMenuScene(stage).getScene()));

        root.getChildren().addAll(title, keysBox, shipBox, backBtn);

        scene = new Scene(root, 1920, 1080);
    }

    public Scene getScene() {
        return scene;
    }
}
