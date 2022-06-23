package ui;

import game.Game;
import game.Game2;
import javafx.scene.Scene;

public abstract class GameScene {

	protected GameUI uiManager;
	protected final int SIZE;
	protected Game myGame;
	protected Game2 game2;
	
	public GameScene(GameUI manager, int SIZE) {
		uiManager = manager;
		this.SIZE = SIZE;
	}
	
	public GameScene(GameUI manager, int SIZE, Game game) {
		this(manager, SIZE);
		myGame = game;
	}

	public GameScene(GameUI manager, int SIZE, Game2 gametwo) {
		this(manager, SIZE);
		this.game2 = gametwo;
	}

	public abstract Scene initScene();

}