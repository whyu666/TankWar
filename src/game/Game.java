package game;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import map.GameMap;
import sprite.Direction;
import sprite.PlayerTank;
import sprite.Sprite;
import stable.Home;
import ui.GameHud;

import java.util.ArrayList;

import static game.Main.soundManager;

/**
 * @author tianq(refactor)
 * @implNote
 * game基础类,可实例(单人),可派生
 *
 * 一个“游戏”要实例化，必须有下列要素
 * 1.游戏按键、作弊按键的处理
 * X.状态判断刷新step(不用动,已经拆成下面两个)
 * 2.状态更新updateStatus
 * 3.元素更新updateElements
 * X.状态枚举值和对应的文本说明(main里也要加,牵一发而动全身,算了)
 * 4.关卡与切换逻辑(map,nextlv)
 * 5.hud
 * 6.tank(protected,派生类直接用父类的做P1)
 */


public class Game {

    //以下为公共存储,在派生类中选择性使用

    protected static KeyMon keyMon;       //游戏输入管理,在init中初始化
    //游戏标题,请在派生类中覆盖
    protected static final String TITLE = "坦克大战";
    protected Status status=Status.Wait;  //游戏状态,初始为等待
    protected GameMap map;		        //游戏地图
    protected int width, height;	        //宽高
    protected GraphicsContext gc;	        //渲染画布
    protected GameHud hudManager;	        //顶部栏
    //场景元素集合,渲染时使用,包含地图(水,墙),精灵(坦克,子弹)
    protected ArrayList<Sprite> elements = new ArrayList<>();

    //游戏时间记录,后面可以不用
    protected long startTime = System.nanoTime();		//开始时间记录
    protected long deadTime = System.nanoTime();		//阵亡时间记录
    protected long toLoseTime = System.nanoTime();	//失败时间记录,用于在失败后短暂显示失败画面
    protected long passLevelTime = System.nanoTime();	//通关时间,作用同失败时间
    //延时集合,用于过渡界面的显示(时刻~时刻+延时),比如过关信息
    protected static final long DIE_DELAY = 200 * 1000000L;       //玩家坦克阵亡后延时一段时间(复活硬直)
    protected static final long LOSE_DELAY = 500 * 1000000L;      //用于在失败后短暂显示失败画面
    protected static final long LEVEL_DELAY = 3000 * 1000000L;    //关卡结束后延时一段时间
    //游戏倒计时
    protected static final long GAME_TIME_SECONDS = 30;  //每一关游戏时间
    protected static final long GAME_TIME = GAME_TIME_SECONDS * 1000000000L;

    //关卡记录
    protected int currentLevel = 0;   //当前关卡
    protected int numLevels;		    //关卡总数

    //生命记录
    protected int lives;              //当前生命
    protected static final int INITIAL_LIVES = 3; //起始生命

    //得分记录
    protected int score;              //当前分数
    protected static int SCORE_UNIT = 100;  //得分系数

    //元素标志
    public static final int PLAYER_TANK_MASK = 1;     //二进制：0001
    public static final int ENEMY_TANK_MASK = 3;      //二进制：0010
    public static final int PLAYER_MISSILE_MASK = 6;  //二进制：0110
    public static final int ENEMY_MISSILE_MASK = 9;   //二进制：1001
    public static final int STABLE_MASK = 15;         //二进制：1111

