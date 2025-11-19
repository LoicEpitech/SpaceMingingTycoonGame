
package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public abstract class GameMenu extends VBox {
    public GameMenu(String title) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing((double)20.0F);
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-padding: 40px; -fx-border-color: white; -fx-border-width: 2;");
        this.setPrefWidth((double)400.0F);
        this.setPrefHeight((double)300.0F);
        Label titleLabel = new Label(title);
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setFont(new Font((double)28.0F));
        this.getChildren().add(titleLabel);
    }

    public void show() {
        this.setVisible(true);
    }

    public void hide() {
        this.setVisible(false);
    }
}
