package game;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import map.GameMap2;
import sprite.Direction;
import sprite.PlayerTank;
import sprite.PlayerTank2;
import sprite.Sprite;
import stable.Home;
import ui.GameHud2;

import java.util.ArrayList;

/**
 * @author wangyuanfeng
 * @data 2022/6/22 7:07
 */
public class Game2 {
    private static final String TITLE = "保卫你的家";
    private Game2.Status status = Game2.Status.Wait;
    private int currentLevel = 0;  //当前关卡

    private long deadTime = System.nanoTime();
    private static final long DIE_DELAY = 200 * 1000000L;  //玩家坦克阵亡后延时一段时间

    public int lives1, lives2;
    private int score1, score2;
    private static final int INITIAL_LIVES = 6;
    private static final int SCORE_UNIT = 100;

    private GraphicsContext gc;
    private PlayerTank playerTank1;
    private PlayerTank2 playerTank2;
    private int width, height;
    private GameMap2 map;
    private GameHud2 hudManager;

    public static final long GAME_TIME_SECONDS = 10;  //每一关游戏时间
    private static final long GAME_TIME = GAME_TIME_SECONDS * 1000000000L;
    private long startTime = System.nanoTime();

    private ArrayList<Sprite> elements = new ArrayList<>();

    public static double playerTank1PositionX = 240;
    public static double playerTank1PositionY = 640;
    public static double playerTank2PositionX = 400;
    public static double playerTank2PositionY = 640;

    public String getTitle() {
        return TITLE;
    }

    public Scene initGameTwo(int width, int height) {
        status = Game2.Status.Play;
        this.width = width;
        this.height = height;
        elements = new ArrayList<>();
        startTime = System.nanoTime();
        lives1 = INITIAL_LIVES;
        lives2 = INITIAL_LIVES;
        score1 = 0;
        score2 = 0;
        currentLevel = 0;
        //界面绘制
        hudManager = new GameHud2(this);
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: black;");
        root.setTop(hudManager.initHud());
        map = new GameMap2(width, height);
        map.init(elements);
        int numLevels = map.numLevels();
        nextLevel();  //进入第一关
        gc = initGraphicsContext(root);
        Scene myScene = new Scene(root, width, height + hudManager.getlivesHud1Height(), Color.BLACK);
        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));  //对输入进行响应
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
            return;
        }
        if (status == Status.Play && System.nanoTime() - startTime > GAME_TIME) {
            status = Status.AllWin;
            gc.clearRect(0, 0, width, height);
            return;
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
                playerTank1.fireMissile();
                break;
            case RIGHT:
                playerTank1.setDirection(Direction.RIGHT);
                playerTank1PositionY = playerTank1.getPositionY();
                break;
            case LEFT:
                playerTank1.setDirection(Direction.LEFT);
                playerTank1PositionY = playerTank1.getPositionY();
                break;
            case UP:
                playerTank1.setDirection(Direction.UP);
                playerTank1PositionX = playerTank1.getPositionX();
                break;
            case DOWN:
                playerTank1.setDirection(Direction.DOWN);
                playerTank1PositionX = playerTank1.getPositionX();
                break;
            case SPACE:
                playerTank2.fireMissile();
                break;
            case D:
                playerTank2.setDirection(Direction.RIGHT);
                System.out.println(playerTank2.getPositionX());
                playerTank2PositionY = playerTank2.getPositionY();
                break;
            case A:
                playerTank2.setDirection(Direction.LEFT);
                playerTank2PositionY = playerTank2.getPositionY();
                break;
            case W:
                playerTank2.setDirection(Direction.UP);
                playerTank2PositionX = playerTank2.getPositionX();
                break;
            case S:
                playerTank2.setDirection(Direction.DOWN);
                playerTank2PositionX = playerTank2.getPositionX();
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

    public int getCurrentLevel() {
        return currentLevel;
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
                    if (status == Status.Play) {
                        status = Status.AllLose;
                    }
                    i++;
                    continue;
                }
                //if (e.getBITMASK() == playerTank1.getBITMASK()) {
                if (e instanceof PlayerTank) {
                    playerTank1 = map.revivePlayerTank();
                    lives1--;
                    deadTime = System.nanoTime();
                    score1 -= 300;
                }
                //if (e.getBITMASK() == playerTank2.getBITMASK()) {
                if (e instanceof PlayerTank2) {
                    playerTank2 = map.revivePlayerTank2();
                    lives2--;
                    deadTime = System.nanoTime();
                    score2 -= 300;
                }
                else if (e.getBITMASK() == ENEMY_TANK_MASK) {  //敌方坦克被子弹击中
                    System.out.println(i);
                    if (e.getPerson() == 1) {
                        score1 += SCORE_UNIT;
                    }
                    if (e.getPerson() == 2) {
                        score2 += SCORE_UNIT;
                    }
                }
                elements.remove(i);
            }
        }
    }

    public int getScore1() {
        return score1;
    }

    public int getScore2() {
        return score2;
    }

    public Status getStatus() {
        return status;
    }

    public static final int PLAYER_TANK_MASK = 1;     //二进制：0001
    public static final int ENEMY_TANK_MASK = 3;      //二进制：0010
    public static final int PLAYER_MISSILE_MASK = 6;  //二进制：0110
    public static final int ENEMY_MISSILE_MASK = 9;   //二进制：1001
    public static final int STABLE_MASK = 15;         //二进制：1111

    enum Status {  //游戏运行状态
        Wait, Play, Lose1, Lose2, AllLose, AllWin
    }
}
