package ui;

import game.GameTwoStart;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * @author wangyuanfeng
 * @data 2022/6/22 9:46
 * 游戏结束画面
 */
public class OverScene2 extends GameScene {

    public OverScene2(GameUI manager, int SIZE, GameTwoStart gametwo) {
        super(manager, SIZE, gametwo);
    }

    public Scene initScene() {
        Label indicator = new Label("游戏结束\n玩家1的分数 : " + gametwo.score1+"\n玩家2的分数:" + gametwo.score2);
        Label lable;
        if(gametwo.score1>gametwo.score2)
            lable = new Label("\n玩家1胜利");
        else if(gametwo.score1<gametwo.score2)
            lable = new Label("\n玩家2胜利");
        else lable = new Label("\n打成平手");
        indicator.setFont(new Font(20));
        Button startButton = uiManager.initStartButton();
        startButton.setText("再玩一次");
        Button returnButton = uiManager.initReturnButton();
        Button exitButton = uiManager.initExitButton();
        VBox root = new VBox();
        root.setSpacing(60);
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(indicator);
        root.getChildren().add(lable);
        root.getChildren().addAll(startButton,  returnButton, exitButton);
        return new Scene(root, SIZE, SIZE);
    }
}
