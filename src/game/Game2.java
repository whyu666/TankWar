package game;

import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import map.GameMap2;
import sprite.Direction;
import sprite.PlayerTank;
import sprite.PlayerTank2;
import sprite.Sprite;
import stable.Home;
import ui.GameHud2;
import static game.Main.soundManager;

public class Game2 {

    private static KeyMon keyMon;  //游戏输入管理
    private Game2.Status status = Game2.Status.Wait;
    private int currentLevel = 0;  //当前关卡
    private long deadTime = System.nanoTime();
    private static final long DIE_DELAY = 200 * 1000000L;  //玩家坦克阵亡后延时一段时间
    private int lives1, lives2;
    private int score;
    private static final int INITIAL_LIVES = 6;
    private static final int SCORE_UNIT = 100;
    private GraphicsContext gc;
    private PlayerTank playerTank1;
    private PlayerTank2 playerTank2;
    private int width, height;
    private GameMap2 map;
    private GameHud2 hudManager;
    private static final long GAME_TIME_SECONDS = 120;  //每一关游戏时间
    private static final long GAME_TIME = GAME_TIME_SECONDS * 1000000000L;
    private long startTime = System.nanoTime();
    private ArrayList<Sprite> elements = new ArrayList<>();
    public static double playerTank1PositionX = 240;
    public static double playerTank1PositionY = 640;
    public static double playerTank2PositionX = 400;
    public static double playerTank2PositionY = 640;

    public Scene initGame2(int width, int height) {
        status = Game2.Status.Play;
        this.width = width;
        this.height = height;
        elements = new ArrayList<>();
        startTime = System.nanoTime();
        lives1 = INITIAL_LIVES;
        lives2 = INITIAL_LIVES;
        score = 0;
        currentLevel = 0;
        //界面绘制
        hudManager = new GameHud2(this);
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: black;");
        root.setTop(hudManager.initHud());
        map = new GameMap2(width, height);
        map.init(elements);
        nextLevel();  //进入第一关
        gc = initGraphicsContext(root);
        Scene myScene = new Scene(root, width, height + hudManager.getLivesHudHeight(), Color.BLACK);
        keyMon = new KeyMon(myScene);	//处理游戏输入
        myScene.setOnKeyPressed(e -> handleCheatKey(e.getCode()));  //处理作弊输入
        return myScene;
    }

    public void step(double elapsedTime) {
        if (lives1 <= 0 && status != Status.Lose1 && lives2 > 0) {
            status = Status.Lose1;
            return;
        }
        if (lives2 <= 0 && status != Status.Lose2 && lives1 > 0) {
            status = Status.Lose2;
            return;
        }
        if (lives1 <= 0 && lives2 <= 0 && status != Status.AllLose) {
            status = Status.AllLose;
            soundManager.playDefeat();
            return;
        }
        if (status == Status.Play && System.nanoTime() - startTime > GAME_TIME) {
            status = Status.AllWin;
            gc.clearRect(0, 0, width, height);
            soundManager.playVictory();
            return;
        }
        for (KeyCode k:keyMon.keyStore) {
            handleKeyInput(k);    //响应玩家操作
        }
        updateElements(elapsedTime);  //更新元素、状态、得分
        detectCollisions();  //判断是否摧毁元素
        renderElements();  //渲染对象
        hudManager.updateHud();  //更新生命、时间、关卡信息
        map.spawnTank();  //生成敌方坦克
    }

    private void nextLevel() {
        elements.clear();
        startTime = System.nanoTime();
        map.buildMap2(currentLevel);
        playerTank1 = map.getPlayerTank();
        playerTank2 = map.getPlayerTank2();
        status = Game2.Status.Play;
        currentLevel++;
    }

    private void handleKeyInput (KeyCode code) {
        if (System.nanoTime() - deadTime < DIE_DELAY) {  //当在阵亡延时中，忽略按键输入
            return;
        }
        switch (code) {
            case ENTER:
                playerTank2.fireMissile();
                soundManager.onShoot();
                break;
            case RIGHT:
                playerTank2.setDirection(Direction.RIGHT);
                playerTank2PositionY = playerTank1.getPositionY();
                break;
            case LEFT:
                playerTank2.setDirection(Direction.LEFT);
                playerTank2PositionY = playerTank1.getPositionY();
                break;
            case UP:
                playerTank2.setDirection(Direction.UP);
                playerTank2PositionX = playerTank1.getPositionX();
                break;
            case DOWN:
                playerTank2.setDirection(Direction.DOWN);
                playerTank2PositionX = playerTank1.getPositionX();
                break;
            case SPACE:
                playerTank1.fireMissile();
                soundManager.onShoot();
                break;
            case D:
                playerTank1.setDirection(Direction.RIGHT);
                System.out.println(playerTank2.getPositionX());
                playerTank1PositionY = playerTank2.getPositionY();
                break;
            case A:
                playerTank1.setDirection(Direction.LEFT);
                playerTank1PositionY = playerTank2.getPositionY();
                break;
            case W:
                playerTank1.setDirection(Direction.UP);
                playerTank1PositionX = playerTank2.getPositionX();
                break;
            case S:
                playerTank1.setDirection(Direction.DOWN);
                playerTank1PositionX = playerTank2.getPositionX();
            default:
                break;
        }
    }

    private void handleCheatKey(KeyCode code) {
        if (System.nanoTime() - deadTime < DIE_DELAY) {  //当在阵亡延时中，忽略按键输入
            return;
        }
        if (code == KeyCode.N) {
            status = Status.AllLose;
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

    public long getStartTime() {
        return startTime;
    }

    public int getLives1() {
        return lives1;
    }

    public int getLives2() {
        return lives2;
    }

    public static long getGameTime() {
        return GAME_TIME_SECONDS;
    }

    private GraphicsContext initGraphicsContext(BorderPane root) {
        Canvas canvas = new Canvas(width, height);  //画布控件表示一个矩形区域，应用程序可以绘制某些东西或者可以接收用户创建的输入
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
//                    if (status == Status.Play) {
//                        status = Status.AllLose;
//                    }
                    status = Status.AllLose;
                    i++;
                    continue;
                }
                if (e instanceof PlayerTank) {
                    playerTank1 = map.revivePlayerTank();
                    lives1--;
                    deadTime = System.nanoTime();
                    score -= 300;
                    if (lives1>0) soundManager.onSlain();
                }
                if (e instanceof PlayerTank2) {
                    playerTank2 = map.revivePlayerTank2();
                    lives2--;
                    deadTime = System.nanoTime();
                    score -= 300;
                    if (lives2 > 0) {
                        soundManager.onSlain();
                    }
                }
                else if (e.getBITMASK() == ENEMY_TANK_MASK) {  //敌方坦克被子弹击中
                    score += SCORE_UNIT;
                    soundManager.onKill();
                }
                elements.remove(i);
            }
        }
    }

    public int getScore() {
        return score;
    }

    public Status getStatus() {
        return status;
    }

    public static final int ENEMY_TANK_MASK = 3;

    enum Status {  //游戏运行状态
        Wait, Play, Lose1, Lose2, AllLose, AllWin
    }

}