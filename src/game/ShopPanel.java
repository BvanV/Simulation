package game;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;


public class ShopPanel extends JPanel {

	private static final long serialVersionUID = -5478162096705084860L;

	private final JButton upgradeTankBtn = new JButton("Upgrade Tank");
	private final JButton resumeGameBtn = new JButton("Speel Verder");

	
	public ShopPanel(ActionListener a) {
		super();
		add(upgradeTankBtn);
		add(resumeGameBtn);
		upgradeTankBtn.addActionListener(a);
		resumeGameBtn.addActionListener(a);
	}


	public JButton getUpgradeTankBtn() {
		return upgradeTankBtn;
	}


	public JButton getResumeGameBtn() {
		return resumeGameBtn;
	}
	
	
}
