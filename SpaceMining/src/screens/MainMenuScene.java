package screens;

import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import tools.SaveManager;
import ui.VideoBackground;

public class MainMenuScene {

    private final Scene scene;
    private final StackPane root;

    public MainMenuScene(Stage stage) {

        Button newGameBtn = new Button("Nouvelle Partie");
        Button loadBtn = new Button("Charger Partie");
        Button settingsBtn = new Button("Options");
        Button quitBtn = new Button("Quitter");

        newGameBtn.setOnAction(e -> {
            ShipSelectionScene select = new ShipSelectionScene(stage);
            stage.setScene(select.getScene());
        });

        loadBtn.setOnAction(e -> {
            // Décommenter si tu veux charger le jeu
            // GameScene game = new GameScene(stage, SaveManager.loadShipType());
            // SaveManager.loadGameInto(game);
            // stage.setScene(game.getScene());
            // stage.setFullScreen(true);
        });

        settingsBtn.setOnAction(e -> {
            SettingsScene settings = new SettingsScene(stage);
            stage.setScene(settings.getScene());
        });

        quitBtn.setOnAction(e -> stage.close());

        VBox menuBox = new VBox(25, newGameBtn, loadBtn, settingsBtn, quitBtn);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.5); " +
                        "-fx-padding: 50; " +
                        "-fx-background-radius: 10;"
        );

        root = new StackPane();
        new VideoBackground(root, "video_menu.mp4");
        root.getChildren().add(menuBox);
        StackPane.setAlignment(menuBox, Pos.CENTER);

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());

        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();
    }

    public Scene getScene() {
        return scene;
    }

    public StackPane getRootPane() {
        return root;
    }
}
