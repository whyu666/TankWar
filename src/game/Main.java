package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import ui.GameUI;
import ui.LeadersScene;
import ui.OverScene;
import ui.OverScene2;
import ui.SoundManager;
import ui.StartScene;
import ui.WinScene;

public class Main extends Application {

    private static final int SIZE = 680;
    private static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

    private Game myGame;
    private Game2 myGame2;
    private Stage stage;
    private KeyFrame frame;
    private Timeline animation;
    private GameUI uiManager;
//    private SoundManager soundManager;
    public static SoundManager soundManager;

    //处理开始按钮
    class GameStart implements EventHandler<ActionEvent> {
    	public void handle(ActionEvent event) {
			gameStart();
		}
    }
    //处理双人模式按钮
    class  GameTwo implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            doubleGameStart();}
    }
    //处理帮助按钮
    class GameHelp implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            gameHelp();
        }
    }
    //处理显示排行榜按钮
    class ShowLeaders implements EventHandler<ActionEvent> {
    	public void handle(ActionEvent event) {
			showLeaders();
		}
    }
    //处理退出按钮
    class GameExit implements EventHandler<ActionEvent> {
    	public void handle(ActionEvent event) {
			stage.close();
		}
    }
    //处理返回主界面按钮
    class GameReturn implements EventHandler<ActionEvent> {  //增加返回主界面动作
        public void handle(ActionEvent event) {
            returnGame();
        }
    }

    private void gameStart() {
        myGame = new Game();
        stage.setTitle(myGame.getTitle());
        uiManager.refreshGame();//文字不可输入
        Scene gameScene = myGame.init(SIZE, SIZE);
        stage.setScene(gameScene);
        //设置游戏循环
        frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step());
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private void step() {
        switch(myGame.getStatus()) {
            case Lost:
                gameOver();
                return;
            case Win:
                gameWin();
                return;
            default:
                break;
        }
        myGame.step(Main.SECOND_DELAY);
    }

    private void doubleGameStart() {
        myGame2 = new Game2();
        stage.setTitle("保卫你的家");
        uiManager.refreshGame();//文字不可输入
        Scene gameScene = myGame2.initGameTwo(SIZE, SIZE);
        stage.setScene(gameScene);
        //设置游戏循环
        frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step2());
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }
    private void step2() {
        switch(myGame2.getStatus()) {
            case AllLose:
            case AllWin:
                gameOver2();
                return;
            default:
                break;
        }
        myGame2.step(Main.SECOND_DELAY);
    }

    private void gameHelp() {
        VBox buttons = new VBox();
        buttons.setPadding(new Insets(100, 100, 100, 120));
        buttons.setSpacing(100);
        Text text = new Text();
        text.setFont(new Font(16));
        text.setWrappingWidth(400);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setText("使用WASD或方向键进行移动\n\n空格键发射子弹\n\n保护你的家并摧毁敌人\n\n\n\n" +
                "作弊操作\n\nB - 无敌五秒\n\nC - 消灭地图上的所有敌方坦克\n\nL - 玩家坦克增加一条生命\n\nN - 跳到下一关");
        Button retMain=uiManager.initReturnButton();
        buttons.getChildren().addAll(retMain);
        buttons.getChildren().addAll(text);
        Scene helpScene = new Scene(buttons, SIZE, SIZE);
        stage.setScene(helpScene);
    }

    public void gameWin() {
    	soundManager.playVictory();
    	Scene winScene = new WinScene(uiManager, SIZE, myGame).initScene();
    	stage.setScene(winScene);
    	clearGame();
    }

    public void gameOver() {
    	soundManager.playDefeat();
    	Scene overScene = new OverScene(uiManager, SIZE, myGame).initScene();
    	stage.setScene(overScene);
    	clearGame();
    }

    public void gameOver2() {
        Scene overScene2 = new OverScene2(uiManager, SIZE, myGame2).initScene();
        stage.setScene(overScene2);
        clearGame();
    }

    public void showLeaders() {
    	Scene leadersScene = new LeadersScene(uiManager, SIZE, myGame).initScene();
    	stage.setScene(leadersScene);
    }

    public void returnGame() {  //增加返回主界面函数
        Scene startScene = new StartScene(uiManager, SIZE).initScene();
        stage.setScene(startScene);
    }

    private void clearGame() {
    	myGame = null;
    	frame = null;
    	if (animation != null) {
            animation.stop();
        }
    	animation = null;
    }

    @Override
    public void start (Stage s) {
        this.stage = s;
        soundManager = new SoundManager();
        //GameUI初始化过程加入返回主菜单键
        uiManager = new GameUI(new GameStart(), new GameTwo(),new GameHelp(),new ShowLeaders(), new GameExit(),
                new GameReturn());
        Scene startScene = new StartScene(uiManager, SIZE).initScene();
        configureStage(startScene);
        stage.show();
    }

    private void configureStage(Scene startScene) {
        stage.setTitle("坦克大战");
        stage.setScene(startScene);
        stage.setResizable(false);
    }
    public static void main (String[] args) {
          launch(args);
    }
}