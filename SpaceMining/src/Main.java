import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import screens.GameScene;
import screens.MainMenuScene;
import ui.VideoBackground;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane();

        new VideoBackground(root, "video_menu.mp4");

        MainMenuScene menu = new MainMenuScene(stage);
        root.getChildren().add(menu.getRootPane());

        Scene scene = new Scene(root, 1920, 1080);

        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(null);

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case F11 -> stage.setFullScreen(!stage.isFullScreen());
            }
        });

        stage.setScene(scene);
        stage.setTitle("Space Mining - Menu");
        stage.setFullScreen(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
