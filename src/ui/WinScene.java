package ui;

import game.Game;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class WinScene extends GameScene {
	
	public WinScene(GameUI manager, int SIZE, Game game) {
		super(manager, SIZE, game);
	}
	
	public Scene initScene() {
    	VBox root = new VBox();
    	root.setSpacing(60);
    	root.setAlignment(Pos.CENTER);
    	int score = myGame.getScore();
    	Label indicator = new Label("你赢了\n分数: " + score);
    	root.getChildren().add(indicator);
    	if (uiManager.getBoard().canGetOn(score)) {
    		root.getChildren().add(uiManager.initNameInput(score));
    	}
    	indicator.setFont(new Font(20));
    	Button startButton = uiManager.initStartButton();
    	Button leadersButton = uiManager.initLeadersButton();
    	startButton.setText("再玩一次");
		Button returnButton = uiManager.initReturnButton();
    	Button exitButton = uiManager.initExitButton();
    	root.getChildren().addAll(startButton, leadersButton, returnButton, exitButton);
		return new Scene(root, SIZE, SIZE);
    }
}