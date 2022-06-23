package ui;

import game.Game2;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class OverScene2 extends GameScene {

    public OverScene2(GameUI manager, int SIZE, Game2 gametwo) {
        super(manager, SIZE, gametwo);
    }

    public Scene initScene() {
        Label indicator = new Label("游戏结束\n分数:" + game2.getScore());
        indicator.setFont(new Font(20));
        Button startButton = uiManager.initDouble_gameButton();
        startButton.setText("再玩一次");
        Button returnButton = uiManager.initReturnButton();
        Button exitButton = uiManager.initExitButton();
        VBox root = new VBox();
        root.setSpacing(60);
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(indicator);
        root.getChildren().addAll(startButton, returnButton, exitButton);
        return new Scene(root, SIZE, SIZE);
    }

}