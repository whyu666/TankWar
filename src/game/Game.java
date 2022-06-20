package game;

import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import map.GameMap;
import sprite.Direction;
import sprite.PlayerTank;
import sprite.Sprite;
import stable.Home;
import ui.GameHud;

enum Status {  //游戏运行状态
	Wait, Play, Lost, Win, ToLose, Between
}

public class Game {

	private static final String TITLE = "保卫你的家";
	private Status status = Status.Wait;
	private int currentLevel = 0;

	private long toLoseTime = System.nanoTime();
	private static final long LOSE_DELAY = 500 * 1000000L;
	private long deadTime = System.nanoTime();
	private static final long DIE_DELAY = 200 * 1000000L;  //玩家坦克阵亡后延时一段时间
	private long passLevelTime = System.nanoTime();
	private static final long LEVEL_DELAY = 3000 * 1000000L;  //关卡结束后延时一段时间

	private int lives;
	private int score;
	private static final int INITIAL_LIVES = 3;
	private static final int SCORE_UNIT = 100;

	private GraphicsContext gc;
	private PlayerTank playerTank;
	private int width, height;
	private GameMap map;
	private int numLevels;
	private GameHud hudManager;

	public static final long GAME_TIME_SECONDS = 30;  //每一关游戏时间
	private static final long GAME_TIME = GAME_TIME_SECONDS * 1000000000L;
	private long startTime = System.nanoTime();

	private ArrayList<Sprite> elements = new ArrayList<>();

	public static double playerTankPositionX = 240;
	public static double playerTankPositionY = 640;
	public static double playerTankPositionX() {
		return playerTankPositionX;
	}
	public static double playerTankPositionY() {
		return playerTankPositionY;
	}

	public String getTitle() {
		return TITLE;
	}

