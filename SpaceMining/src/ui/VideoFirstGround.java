package ui;

import java.io.File;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class VideoFirstGround {
    private MediaPlayer player;
    private MediaView view;

    public VideoFirstGround(StackPane root, String videoFileName, boolean loop) {
        try {
            String path = (new File("src/assets/videos/" + videoFileName)).toURI().toString();
            Media media = new Media(path);
            this.player = new MediaPlayer(media);
            this.player.setMute(false);
            this.player.setCycleCount(loop ? -1 : 1);
            this.view = new MediaView(this.player);
            this.view.setPreserveRatio(true);
            this.view.setFitWidth(1920.0);
            this.view.setFitHeight(1080.0);
            this.view.fitWidthProperty().bind(root.widthProperty());
            this.view.fitHeightProperty().bind(root.heightProperty());
            root.getChildren().add(this.view);
            this.player.setOnReady(() -> {
                this.player.play();
            });
        } catch (Exception var6) {
            throw new RuntimeException("Erreur lors du chargement de la vidéo : " + videoFileName, var6);
        }
    }

    public void stop() {
        if (this.player != null) {
            this.player.stop();
        }

    }

    public void removeFromParent(StackPane root) {
        if (this.view != null) {
            root.getChildren().remove(this.view);
        }

    }

    public void replay() {
        if (this.player != null) {
            this.player.seek(Duration.ZERO);
            this.player.play();
        }

    }
}