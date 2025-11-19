package ui;

import java.io.File;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class VideoBackground {
    private final MediaPlayer player;

    public VideoBackground(StackPane root, String videoFileName) {
        try {
            String path = (new File("SpaceMining/src/assets/videos/" + videoFileName)).toURI().toString();
            Media media = new Media(path);
            this.player = new MediaPlayer(media);
            this.player.setCycleCount(-1);
            this.player.setMute(true);
            this.player.play();
            MediaView view = new MediaView(this.player);
            view.setPreserveRatio(true);
            view.setFitWidth(1920.0);
            view.setFitHeight(1080.0);
            view.fitWidthProperty().bind(root.widthProperty());
            view.fitHeightProperty().bind(root.heightProperty());
            root.getChildren().add(0, view);
            this.player.setOnReady(() -> {
                this.player.play();
            });
        } catch (Exception var6) {
            throw new RuntimeException("Erreur lors du chargement de la vidéo : " + videoFileName, var6);
        }
    }

    public void play() {
        if (this.player != null) {
            this.player.play();
        }

    }

    public void stop() {
        if (this.player != null) {
            this.player.stop();
            this.player.dispose();
        }

    }
}