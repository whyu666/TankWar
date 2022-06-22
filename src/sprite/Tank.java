package sprite;

import game.Game;
import java.util.ArrayList;
import stable.Grass;

public abstract class Tank extends Sprite {

	private static final long MISSILE_DELAY = 200 * 1000000L;  //两颗子弹间隔时间
	protected int SPEED = 300;  //默认坦克速度（玩家坦克速度）
	protected String TANK_UP, TANK_DOWN, TANK_LEFT, TANK_RIGHT;
	private Direction direction = Direction.UP;  //默认坦克方向向上
	protected Direction missileDirection = getDirection();  //坦克发射子弹的方向即为坦克方向
	private long fireTime = System.nanoTime();
	protected final ArrayList<Sprite> elements;
	
	public Tank(ArrayList<Sprite> elements) {
		this.elements = elements;
		setWhite();
		setImage(TANK_UP);
		BITMASK = Game.ENEMY_TANK_MASK;  //默认初始化为敌方坦克
	}
	
	public Tank(ArrayList<Sprite> elements, int mask) {
		this(elements);
		BITMASK = mask;  //初始化为指定坦克
	}
	
	public void update(double time) {
		setSpeedWithDirection();
		setImageWithDirection();
		super.update(time);
	}
	
	public void setDirection(Direction dir) {
		this.direction = dir;
	}
	
	private void setSpeedWithDirection() {
		switch (getDirection()) {
		case RIGHT:
			velocityX = SPEED;
			velocityY = 0;
			break;
		case LEFT:
			velocityX = -SPEED;
			velocityY = 0;
			break;
		case UP:
			velocityX = 0;
			velocityY = -SPEED;
			break;
		case DOWN:
			velocityX = 0;
			velocityY = SPEED;
			break;
		default:
			velocityX = 0;
			velocityY = 0;
			break;
		}
	}
	
	private void setImageWithDirection() {
		switch (missileDirection) {
		case RIGHT:
			setImage(TANK_RIGHT);
			break;
		case LEFT:
			setImage(TANK_LEFT);
			break;
		case UP:
			setImage(TANK_UP);
			break;
		case DOWN:
			setImage(TANK_DOWN);
			break;
		default:
			break;
		}
	}
	
	public void fire() {
		long time = System.nanoTime();
		if (time - fireTime < MISSILE_DELAY) {  //小于连续发射子弹最短时间，不能发射子弹
			return;
		}
		Missile missile = new Missile(missileDirection, getMissileMask());
		switch (missileDirection) {
			case UP:
				missile.setPosition(positionX + 0.5 * width - 0.5 * missile.width, positionY - missile.height);
				break;
			case DOWN:
				missile.setPosition(positionX + 0.5 * width - 0.5 * missile.width, positionY + height);
				break;
			case LEFT:
				missile.setPosition(positionX - missile.width, positionY + 0.5 * height - 0.5 * missile.height);
				break;
			case RIGHT:
				missile.setPosition(positionX + width, positionY + 0.5 * height - 0.5 * missile.height);
				break;
		}
		fireTime = time;
		elements.add(missile);
	}
	
	public void handleCollision(Sprite s) {
		if (BITMASK == s.BITMASK) {  //是同一个类，直接获取lastPosition
			lastPosition();
		}
		else if (s.BITMASK == Game.STABLE_MASK && !(s instanceof Grass)) {  //s是stable类但不是草类，直接获取lastPosition
			lastPosition();
		}
		super.handleCollision(s);  //调用父类的碰撞事件
	}

	public abstract int getMissileMask();

	public void setGreen() {
		TANK_UP = GREEN_TANK_UP;
		TANK_DOWN = GREEN_TANK_DOWN;
		TANK_LEFT = GREEN_TANK_LEFT;
		TANK_RIGHT = GREEN_TANK_RIGHT;
	}

	public void setRed() {
		TANK_UP = RED_TANK_UP;
		TANK_DOWN = RED_TANK_DOWN;
		TANK_LEFT = RED_TANK_LEFT;
		TANK_RIGHT = RED_TANK_RIGHT;
	}

	public void setYellow() {
		TANK_UP = YELLOW_TANK_UP;
		TANK_DOWN = YELLOW_TANK_DOWN;
		TANK_LEFT = YELLOW_TANK_LEFT;
		TANK_RIGHT = YELLOW_TANK_RIGHT;
	}

	public void setWhite() {
		TANK_UP = WHITE_TANK_UP;
		TANK_DOWN = WHITE_TANK_DOWN;
		TANK_LEFT = WHITE_TANK_LEFT;
		TANK_RIGHT = WHITE_TANK_RIGHT;
	}

	public Direction getDirection() {
		return direction;
	}

	//资源文件信息
	protected static final String WHITE_TANK_UP = "white-tank-up.gif";
	protected static final String WHITE_TANK_DOWN = "white-tank-down.gif";
	protected static final String WHITE_TANK_LEFT = "white-tank-left.gif";
	protected static final String WHITE_TANK_RIGHT = "white-tank-right.gif";
	protected static final String GREEN_TANK_UP = "green-tank-up.gif";
	protected static final String GREEN_TANK_DOWN = "green-tank-down.gif";
	protected static final String GREEN_TANK_LEFT = "green-tank-left.gif";
	protected static final String GREEN_TANK_RIGHT = "green-tank-right.gif";
	protected static final String RED_TANK_UP = "red-tank-up.gif";
	protected static final String RED_TANK_DOWN = "red-tank-down.gif";
	protected static final String RED_TANK_LEFT = "red-tank-left.gif";
	protected static final String RED_TANK_RIGHT = "red-tank-right.gif";
	protected static final String YELLOW_TANK_UP = "yellow-tank-up.gif";
	protected static final String YELLOW_TANK_DOWN = "yellow-tank-down.gif";
	protected static final String YELLOW_TANK_LEFT = "yellow-tank-left.gif";
	protected static final String YELLOW_TANK_RIGHT = "yellow-tank-right.gif";
}