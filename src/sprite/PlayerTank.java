package sprite;

import java.util.ArrayList;
import game.Game;

public class PlayerTank extends Tank {

	private static final long IMMORTAL_DELAY = 5 * 1000 * 1000 * 100;  //buff持续时间：5秒
	private long immortalStartTime = System.nanoTime();  //生成玩家坦克时，记录时间

	public PlayerTank(ArrayList<Sprite> elements) {
		super(elements);
		setGreen();
		person = 1;
		BITMASK = Game.PLAYER_TANK_MASK;
		buffImmortal();  //初始化时，添加buff
	}
	
	public void update(double time) {
		super.update(time);
		SPEED = 1000;  //控制玩家坦克速度
		if (getDirection() != Direction.NONE) {  //当坦克方向改变时，子弹发射方向也随之改变
			missileDirection = getDirection();
		}
		setDirection(Direction.NONE);
		checkImmortalOut();
	}
	
	private void checkImmortalOut() {
		if (System.nanoTime() - immortalStartTime > IMMORTAL_DELAY * 10) {  //buff持续时间
			debuffImmortal();
		}
	}

	public void fireMissile() {
		//fire();
		long time = System.nanoTime();
		//if (time - fireTime < MISSILE_DELAY) {  //小于连续发射子弹最短时间，不能发射子弹
		//	return;
		//}
		Missile missile = new Missile(missileDirection, getMissileMask(), 1);
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
		//fireTime = time;
		elements.add(missile);
	}
	
	public int getMissileMask() {
		return Game.PLAYER_MISSILE_MASK;
	}
	
	public void buffImmortal() {
		setRed();
		health = Integer.MAX_VALUE;  //在有buff时，处于无敌状态
		immortalStartTime = System.nanoTime();
	}
	
	public void debuffImmortal() {
		health = 1;  //当前生命值为1，碰到敌方坦克或子弹即死
		setGreen();
	}
	
	protected void dealWithCollision(Sprite s) {
		if (s.BITMASK == Game.ENEMY_MISSILE_MASK ||
				s.BITMASK == Game.ENEMY_TANK_MASK) {  //当碰到敌方坦克或子弹时，失去一点生命值
			health--;
		}
	}
}