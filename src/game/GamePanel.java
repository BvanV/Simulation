package game;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class GamePanel extends JPanel {

	private static final long serialVersionUID = 6660497668168234765L;
	private static final int PEOPLE_ON_A_ROW = 20;
	private BufferedImage poppetje = null;
	private BufferedImage smallBus = null;
	
	final JButton tankTenliterBtn = new JButton("Tank 10 l voor $ 10");
	final JButton tankFiftyliterBtn = new JButton("Tank 50 l voor $ 45");
	final JButton goToShopBtn = new JButton("Go To Shop");
	final JLabel moneyLbl = new JLabel("$ 0");
	final JLabel fuelLbl = new JLabel("l: 50");
	final JLabel scoreLbl = new JLabel("Score: 0");
	
	private int xPos = 40;
	private int yPos = 760;
	private int[] waitingPeople = {0,0};
	
	public GamePanel(ActionListener a) {
		super();
		add(moneyLbl);
		add(fuelLbl);
		add(scoreLbl);
		add(tankTenliterBtn);
		add(tankFiftyliterBtn);
		add(goToShopBtn);
		tankTenliterBtn.addActionListener(a);
		tankFiftyliterBtn.addActionListener(a);
		goToShopBtn.addActionListener(a);
		moneyLbl.setBounds(1500, 100, 200, 50);
		fuelLbl.setBounds(1500, 150, 200, 50);
		try {
			poppetje = ImageIO.read(getClass().getResource("./img/poppetje_micro.png"));
			smallBus = ImageIO.read(getClass().getResource("./img/small_bus_small.png"));
		} catch (IOException e) {
			
		}
	}
	
	public void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {}
	}
	
	public void drawWaitingPeople(Graphics g) {
		for(int i=0;i<App.NUMBER_OF_STOPS;i++) {
			for(int j=0;j<waitingPeople[i];j++) {
				g.drawImage(poppetje, App.xPositions[i] + (j % PEOPLE_ON_A_ROW) *8, App.yPositions[i] + 40 + (j/PEOPLE_ON_A_ROW)*20,this);
			}
		}
	}

	
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        drawWaitingPeople(g);
        g.drawImage(smallBus, xPos, yPos, this);
    }

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BufferedImage getPoppetje() {
		return poppetje;
	}

	public BufferedImage getSmallBus() {
		return smallBus;
	}

	public JButton getTankTenliterBtn() {
		return tankTenliterBtn;
	}

	public JButton getTankFiftyliterBtn() {
		return tankFiftyliterBtn;
	}

	public JButton getGoToShopBtn() {
		return goToShopBtn;
	}

	public JLabel getMoneyLabel() {
		return moneyLbl;
	}

	public JLabel getFuelLabel() {
		return fuelLbl;
	}

	public int getxPos() {
		return xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

	public int[] getWaitingPeople() {
		return waitingPeople;
	}

	public void setWaitingPeople(int[] waitingPeople) {
		this.waitingPeople = waitingPeople;
	}

	public JLabel getScoreLbl() {
		return scoreLbl;
	}
	
}
