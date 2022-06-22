package ui;

import game.Game;
import game.GameTwoStart;
import javafx.scene.Scene;

public abstract class GameScene {

	protected GameUI uiManager;
	protected final int SIZE;
	protected Game myGame;
	protected GameTwoStart gametwo;
	
	public GameScene(GameUI manager, int SIZE) {
		uiManager = manager;
		this.SIZE = SIZE;
	}
	
	public GameScene(GameUI manager, int SIZE, Game game) {
		this(manager, SIZE);
		myGame = game;
	}

	public GameScene(GameUI manager, int SIZE, GameTwoStart gametwo) {
		this(manager, SIZE);
		this.gametwo = gametwo;
	}

	public abstract Scene initScene();
}