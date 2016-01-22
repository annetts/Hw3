package ee.ut.cs.ds.client.characters;

import java.awt.Image;

public abstract class AGameCharacter {

	protected Image[] imgFrames;

	protected int x, y;
	
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public Image getCurrentImage() {
		return imgFrames[0];
	}
}

