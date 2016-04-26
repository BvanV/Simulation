import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class NewGamePanel extends JPanel {

	private static final long serialVersionUID = -5484579341901812018L;
	private final JTextField newGameNameTextbox = new JTextField(10);
	private final JButton newGameStartButton = new JButton("Start");
	private final JLabel newGameNameLabel = new JLabel("Naam");
	private final JLabel newGameFailedLabel = new JLabel("");
	
	public NewGamePanel(ActionListener al) {
		super();
		add(newGameNameLabel);
		add(newGameNameTextbox);
		add(newGameFailedLabel);
		add(newGameStartButton);
		newGameStartButton.addActionListener(al);
		newGameNameLabel.setBounds(20, 20, 100, 50);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JTextField getNewGameNameTextbox() {
		return newGameNameTextbox;
	}

	public JButton getNewGameStartButton() {
		return newGameStartButton;
	}

	public JLabel getNewGameNameLabel() {
		return newGameNameLabel;
	}

	public JLabel getNewGameFailedLabel() {
		return newGameFailedLabel;
	}
	
}
