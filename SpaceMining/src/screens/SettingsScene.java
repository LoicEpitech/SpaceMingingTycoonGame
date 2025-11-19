package screens;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SettingsScene {
    private final Scene scene;

    public SettingsScene(Stage stage) {
        Button back = new Button("Retour");

        back.setOnAction(e -> stage.setScene(new MainMenuScene(stage).getScene()));

        VBox root = new VBox(20, back);
        root.setAlignment(Pos.CENTER);

        scene = new Scene(root, 1920, 1080);
    }

    public Scene getScene() { return scene; }
}
