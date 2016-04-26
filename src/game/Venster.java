package game;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class Venster extends JFrame {

	private static final long serialVersionUID = 1346987691759024283L;
	private static final int WIDTH = 1800;
	private static final int HEIGHT = 1000;	
	
	private final JTabbedPane tabbedPane = new JTabbedPane();
	private final NewGamePanel newGamePanel;
	private final GamePanel gamePanel;
	private final ShopPanel shopPanel;
	private final TopScorePanel topScorePanel = new TopScorePanel();
	
	public Venster(String name, ActionListener a) {
		super(name);
		newGamePanel = new NewGamePanel(a);
		gamePanel = new GamePanel(a);
		shopPanel = new ShopPanel(a);
		setSize(new Dimension(WIDTH, HEIGHT));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		tabbedPane.addTab("Nieuw spel",null,newGamePanel, "Start nieuw spel");	
		tabbedPane.addTab("Game", null, gamePanel,"Game");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		gamePanel.setVisible(false);

		tabbedPane.addTab("Shop", null, shopPanel,"Shop");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
		tabbedPane.addTab("Topscores", null, topScorePanel,"Topscores");
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
		this.add(tabbedPane);
		this.setVisible(true);
		tabbedPane.setSelectedIndex(0);
	}
	
	public void addBusStop(int index, BusStop busStop) {
		gamePanel.addBusStop(index, busStop);
	}

	public void moveToTab(int tab) {
		tabbedPane.setSelectedIndex(tab);
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public NewGamePanel getNewGamePanel() {
		return newGamePanel;
	}

	public GamePanel getGamePanel() {
		return gamePanel;
	}

	public JPanel getScorePanel() {
		return shopPanel;
	}

	public TopScorePanel getTopScorePanel() {
		return topScorePanel;
	}

	public ShopPanel getShopPanel() {
		return shopPanel;
	}

}
