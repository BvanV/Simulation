package game;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;


public class ShopPanel extends JPanel {

	private static final long serialVersionUID = -5478162096705084860L;

	private final ArrayList<JButton> buttons = new ArrayList<>();
	private final JButton upgradeTankBtn 	= new JButton("Upgrade Tank");
	private final JButton addSeatsBtn		= new JButton("Voeg zitplaatsen toe");
	private final JButton resumeGameBtn 	= new JButton("Speel Verder");
	
	public ShopPanel(ActionListener a) {
		super();
		buttons.add(upgradeTankBtn);
		buttons.add(addSeatsBtn);
		buttons.add(resumeGameBtn);
		for(JButton b : buttons) {
			add(b);
			b.addActionListener(a);
		}
	}


	public JButton getUpgradeTankBtn() {
		return upgradeTankBtn;
	}


	public JButton getResumeGameBtn() {
		return resumeGameBtn;
	}


	public JButton getAddSeatsBtn() {
		return addSeatsBtn;
	}
	
	
}
