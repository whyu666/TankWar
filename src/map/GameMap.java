package map;

import java.util.ArrayList;
import sprite.Direction;
import sprite.EnemyTank;
import sprite.PlayerTank;
import sprite.Sprite;
import stable.Brick;
import stable.Grass;
import stable.Home;
import stable.Stable;
import stable.Stone;
import stable.Water;

public class GameMap {

	private static final double SPAWN_POS = 0.0025;
	protected static final int unitSize = 20;

	private final int width, height;
	private ArrayList<Sprite> elements;
	private PlayerTank playerTank;

	private MapData data;
	private Map currentMap;

	public GameMap(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void init(ArrayList<Sprite> elements) {
		this.elements = elements;
		data = new MapData();
	}

    //单人游戏地图
	public void buildMap(int level) {
		pad();
		createMap(level);
	}


	private void pad() {
		double x, y;
		x = -unitSize;
		for (y = 0; y <= height; y += unitSize) {
			addPadding(x, y);
		}
		y = height;
		for (x = 0; x <= width; x += unitSize) {
			addPadding(x, y);
		}
		x = width;
		for (y = 0; y <= height; y+= unitSize) {
			addPadding(x, y);
		}
		y = -unitSize;
		for (x = 0; x <= width; x += unitSize) {
			addPadding(x, y);
		}
	}

	private void addPadding(double x, double y) {
		Stone padding = new Stone();
		padding.setPosition(x, y);
		elements.add(padding);
	}

	private void createMap(int level) {
		currentMap = data.mapWithLevel(level);
		addStable(currentMap.brickPos, Brick.class);
		addStable(currentMap.stonePos, Stone.class);
		addStable(currentMap.grassPos, Grass.class);
		addStable(currentMap.waterPos, Water.class);
		addHome();
		playerTank = revivePlayerTank();
	}

	private void addStable(int[][] pos, Class<? extends Stable> cls) {
		for (int[] chunk: pos) {
			int x = chunk[0];
			int y = chunk[1];
			int chunkWidth = chunk[2];
			int chunkHeight = chunk[3];
			for (int p = x; p <= x + chunkWidth - unitSize; p += unitSize) {
				for (int q = y; q <= y + chunkHeight - unitSize; q += unitSize) {
					Stable e = null;
					try {
						e = cls.newInstance();
					} catch (InstantiationException | IllegalAccessException e1) {
						e1.printStackTrace();
					}
					assert e != null;
					e.setPosition(p, q);
					elements.add(e);
				}
			}
		}
	}

	private void addHome() {
		Home home = new Home();
		home.setPosition(currentMap.homePos[0], currentMap.homePos[1]);
		elements.add(home);
	}
	//添加敌方坦克
	public void spawnTank() {
		for (int[] pos: currentMap.tankPos) {
			if (Math.random() < SPAWN_POS) {
				EnemyTank tank = new EnemyTank(elements);
				tank.setPosition(pos[0], pos[1]);
				boolean valid = true;
				for (Sprite e: elements) {
					if (tank.intersects(e)) {
						valid = false;
					}
				}
				if (valid) {
					tank.setDirection(Direction.DOWN);
					elements.add(tank);
				}
			}
		}
	}

	public PlayerTank getPlayerTank() {
		return playerTank;
	}

    //赋予玩家坦克生命
	public PlayerTank revivePlayerTank() {
		playerTank = new PlayerTank(elements);
		playerTank.setPosition(currentMap.playerPos[0], currentMap.playerPos[1]);
		elements.add(playerTank);
		return playerTank;
	}

	public int numLevels() {
		return data.numLevels();
	}

}