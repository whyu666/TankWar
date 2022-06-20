package sprite;

import game.Game;
import java.util.ArrayList;

public class EnemyTank extends Tank {
	private long lastChangeDirection = System.nanoTime();
	private static final long DIRECTION_DELAY = 1000 * 100000L;	//变换方向时间，单位ms
	private static final double DIRECTION_CHANGE_POS = 0.1;	//更改方向的概率
	private static final double FIRE_MISSILE_POS = 0.02;		//发射子弹的概率
	
	public EnemyTank(ArrayList<Sprite> elements) {
		super(elements);
		SPEED = 100;  //敌方坦克速度
	}

	public void update(double time) {
		super.update(time);
		attemptChangeDirection();
		fireMissileRandom();
		missileDirection = getDirection();
	}
	
	public int getMissileMask() {
		return Game.ENEMY_MISSILE_MASK;
	}
	
	private void attemptChangeDirection() {
		if (Math.random() < DIRECTION_CHANGE_POS  && System.nanoTime() - lastChangeDirection > DIRECTION_DELAY) {
			changeRandomDirection();
		}
	}
	
	private void changeRandomDirection() {
		double playerPositionX = Game.playerTankPositionX();
		double playerPositionY = Game.playerTankPositionY();
		double enemyPositionX = getPositionX();
		double enemyPositionY = getPositionY();
		int dir = (int)(Math.random() * 2);
		if (enemyPositionY - playerPositionY > 0 && enemyPositionX - playerPositionX > 0) {
			if (dir == 0) {
				setDirection(Direction.UP);
			}
			else {
				setDirection(Direction.LEFT);
			}
		}
		if (enemyPositionY - playerPositionY > 0 && enemyPositionX - playerPositionX < 0) {
			if (dir == 0) {
				setDirection(Direction.UP);
			}
			else {
				setDirection(Direction.RIGHT);
			}
		}
		if (enemyPositionY - playerPositionY < 0 && enemyPositionX - playerPositionX > 0) {
			if (dir == 0) {
				setDirection(Direction.DOWN);
			}
			else {
				setDirection(Direction.LEFT);
			}
		}
		if (enemyPositionY - playerPositionY < 0 && enemyPositionX - playerPositionX < 0) {
			if (dir == 0) {
				setDirection(Direction.DOWN);
			}
			else {
				setDirection(Direction.RIGHT);
			}
		}
		lastChangeDirection = System.nanoTime();
	}
	
	private void fireMissileRandom() {
		if (Math.random() < FIRE_MISSILE_POS) {
			fireMissile();
		}
	}
	
	protected void dealWithCollision(Sprite s) {
		if (s.BITMASK == Game.PLAYER_MISSILE_MASK || s.BITMASK == Game.PLAYER_TANK_MASK) {
			health--;
		}
	}
}