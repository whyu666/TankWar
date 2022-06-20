package sprite;

import java.util.Objects;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;

/**
 * Essential class for object movements and collision.
 */
public abstract class Sprite implements Comparable<Sprite> {
    private Image image;            //图像
    private boolean alive;          //是否存活
    protected double positionX;     //x坐标
    protected double positionY;     //y坐标
    protected double lastX;
    protected double lastY;
    protected double velocityX;     //沿x方向的速度
    protected double velocityY;     //沿y方向的速度
    protected double width;         //长度
    protected double height;        //宽度
    protected int BITMASK;
    protected int health;           //生命值

    public Sprite() {
        alive = true;
        positionX = 0;
        positionY = 0;    
        velocityX = 0;
        velocityY = 0;
        lastX = 0;
        lastY = 0;
        health = 1;
    }

    public void setImage(Image i) {
        image = i;
        width = i.getWidth();
        height = i.getHeight();
    }

    public void setImage(String filename) {
        Image i = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(filename)));
        setImage(i);
    }

    public void setPosition(double x, double y) {
        positionX = x;
        positionY = y;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setVelocity(double x, double y) {
        velocityX = x;
        velocityY = y;
    }
    
    public double getWidth() {
    	return width;
    }
    
    public double getHeight() {
    	return height;
    }

    public void update(double time) {
    	lastX = positionX;
        lastY = positionY;
        positionX += velocityX * time;
        positionY += velocityY * time;
    }
    
    public void lastPosition() {
    	positionX = lastX;
    	positionY = lastY;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(image, positionX, positionY);
    }

    public Rectangle2D getRect() {
        return new Rectangle2D(positionX, positionY, width, height);
    }

    public boolean intersects(Sprite s) {
        return s.getRect().intersects(this.getRect());
    }
    
    /**
     * @param s another sprite collided
     * Handle collision with another sprite.
     */
    public void handleCollision(Sprite s) {
    	dealWithCollision(s);
    	if (health <= 0) {
    		setAlive(false);
    	}
    }

    protected abstract void dealWithCollision(Sprite s);
    
    /**
     * @return if the sprite is alive
     */
    public boolean isAlive() {
    	return alive;
    }

	@Override
	public int compareTo(Sprite o) {  //调用Comparable接口，需要将其中的compareTo实例化
		return 0;
	}

	/**
	 * @return bitmask of the sprite
	 */
	public int getBITMASK() {
		return BITMASK;
	}

	public void setBITMASK(int bITMASK) {
		BITMASK = bITMASK;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
}