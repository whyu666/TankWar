package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Welcome scene of the game.
 */
public class StartScene extends GameScene {
	public StartScene(GameUI manager, int SIZE) {
		super(manager, SIZE);
	}
	
	/**
	 * @return the set of buttons on the start scene
	 */
	private Node initStartViewButtons() {
    	VBox buttons = new VBox();
    	
    	buttons.setPadding(new Insets(15, 12, 15, 12));
        buttons.setSpacing(100);
        
        Button startButton = uiManager.initStartButton();
    	
    	Text text = new Text();
		text.setFont(new Font(16));
		text.setWrappingWidth(400);
		text.setTextAlignment(TextAlignment.CENTER);
		text.setText("使用WASD或方向键进行移动\n\n空格键发射子弹\n\n保护你的家并摧毁敌人\n\n\n\n" +
				"作弊操作\n\nB - 无敌五秒\n\nC - 消灭地图上的所有敌方坦克\n\nL - 玩家坦克增加一条生命\n\nN - 跳到下一关");
		
    	buttons.getChildren().addAll(text, startButton);
    	buttons.setAlignment(Pos.CENTER);
    	
    	return buttons;
    }
    
    /**
     * @return the title label on the start scene
     */
    private Label initTitle() {
    	Label title = new Label("欢迎来到坦克大战");
    	title.setFont(new Font(20));
    	title.setPadding(new Insets(15, 15, 15, 15));
    	title.setTextAlignment(TextAlignment.CENTER);
    	return title;
    }
    
    public Scene initScene() {
    	BorderPane root = new BorderPane();
    	
    	Node startViewButtons = initStartViewButtons();
    	Label title = initTitle();
    	
    	root.setTop(title);
    	root.setCenter(startViewButtons);
    	BorderPane.setAlignment(title, Pos.CENTER);
		return new Scene(root, SIZE, SIZE);
    }
}