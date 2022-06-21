package ui;

//游戏ui管理器
//包含公共元素和逻辑
//eventhandler来自main函数中的初始化

import javafx.event.ActionEvent;
import javafx.event.Event;
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
import map.Map;
import misc.LengthLimitedTextField;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class GameUI {

	private final EventHandler<ActionEvent> gameStart;
	private final EventHandler<ActionEvent> showLeaders;
	private final EventHandler<ActionEvent> gameExit;
	private final EventHandler<ActionEvent> gameReturn;
	private final EventHandler<ActionEvent> gameMap;
	private final EventHandler<ActionEvent> saveMap;
	private boolean didInputName;
	private LeaderBoard board;
	private static final String MAP_FILE = "map.save";
	
	public GameUI(EventHandler<ActionEvent> start, EventHandler<ActionEvent> leaders, EventHandler<ActionEvent> exit,
				  EventHandler<ActionEvent> ret, EventHandler<ActionEvent> map, EventHandler<ActionEvent> save) {
		gameStart = start;
		showLeaders = leaders;
		gameExit = exit;
		gameReturn = ret;  //将返回主菜单键加入到GameUI中
		gameMap = map;
		saveMap = save;
		refreshGame();
		setBoard(new LeaderBoard());
	}

	public void refreshGame() {
		didInputName = false;
	}

	public Button initStartButton() {
		Button startButton = new Button("开始游戏");
		startButton.setPrefWidth(120);
		startButton.setOnAction(gameStart);
		return startButton;
	}

	public Button initLeadersButton() {
		Button leadersButton = new Button("排行榜");
		leadersButton.setPrefWidth(120);
		leadersButton.setOnAction(showLeaders);
		return leadersButton;
	}

	public Button initMapButton() {
		Button leadersButton = new Button("地图");
		leadersButton.setPrefWidth(120);
		leadersButton.setOnAction(gameMap);
		return leadersButton;
	}

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
		//排行榜排版解决：仅接受前10个字
		TextField input = new LengthLimitedTextField(10);
		Button confirm = new Button("确认");
		confirm.setOnAction(event -> {
			if (didInputName) {
				indicator.setText("成绩已经保存到排行榜中");
				return;
			}
			didInputName = true;
			String name = input.getText();
			Leader l = new Leader(name, score);
			getBoard().putOn(l);
			getBoard().save();
			indicator.setText("成功添加");
		});
		box.getChildren().addAll(prompt, input, confirm);
		whole.getChildren().addAll(indicator, box);
		return whole;
	}

	public Button initConfirmButton() {
		Button leadersButton = new Button("确认");
		leadersButton.setPrefWidth(120);
		leadersButton.setOnAction(saveMap);
		return leadersButton;
	}

	public Button initReturnButton() {
		Button leadersButton = new Button("返回主界面");
		leadersButton.setPrefWidth(120);
		leadersButton.setOnAction(gameReturn);
		return leadersButton;
	}

	public Button initExitButton() {
		Button exitButton = new Button("退出游戏");
		exitButton.setPrefWidth(120);
		exitButton.setOnAction(gameExit);
		return exitButton;
	}

	public VBox initMapInput() {
    	VBox whole = new VBox();
    	whole.setAlignment(Pos.CENTER);
    	Label indicator = new Label("设置地图");
    	whole.setSpacing(20);
    	HBox box = new HBox();
    	box.setAlignment(Pos.CENTER);
    	box.setPadding(new Insets(15, 12, 15, 12));
    	box.setSpacing(20);
		TextField tank = new TextField();
		TextField brick = new TextField();
		TextField grass = new TextField();
		TextField stone = new TextField();
		TextField water = new TextField();
		TextField home = new TextField();
		TextField player = new TextField();
    	Button confirm = new Button("确认");
    	confirm.setOnAction(event -> {
			String sTank = tank.getText();
			String sBrick = brick.getText();
			String sGrass = grass.getText();
			String sStone = stone.getText();
			String sWater = water.getText();
			String sHome = home.getText();
			String sPlayer = player.getText();
			int [][]tankPos = new int[10][6];
			int [][]brickPos = new int[10][6];
			int [][]grassPos = new int[10][6];
			int [][]stonePos = new int[10][6];
			int [][]waterPos = new int[10][6];
			int []homePos = new int[6];
			int []playerPos = new int[6];
			if (sTank.length() != 0) {
				String[] splitTank = sTank.split(",");
				getMapData(tankPos, splitTank);
			}
			if (sBrick.length() != 0) {
				String[] splitBrick = sBrick.split(",");
				getMapData(brickPos, splitBrick);
			}
			if (sGrass.length() != 0) {
				String[] splitGrass = sGrass.split(",");
				getMapData(grassPos, splitGrass);
			}
			if (sStone.length() != 0) {
				String[] splitStone = sStone.split(",");
				getMapData(stonePos, splitStone);
			}
			if (sWater.length() != 0) {
				String[] splitWater = sWater.split(",");
				getMapData(waterPos, splitWater);
			}
			if (sHome.length() != 0) {
				String[] splitHome = sHome.split(",");
				getMapData(homePos, splitHome);
			}
			if (sPlayer.length() != 0) {
				String[] splitPlayer = sPlayer.split(",");
				getMapData(playerPos, splitPlayer);
			}
			Map map = new Map(tankPos, brickPos, stonePos, waterPos, grassPos, homePos, playerPos);
			save(map);
			indicator.setText("成功添加");
		});
    	box.getChildren().addAll(tank, brick, grass, stone, water, home, player, confirm);
    	whole.getChildren().addAll(indicator, box);
    	return whole;
    }

	private void save(Map newMap) {
		try {
			FileOutputStream fout = new FileOutputStream(MAP_FILE);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(newMap);
			oos.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void getMapData(int[][] pos, String[] split) {
		int j = 0, k = 0;
		for (String s : split) {
			if (k == 4) {
				j++;
				k = 0;
			}
			k++;
			try {
				int temp = Integer.parseInt(s);
				pos[j][k] = temp;
			} catch (Exception e) {
				//System.out.println("ERROR: " + e);
			}
		}
	}

	private void getMapData(int[] pos, String[] split) {
		for (int i = 0; i < split.length; i++) {
			try {
				int temp = Integer.parseInt(split[i]);
				pos[i] = temp;
			} catch(Exception e) {
				//System.out.println("ERROR: " + e);
			}
		}
	}

	public LeaderBoard getBoard() {
		return board;
	}

	private void setBoard(LeaderBoard board) {
		this.board = board;
	}
}