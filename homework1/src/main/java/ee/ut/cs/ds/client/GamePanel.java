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
	private int scale;
	
	public GamePanel(List<AGameCharacter> characters) {
		this.characters = characters;
		
		Thread animation = new Thread(new Runnable() {

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
		
		for (AGameCharacter ch : characters) {
			g.drawImage(ch.getCurrentImage(), ch.getX(), ch.getY(), new ImageObserver() {
				
				@Override
				public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
					setScale(height);
					return true;
				}
			});
		}
		
		setBackground(Color.BLACK);
	}
	
	
	public void setScale(int scale) {
		this.scale = scale;
	}
	
	public int getScale() {
		return scale;
	}

}
