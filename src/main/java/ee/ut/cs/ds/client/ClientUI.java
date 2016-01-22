package ee.ut.cs.ds.client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import ee.ut.cs.ds.Utils;

public class ClientUI extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	public ClientUI(Connector con) {
		init(con);
	}

	private void init(Connector con) {
		setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);

		addElement(new JLabel("Enter name:"), constraints, 0, 0);
		JTextField textNameInput = (JTextField) addElement(new JTextField(20), constraints, 0, 1);
		addElement(new JLabel("Enter ip: "), constraints, 0, 2);
		JTextField textIpInput = (JTextField) addElement(new JTextField(20), constraints, 0, 3);
		JRadioButton fly = (JRadioButton) addElement( new JRadioButton("Fly"), constraints, 0, 4);
		JRadioButton frog = (JRadioButton) addElement(new JRadioButton("Frog"), constraints, 0, 5);
		JButton buttonLogin = (JButton) addElement(new JButton("Submit"), constraints, 0, 6);

		constraints.anchor = GridBagConstraints.CENTER;
		add(buttonLogin, constraints);
		
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Enter Game info"));
		
		fly.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frog.setSelected(false);
			}
		});
		
		frog.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fly.setSelected(false);
			}
		});
		
		buttonLogin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// if ip is set "". Use null value
					if (!con.isConnected()) {
						con.connect(textIpInput.equals("") ? null : textIpInput.getText());		
					}
				
					if (fly.isSelected()) {
						con.sendCharacter("fly");
						fly.setEnabled(false);
						frog.setEnabled(false);
					} else if (frog.isSelected()) {
						con.sendCharacter("frog");
						fly.setEnabled(false);
						frog.setEnabled(false);
					}	
					
					if (con.isConnected() && (frog.isSelected() || fly.isSelected())) {
						buttonLogin.setEnabled(false);
						textNameInput.setEnabled(false);
						textIpInput.setEnabled(false);
						ClientUI.this.setEnabled(false);
					} else 	if (con.isConnected()) {
						ClientUI.this.setEnabled(false);
						textNameInput.setEnabled(false);
						textIpInput.setEnabled(false);
					} 

				} catch (IOException e1) {
					Utils.logger("Could not connect to server: " + textIpInput.getText(), true);
				}
			}
		});
		setVisible(true);
	}

	private JComponent addElement(JComponent element, GridBagConstraints constraints, int x, int y) {
		constraints.gridy = y;
		constraints.gridx = x;
		add(element, constraints);
		return element;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

}
