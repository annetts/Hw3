package ee.ut.cs.ds.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import ee.ut.cs.ds.client.characters.AGameCharacter;

public class GameKeyListener implements KeyListener {

	private List<AGameCharacter> characters;

	public GameKeyListener(List<AGameCharacter> characters) {
		this.characters = characters;
    }

	@Override
    public void keyTyped(KeyEvent e) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
	public void keyPressed(KeyEvent e) {
		int target;
		switch (KeyEvent.getKeyText(e.getKeyCode())) {

		case "Up":
			target = characters.get(0).getY() - Constants.CANVAS_HEIGHT / Constants.grid_height;
			if (target > 0 && target < Constants.CANVAS_HEIGHT)
				characters.get(0).setY(target);
			break;
		case "Left":
			target = characters.get(0).getX() - Constants.CANVAS_WIDTH / Constants.grid_width;
			if (target > 0 && target < Constants.CANVAS_WIDTH)
				characters.get(0).setX(target);
			break;
		case "Down":
			target = characters.get(0).getY() + Constants.CANVAS_HEIGHT / Constants.grid_height;
			if (target > 0 && target < Constants.CANVAS_HEIGHT)
				characters.get(0).setY(target);
			break;
		case "Right":
			target = characters.get(0).getX() + Constants.CANVAS_WIDTH / Constants.grid_width;
			if (target > 0 && target < Constants.CANVAS_WIDTH)
				characters.get(0).setX(target);
			break;

		}
	}

	@Override
    public void keyReleased(KeyEvent e) {
	    // TODO Auto-generated method stub
	    
    }

}
