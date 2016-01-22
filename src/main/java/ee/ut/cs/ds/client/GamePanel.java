package ee.ut.cs.ds.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.List;

import javax.swing.JPanel;

import ee.ut.cs.ds.client.characters.AGameCharacter;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	
	private List<AGameCharacter> characters;
	private Thread animation;
	
	public GamePanel(List<AGameCharacter> characters) {
		this.characters = characters;

		animation = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					repaint();
					try {
						Thread.sleep(1000 / Constants.FRAME_RATE);
					} catch (InterruptedException e) {
					}
				}
			}
		});
		animation.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		setBackground(Color.BLACK);
		// loop to draw all characters to screen
		for (int i = characters.size() - 1; i >= 0; i--) {
			AGameCharacter ch = characters.get(i);
			
			// draw main char background
			if (i == 0) {
				int posX = ch.getX() * Constants.getPixelRatioX();
				int posY = ch.getY() * Constants.getPixelRatioY();
				g.setColor(Color.WHITE);
				int visibilityArea;
				if (ch.getType().equals("fly")) {
					visibilityArea = 5;
				} else {
					visibilityArea = 3;				
				}
				//TODO background is not perfect centered
				g.drawRect(
						posX - (Constants.getPixelRatioX() * visibilityArea)/2,
						posY - (Constants.getPixelRatioY() * visibilityArea)/2,
						Constants.getPixelRatioX() * visibilityArea,
						Constants.getPixelRatioY() * visibilityArea);	
			}
			
			//TODO if have some time... implement scaling of game screen and all characters
			g.drawImage(ch.getCurrentImage(), (ch.getX() * Constants.getPixelRatioX()) - Constants.getPixelRatioX()/2, (ch.getY() * Constants.getPixelRatioY()) - Constants.getPixelRatioY()/2, new ImageObserver() {
				
				@Override
				public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
					return true;
				}
			});
		}
	}

}
