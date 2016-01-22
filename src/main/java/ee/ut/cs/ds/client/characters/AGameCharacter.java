package ee.ut.cs.ds.client.characters;

import java.awt.Image;

public abstract class AGameCharacter {


	protected Image[] imgFrames;
	protected String type; //TODO should use enum to avoid accidental false types

	protected int x, y;
	
	/**
	 * Get character y coordinate
	 * @return
	 */
	public int getY() {
		return y;
		}
	
	/**
	 * Set character y coordinate.
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Get character x coordinate.
	 * @return
	 */
	public int getX() {
		return x;
	}

	/**
	 * Set character x coordinate.
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * returns image of character
	 * @return
	 */
	public Image getCurrentImage() {
		return imgFrames[0];
	}
	
	/**
	 * Returns type of character
	 * @return
	 */
	public String getType() {
		return type;
	}

}

