package stable;

import sprite.Sprite;

/**
 * Cannot be destroyed whatsoever.
 */
public final class Stone extends Stable {
	
	public Stone() {
		super();
	}

	@Override
	protected void setImageFile() {
		imageFile = "stone.gif";
	}

	@Override
	protected void dealWithCollision(Sprite s) {
	}
}