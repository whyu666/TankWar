package ui;

import game.Game;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class OverScene extends GameScene {
	
	public OverScene(GameUI manager, int SIZE, Game game) {
		super(manager, SIZE, game);
	}
	
	public Scene initScene() {
    	Label indicator = new Label("游戏结束\n分数 : " + myGame.getScore());
    	indicator.setFont(new Font(20));
    	Button startButton = uiManager.initStartButton();
    	startButton.setText("再玩一次");
    	Button leadersButton = uiManager.initLeadersButton();
		Button returnButton = uiManager.initReturnButton();
    	Button exitButton = uiManager.initExitButton();
    	VBox root = new VBox();
    	root.setSpacing(60);
    	root.setAlignment(Pos.CENTER);
    	root.getChildren().add(indicator);
    	int score = myGame.getScore();
    	if (uiManager.getBoard().canGetOn(score)) {
    		root.getChildren().add(uiManager.initNameInput(score));
    	}
    	root.getChildren().addAll(startButton, leadersButton, returnButton, exitButton);
		return new Scene(root, SIZE, SIZE);
    }

}