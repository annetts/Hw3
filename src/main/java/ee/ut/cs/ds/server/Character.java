package ee.ut.cs.ds.server;

import ee.ut.cs.ds.Utils;

public class Character {

	private int y;
	private int x;
	private int points = 0;

	private boolean isMoved;
	
	public enum Characters {
		FROG, FLY
	}
	
	private Characters type;
	private long eatTime;
	private String name;

	public Character(String type) {
		if (type.equals("frog")) {
			this.type = Characters.FROG;
		} else if (type.equals("fly")) {
			this.type = Characters.FLY;
		}
		isMoved = false;
		eatTime = System.currentTimeMillis();
	}
	
	public void setX(int x) {
		this.x = x;		
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public Characters getType() {
		return type;
	}
	
	public void setMoved(boolean isValid) {
		this.isMoved = isValid;
	}
	
	public boolean isMoved() {
		return isMoved;
	}

	public long getEatTime() {
		return eatTime;
	}
	
	public void setEatTime(long eatTime) {
		this.eatTime = eatTime;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
		Utils.logger("Player " + name + " has " + points + " points", false);
	}

	public void setName(String name) {
		this.name = name;
	}

}
