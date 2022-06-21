package ui;

import game.Game;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import ui.GameUI;

public class SetMapScene extends GameScene {

    public SetMapScene(GameUI manager, int SIZE) {
        super(manager, SIZE);
    }

    @Override
    public Scene initScene() {
        Label title = new Label("设置地图");
        title.setFont(new Font(20));
        title.setPadding(new Insets(15, 15, 15, 15));
        title.setTextAlignment(TextAlignment.CENTER);
        VBox root = new VBox();
        root.setSpacing(60);
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(uiManager.initMapInput());
        return new Scene(root, SIZE, SIZE);
    }
}
