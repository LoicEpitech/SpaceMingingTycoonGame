package screens;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ui.VideoBackground;

public class ShipSelectionScene {

    private final Scene scene;
    private final StackPane root;

    public ShipSelectionScene(Stage stage) {

        Button speedBtn = new Button("Vaisseau Rapide");
        Button stockBtn = new Button("Transporteur");
        Button discountBtn = new Button("Discount");

        speedBtn.setOnAction(e -> startGame(stage, "speed"));
        stockBtn.setOnAction(e -> startGame(stage, "stock"));
        discountBtn.setOnAction(e -> startGame(stage, "discount"));

        VBox menuBox = new VBox(20, speedBtn, stockBtn, discountBtn);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 50; -fx-background-radius: 10;");

        root = new StackPane();
        new VideoBackground(root, "video_menu.mp4");
        root.getChildren().add(menuBox);
        StackPane.setAlignment(menuBox, Pos.CENTER);

        scene = new Scene(root, 1920, 1080);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint(""); // supprime le message "Press ESC..."
        stage.show();
    }

    private void startGame(Stage stage, String type) {
        GameScene game = new GameScene(stage, type);
        stage.setScene(game.getScene());
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(null);
        stage.setFullScreen(true);
    }

    public Scene getScene() {
        return scene;
    }

    public StackPane getRootPane() {
        return root;
    }
}
