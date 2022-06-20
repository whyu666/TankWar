package ui;

import game.Game;
import javafx.scene.Scene;

public abstract class GameScene {

	protected GameUI uiManager;
	protected final int SIZE;
	protected Game myGame;
	
	public GameScene(GameUI manager, int SIZE) {
		uiManager = manager;
		this.SIZE = SIZE;
	}
	
	public GameScene(GameUI manager, int SIZE, Game game) {
		this(manager, SIZE);
		myGame = game;
	}

	public abstract Scene initScene();
}