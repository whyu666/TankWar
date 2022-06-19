package ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import leader.Leader;
import leader.LeaderBoard;

/**
 * Common buttons reused across scenes.
 */
public class GameUI {
	private final EventHandler<ActionEvent> gameStart;
	private final EventHandler<ActionEvent> showLeaders;
	private final EventHandler<ActionEvent> gameExit;

	private final EventHandler<ActionEvent> gameReturn;

	private boolean didInputName;
	private LeaderBoard board;
	
	public GameUI(EventHandler<ActionEvent> start, EventHandler<ActionEvent> leaders, EventHandler<ActionEvent> exit, EventHandler<ActionEvent> ret) {
		gameStart = start;
		showLeaders = leaders;
		gameExit = exit;

		//将返回主菜单键加入到GameUI中
		gameReturn = ret;

		refreshGame();
		setBoard(new LeaderBoard());
	}
	
	/**
	 * Refresh the states.
	 */
	public void refreshGame() {
		didInputName = false;
	}

	/**
	 * @return button for game start
	 */
	public Button initStartButton() {
		Button startButton = new Button("开始游戏");
		startButton.setPrefWidth(120);
		startButton.setOnAction(gameStart);
		return startButton;
	}

	/**
	 * @return button for displaying leader board
	 */
	public Button initLeadersButton() {
		Button leadersButton = new Button("排行榜");
		leadersButton.setPrefWidth(120);
		leadersButton.setOnAction(showLeaders);
		return leadersButton;
	}

	public Button initReturnButton() {
		Button leadersButton = new Button("返回主界面");
		leadersButton.setPrefWidth(120);
		leadersButton.setOnAction(gameReturn);
		return leadersButton;
	}

	/**
	 * @return button to quit game
	 */
	public Button initExitButton() {
		Button exitButton = new Button("退出游戏");
		exitButton.setPrefWidth(120);
		exitButton.setOnAction(gameExit);
		return exitButton;
	}
	
	/**
	 * @param score player's score after the game finishes
	 * @return a UI element that let the player input the name
	 */
	public VBox initNameInput(int score) {
    	VBox whole = new VBox();
    	whole.setAlignment(Pos.CENTER);
    	Label indicator = new Label("你的成绩登上了排行榜");
    	whole.setSpacing(20);
    	HBox box = new HBox();
    	box.setAlignment(Pos.CENTER);
    	box.setPadding(new Insets(15, 12, 15, 12));
    	box.setSpacing(20);
    	Label prompt = new Label("姓名:");
    	TextField input = new TextField();
    	Button confirm = new Button("确认");
    	confirm.setOnAction(event -> {
			if (didInputName) {
				indicator.setText("成绩已经保存到排行榜中");
				return;
			}
			didInputName = true;
			Leader l = new Leader(input.getText(), score);
			getBoard().putOn(l);
			getBoard().save();
			indicator.setText("成功添加");
		});
    	box.getChildren().addAll(prompt, input, confirm);
    	whole.getChildren().addAll(indicator, box);
    	return whole;
    }

	/**
	 * @return the leader board object
	 */
	public LeaderBoard getBoard() {
		return board;
	}

	/**
	 * @param board leader board object
	 */
	private void setBoard(LeaderBoard board) {
		this.board = board;
	}
}