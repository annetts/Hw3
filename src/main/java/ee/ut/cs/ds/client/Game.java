package ee.ut.cs.ds.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import ee.ut.cs.ds.client.characters.AGameCharacter;

public class Game {

	private static List<AGameCharacter> characters;
	private static Connector con;
	private static GamePanel panel;

	public static void main(String[] args) throws IOException {
		
		characters = new LinkedList<AGameCharacter>();		
		con = new Connector(characters);
		con.startGame();
		
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				
				// initialize gamePanel
				panel = new GamePanel(characters);
				panel.setPreferredSize(new Dimension(Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT));
				panel.setFocusable(true);
				
				// initialize UI panel
				ClientUI uipanel = new ClientUI(con);
				
				initFrame(panel, uipanel);
			}
		});
	}

	private static void initFrame(GamePanel panel, ClientUI uipanel) {
		JFrame frame = new JFrame(Constants.TITLE);
		
		//TODO +40 is just random, idk how to know how much have to be added.
		frame.setSize(Constants.CANVAS_WIDTH + Constants.UI_CANVAS_WIDTH, Constants.CANVAS_HEIGHT +40);
		
		// add panels to frame
		frame.add(uipanel, BorderLayout.WEST);
		frame.add(panel, BorderLayout.EAST);
		// set some frame properties
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null); // center the application window
		frame.setVisible(true);
		bindKeys(panel);
		mapKeys(panel);
	}

	private static void mapKeys(GamePanel panel2) {
		
		panel.getActionMap().put("forward", new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				con.sendPosition(0, -1);
			}
		});
		panel.getActionMap().put("back", new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				con.sendPosition(0, 1);
			}
		});
		panel.getActionMap().put("left", new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				con.sendPosition(-1, 0);
			}
		});
		panel.getActionMap().put("right", new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				con.sendPosition(1, 0);
			}
		});
	}

	private static void bindKeys(GamePanel panel) {
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "forward");
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "back");
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
	}

}
