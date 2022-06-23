package ui;

import game.Game2;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameHud2 {

    private Text livesHud;
    private Text timeHud;
    private final Game2 game2;

    public GameHud2(Game2 newGame2) {
        this.game2 = newGame2;
    }

    public HBox initHud() {
        livesHud = new Text();
        configureGameHud(livesHud);
        timeHud = new Text();
        configureGameHud(timeHud);
        HBox box = new HBox();
        box.getChildren().addAll(livesHud, timeHud);
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
    }

    public void updateLivesHud() {
        livesHud.setText(String.format("玩家总生命: %d", game2.getLives()));
    }

    public void updateTimeHud() {
        timeHud.setText("时间: " + (Game2.getGameTime() - (System.nanoTime() - game2.getStartTime()) / 1000000000L));
    }

    public double getLivesHudHeight() {
        return livesHud.getLayoutBounds().getHeight();
    }

}