	public Scene init(int width, int height) {
		//初始化变量
		status = Status.Play;
		this.width = width;
		this.height = height;
		elements = new ArrayList<>();
		startTime = System.nanoTime();
		lives = INITIAL_LIVES;
		score = 0;
		currentLevel = 0;
		//界面绘制
		hudManager = new GameHud(this);
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: black;");
		root.setTop(hudManager.initHud());
		map = new GameMap(width, height);
		map.init(elements);
		numLevels = map.numLevels();
		nextLevel();  //进入第一关
		gc = initGraphicsContext(root);
		Scene myScene = new Scene(root, width, height + hudManager.getHeight(), Color.BLACK);
		myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));  //对输入进行响应
		return myScene;
	}

	public void step(double elapsedTime) {
		if (lives <= 0 && status != Status.ToLose) {
			setToLose();
			return;
		}
		if (status == Status.Play && System.nanoTime() - startTime > GAME_TIME) {
			if (currentLevel >= numLevels) {
				status = Status.Win;
				return;
			}
			//显示该关成绩，并短暂暂停一段时间
			gc.clearRect(0, 0, width, height);
			showScore();
			setBetween();
		}
		if (status == Status.Between) {
			if (System.nanoTime() - passLevelTime > LEVEL_DELAY) {  //暂停一段时间后，进入到下一关
				nextLevel();
			}
			return;
		}
		if (status == Status.ToLose && System.nanoTime() - toLoseTime > LOSE_DELAY) {
			status = Status.Lost;
			return;
		}
		updateElements(elapsedTime);  //更新元素、状态、得分
		detectCollisions();  //判断是否摧毁元素
		renderElements();  //渲染对象
		hudManager.updateHud();  //更新生命、时间、关卡信息
		map.spawnTank();  //生成敌方坦克
	}

	public void setToLose() {
		status = Status.ToLose;
		toLoseTime = System.nanoTime();
	}

	public Status getStatus() {
		return status;
	}

	public int getScore() {
		return score;
	}

	private void nextLevel() {
		if (currentLevel >= numLevels) {
			status = Status.Win;
			return;
		}
		elements.clear();
		startTime = System.nanoTime();
		map.buildMap(currentLevel);
		playerTank = map.getPlayerTank();
		status = Status.Play;
		currentLevel++;
	}

	private void handleKeyInput (KeyCode code) {
		if (System.nanoTime() - deadTime < DIE_DELAY) {  //当在阵亡延时中，忽略按键输入
			return;
		}
		switch (code) {
			case SPACE:
				playerTank.fireMissile();
				break;
			case RIGHT:
			case D:
				playerTank.setDirection(Direction.RIGHT);
				playerTankPositionY = playerTank.getPositionY();
				break;
			case LEFT:
			case A:
				playerTank.setDirection(Direction.LEFT);
				playerTankPositionY = playerTank.getPositionY();
				break;
			case UP:
			case W:
				playerTank.setDirection(Direction.UP);
				playerTankPositionX = playerTank.getPositionY();
				break;
			case DOWN:
			case S:
				playerTank.setDirection(Direction.DOWN);
				playerTankPositionX = playerTank.getPositionY();
				break;
			case C:
				clearEnemies();
				break;
			case B:
				playerTank.buffImmortal();
				break;
			case L:
				lives++;
				break;
			case N:
				nextLevel();
				break;
			default:
				break;
		}
	}

	private void detectCollisions() {
		for (int i = 0; i < elements.size(); i++) {
			for (int j = i + 1; j < elements.size(); j++) {
				Sprite e1 = elements.get(i);
				Sprite e2 = elements.get(j);
				if (!e1.intersects(e2)) {  //判断两个element是否相交
					continue;
				}
				if ((e1.getBITMASK() & e2.getBITMASK()) != 0) {  //两个element相交，依次判断每一位，至少存在一位同时为1
					//有以下情况：玩家坦克与敌方子弹和stable类、敌方坦克与玩家子弹和stable类、玩家子弹与stable类、敌方子弹与stable类
					//分别使用handleCollision函数进行判断是否摧毁对应元素
					e1.handleCollision(e2);
					e2.handleCollision(e1);
				}
			}
		}
	}

	public void clearEnemies() {
		for (int i = 0; i < elements.size(); i++) {
			Sprite e = elements.get(i);
			if (e.getBITMASK() == ENEMY_TANK_MASK) {
				e.setAlive(false);
				elements.remove(i);
				i--;
			}
		}
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public long getStartTime() {
		return startTime;
	}

	public int getLives() {
		return lives;
	}

	private GraphicsContext initGraphicsContext(BorderPane root) {
		Canvas canvas = new Canvas(width, height);
		canvas.setStyle("-fx-background-color: black;");
		root.setCenter(canvas);
		return canvas.getGraphicsContext2D();
	}

	private void renderElements() {
		elements.sort(null);  //对elements进行排序
		for (Sprite e: elements) {
			e.render(gc);  //渲染每一个对象
		}
	}

	private void updateElements(double elapsedTime) {
		gc.clearRect(0, 0, width, height);
		int i = 0;
		while (i < elements.size()) {
			Sprite e = elements.get(i);
			if (e.isAlive()) {
				e.update(elapsedTime);
				i++;
			}
			else {
				if (e instanceof Home) {  //家不存在
					if (status == Status.Play) {
						setToLose();
					}
					i++;
					continue;
				}
				elements.remove(i);
				if (e.getBITMASK() == playerTank.getBITMASK()) {  //玩家坦克被子弹击中
					playerTank = map.revivePlayerTank();
					lives--;
					deadTime = System.nanoTime();
				}
				else if (e.getBITMASK() == Game.ENEMY_TANK_MASK) {  //敌方坦克被子弹击中
					score += SCORE_UNIT;
				}
			}
		}
	}

	private void showScore() {  //一个关卡成功后，显示该界面
		gc.setFill(Color.WHITE);
		gc.setFont(new Font(20));
		gc.fillText("当前分数: " + getScore(), (float)width / 2 - 80, (float)height / 2);
	}

	private void setBetween() {
		passLevelTime = System.nanoTime();
		status = Status.Between;
	}

	public static final int PLAYER_TANK_MASK = 1;     //二进制：0001
	public static final int ENEMY_TANK_MASK = 3;      //二进制：0010
	public static final int PLAYER_MISSILE_MASK = 6;  //二进制：0110
	public static final int ENEMY_MISSILE_MASK = 9;   //二进制：1001
	public static final int STABLE_MASK = 15;         //二进制：1111
}