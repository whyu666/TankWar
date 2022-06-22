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

public class StartScene extends GameScene {

	public StartScene(GameUI manager, int SIZE) {
		super(manager, SIZE);
	}

	private Node initStartViewButtons() {
    	VBox buttons = new VBox();
    	buttons.setPadding(new Insets(15, 12, 15, 12));
        buttons.setSpacing(100);
        Button startButton = uiManager.initStartButton();
		Button Double_game_Btn = uiManager.initDouble_gameButton();
		Button help_Btn=uiManager.init_helpButton();
    	buttons.getChildren().addAll(startButton, Double_game_Btn ,help_Btn);
    	buttons.setAlignment(Pos.CENTER);
    	return buttons;
    }

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