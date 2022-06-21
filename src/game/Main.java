package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import ui.GameUI;
import ui.LeadersScene;
import ui.OverScene;
import ui.SetMapScene;
import ui.SoundManager;
import ui.StartScene;
import ui.WinScene;

public class Main extends Application {

    private static final int SIZE = 680;
    private static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

    private Game myGame;
    private Stage stage;
    private KeyFrame frame;
    private Timeline animation;
    private GameUI uiManager;
    private SoundManager soundManager;

    class GameStart implements EventHandler<ActionEvent> {
    	public void handle(ActionEvent event) {
			gameStart();
		}
    }
    
    class ShowLeaders implements EventHandler<ActionEvent> {
    	public void handle(ActionEvent event) {
			showLeaders();
		}
    }
    
    class GameExit implements EventHandler<ActionEvent> {
    	public void handle(ActionEvent event) {
			stage.close();
		}
    }

    class GameReturn implements EventHandler<ActionEvent> {  //增加返回主界面动作
        public void handle(ActionEvent event) {
            returnGame();
        }
    }

    class GameMap implements EventHandler<ActionEvent> {  //增加返回主界面动作
        public void handle(ActionEvent event) {
            showMap();
        }
    }

    class SaveMap implements EventHandler<ActionEvent> {  //增加返回主界面动作
        public void handle(ActionEvent event) {
            returnGame();
        }
    }

    @Override
    public void start (Stage s) {
    	this.stage = s;
    	soundManager = new SoundManager();
        //GameUI初始化过程加入返回主菜单键
    	uiManager = new GameUI(new GameStart(), new ShowLeaders(), new GameExit(),
                new GameReturn(), new GameMap(), new SaveMap());
    	Scene startScene = new StartScene(uiManager, SIZE).initScene();
    	configureStage(startScene);
    	stage.show();
    }

    private void gameStart() {
        myGame = new Game();
        stage.setTitle(myGame.getTitle());
        uiManager.refreshGame();
        Scene gameScene = myGame.init(SIZE, SIZE);
        stage.setScene(gameScene);
        //设置游戏循环
        frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step());
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
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

    public void showLeaders() {
    	Scene leadersScene = new LeadersScene(uiManager, SIZE, myGame).initScene();
    	stage.setScene(leadersScene);
    }

    public void returnGame() {  //增加返回主界面函数
        Scene startScene = new StartScene(uiManager, SIZE).initScene();
        stage.setScene(startScene);
    }

    public void showMap() {  //增加返回主界面函数
        Scene startScene = new SetMapScene(uiManager, SIZE).initScene();
        stage.setScene(startScene);
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

    private void configureStage(Scene startScene) {
    	stage.setTitle("坦克大战");
    	stage.setScene(startScene);
    	stage.setResizable(false);
    }

    private void clearGame() {
    	myGame = null;
    	frame = null;
    	if (animation != null) {
            animation.stop();
        }
    	animation = null;
    }

    public static void main (String[] args) {
        launch(args);
    }
}