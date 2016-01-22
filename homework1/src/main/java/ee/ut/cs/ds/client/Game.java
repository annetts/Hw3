package ee.ut.cs.ds.client;

import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ee.ut.cs.ds.client.characters.AGameCharacter;
import ee.ut.cs.ds.client.characters.GameFrog;

public class Game {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				List<AGameCharacter> characters = new LinkedList<AGameCharacter>();
				
				// initialize main char
				AGameCharacter ch = new GameFrog();
				ch.setX(Constants.CANVAS_WIDTH/Constants.grid_width/2);
				ch.setY(Constants.CANVAS_HEIGHT/Constants.grid_height/2);
				characters.add(ch);
				
				GamePanel panel = new GamePanel(characters);
				panel.setPreferredSize(new Dimension(Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT));
				panel.addKeyListener(new GameKeyListener(characters));
				panel.setFocusable(true);
				
				initFrame(panel);
			}
		});
	}

	private static void initFrame(GamePanel panel) {
		JFrame frame = new JFrame(Constants.TITLE);
		frame.setContentPane(panel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null); // center the application window
		frame.setVisible(true);
	}

}