    //玩家坦克
    protected PlayerTank playerTank;
    protected static double playerTankPositionX = 240;
    protected static double playerTankPositionY = 640;
    //外部访问(真正的外部)
    public static double playerTankPositionX() { return playerTankPositionX; }
    public static double playerTankPositionY() {
        return playerTankPositionY;
    }
    public static long getGameTime() { return GAME_TIME_SECONDS; }
    public String getTitle() {
        return TITLE;
    }
    public Status getStatus() {
        return status;
    }
    public int getScore() {
        return score;
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

    //游戏运行状态
    enum Status {
        Wait, Play, Lost, Win, ToLose, Between
    }
    //状态设置函数
    public void setToLose() {   //失败状态
        status = Status.ToLose;
        toLoseTime = System.nanoTime();
    }
    private void setBetween() { //选关状态
        passLevelTime = System.nanoTime();
        status = Status.Between;
    }

    //游戏初始化,派生类第一句调用,再做修改
    public Scene init(int width, int height){
        //[0]:变量初始化
        status = Status.Play;           //游戏开始状态
        this.width = width;             //画面宽
        this.height = height;           //高
        elements = new ArrayList<>();   //渲染集合
        startTime = System.nanoTime();  //开始时间
        lives = INITIAL_LIVES;          //生命值
        score = 0;                      //分数
        currentLevel = 0;               //关卡索引

        //[1]:绘制定义,当中初始化hud和map,派生时覆盖
        setDrawHint(this);

        //[2]:绘制处理
        BorderPane root = new BorderPane(); //游戏主界面
        root.setStyle("-fx-background-color: black;");  //黑色背景
        root.setTop(hudManager.initHud());  //顶栏初始化
        map.init(elements);     //把地图放入场景元素集合
        numLevels = map.numLevels();    //关卡数记录
        nextLevel();  //进入第一关
        gc = initGraphicsContext(root); //初始化绘制区域
        Scene myScene = new Scene(root, width, height + hudManager.getHeight(), Color.BLACK);

        //[3]:输入处理
        keyMon = new KeyMon(myScene);  //处理游戏输入
        myScene.setOnKeyPressed(e -> handleCheatKey(e.getCode()));  //处理作弊输入

        return myScene;
    }

    //场景组成说明,指定游戏的地图和hud务必在派生类中覆盖
    //to Overwrite
    private void setDrawHint(Game g){
        g.hudManager = new GameHud(g);
        g.map = new GameMap(g.width,g.height);
    }

    //游戏帧更新,在main中一秒60次
    public void step(double elapsedTime) {
        updateStatus(elapsedTime);	//更新游戏状态
        handleGameInput();	//处理游戏输入
        //注意:作弊输入在init中
        updateElements(elapsedTime);  //更新元素、状态、得分
        detectCollisions();  //判断是否摧毁元素
        renderElements();  //渲染对象
        hudManager.updateHud();  //更新生命、时间、关卡信息
        map.spawnTank();  //生成敌方坦克
    }

    //下一关,init中使用,
    private void nextLevel() {
        soundManager.onNew();
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


    //处理游戏输入
    //to overwrite
    private void handleGameInput(){
        for (KeyCode k : keyMon.keyStore) {
            handleGameKey(k);    //响应玩家操作
        }
    }

    private void handleGameKey(KeyCode code) {
        if (System.nanoTime() - deadTime < DIE_DELAY) {  //当在阵亡延时中，忽略按键输入
            return;
        }
        switch (code) {
            case SPACE:
                playerTank.fireMissile();
                soundManager.onShoot();
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
            default:
                break;
        }
    }

    //处理作弊输入
    //to overwrite
    private void handleCheatKey(KeyCode code) {
        if (System.nanoTime() - deadTime < DIE_DELAY) {  //当在阵亡延时中，忽略按键输入
            return;
        }
        switch (code) {
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
        }
    }

    //碰撞检测
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

    //清除敌人(作弊选项)
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

    //创建绘制区域
    private GraphicsContext initGraphicsContext(BorderPane root) {
        Canvas canvas = new Canvas(width, height);
        canvas.setStyle("-fx-background-color: black;");
        root.setCenter(canvas);
        return canvas.getGraphicsContext2D();
    }

    //渲染元素
    private void renderElements() {
        elements.sort(null);  //对elements进行排序
        for (Sprite e : elements) {
            e.render(gc);  //渲染每一个对象
        }
    }

    public void updateStatus(double elapsedTime) {
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
//            return;
        }
    }
    //元素更新(主要关卡逻辑)
    //to overwrite
    private void updateElements(double elapsedTime) {
        gc.clearRect(0, 0, width, height);
        int i = 0;
        while (i < elements.size()) {
            Sprite e = elements.get(i);
            if (e.isAlive()) {
                e.update(elapsedTime);
                i++;
            } else {
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
                    if (lives > 0) {
                        soundManager.onSlain();
                    }
                } else if (e.getBITMASK() == Game.ENEMY_TANK_MASK) {  //敌方坦克被子弹击中
                    score += SCORE_UNIT;
                    soundManager.onKill();
                }
            }
        }
    }

    //展示成绩
    private void showScore() {  //一个关卡成功后，显示该界面
        gc.setFill(Color.WHITE);
        gc.setFont(new Font(20));
        gc.fillText("当前分数: " + getScore(), (float) width / 2 - 80, (float) height / 2);
    }

}
