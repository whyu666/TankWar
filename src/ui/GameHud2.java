package ui;

import game.Game2;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * @author wangyuanfeng
 * @data 2022/6/22 9:32
 */
public class GameHud2 {
    private Text timeHud;
    private Text livesHud1;
    private Text livesHud2;
    private Text levelHud;
    private Game2 gameTwo;

    public GameHud2(Game2 gametwo) {
        this.gameTwo = gametwo;
    }

    public HBox initHud() {
        timeHud = new Text();
        configureGameHud(timeHud);

        livesHud1 = new Text();
        configureGameHud(livesHud1);

        livesHud2=new Text();
        configureGameHud(livesHud2);

        levelHud = new Text();
        configureGameHud(levelHud);

        HBox box = new HBox();
        box.getChildren().addAll(livesHud1, livesHud2,timeHud, levelHud);
        box.setSpacing(120);  //居中显示
        BorderPane.setAlignment(box, Pos.CENTER);
        return box;
    }

    private void configureGameHud(Text hud) {
        hud.setFont(new Font(20));
        hud.setFill(Color.WHITE);
    }

    public void updateHud() {
        updateLivesHud();
        updateTimeHud();
        updateLevelHud();
    }

    public void updateLivesHud() {
        livesHud1.setText(String.format("总生命: %d", gameTwo.lives1));
    }

    public void updateTimeHud() {
        timeHud.setText("时间: " + (Game2.GAME_TIME_SECONDS - (System.nanoTime() - gameTwo.getStartTime()) / 1000000000L));
    }

    public void updateLevelHud() {
        levelHud.setText("关卡: " + gameTwo.getCurrentLevel());
    }
    public double getlivesHud1Height() {
        return livesHud1.getLayoutBounds().getHeight();
    }
    public double getlivesHud2Height() {
        return livesHud2.getLayoutBounds().getHeight();
    }
}
