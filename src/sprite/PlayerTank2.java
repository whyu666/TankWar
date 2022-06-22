package sprite;

import game.Game;
import java.util.ArrayList;

public class PlayerTank2 extends Tank {

	private static final long IMMORTAL_DELAY = 0;  //buff持续时间：0秒
	private long immortalStartTime = System.nanoTime();  //生成玩家坦克时，记录时间

	public PlayerTank2(ArrayList<Sprite> elements) {
		super(elements);
		setGreen();
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
		if (s.BITMASK == Game.ENEMY_MISSILE_MASK || s.BITMASK == Game.ENEMY_TANK_MASK) {  //当碰到敌方坦克或子弹时，失去一点生命值
			health--;
		}
	}
}