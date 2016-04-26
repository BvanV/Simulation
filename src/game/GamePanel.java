package game;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class GamePanel extends JPanel {

	private static final long serialVersionUID = 6660497668168234765L;
	private static final int PEOPLE_ON_A_ROW = 20;
	private final BusStop[] busStops = new BusStop[App.NUMBER_OF_STOPS];
	private BufferedImage[] poppetjes = new BufferedImage[4];
	private BufferedImage smallBus = null;
	private BufferedImage smallBusS = null;
	private BufferedImage drivingBus;
	private BufferedImage fuelBar = null;
	
	final JButton tankTenliterBtn = new JButton("Tank 10 l voor $ 10");
	final JButton tankFiftyliterBtn = new JButton("Tank 50 l voor $ 45");
	final JButton tankHundredliterBtn = new JButton("Tank 100 l voor $ 80");
	final JButton activateStop = new JButton("Voeg bushalte toe voor $ 500");
	final JButton goToShopBtn = new JButton("Go To Shop");
	final JLabel moneyLbl = new JLabel("$ 0");
	final JLabel fuelLbl = new JLabel("l: 50");
	final JLabel scoreLbl = new JLabel("Score: 0");
	
	private int xPos = 200;
	private int yPos = 700;
	private int fuelProm = 1000;
	private int fuelSize = 0;
	
	public GamePanel(ActionListener a) {
		super();
		add(moneyLbl);
		add(fuelLbl);
		add(scoreLbl);
		add(tankTenliterBtn);
		add(tankFiftyliterBtn);
		add(tankHundredliterBtn);
		add(activateStop);
		add(goToShopBtn);
		tankTenliterBtn.addActionListener(a);
		tankFiftyliterBtn.addActionListener(a);
		tankHundredliterBtn.addActionListener(a);
		activateStop.addActionListener(a);
		goToShopBtn.addActionListener(a);
		moneyLbl.setBounds(1500, 100, 200, 50);
		fuelLbl.setBounds(1500, 150, 200, 50);
		for(int i=0;i<4;i++) {
			try {
				poppetjes[i] = ImageIO.read(getClass().getResource("./img/poppetje_micro_" + i + ".png"));
			} catch (IOException e) { }
		}
		try {
			smallBus = ImageIO.read(getClass().getResource("./img/small_bus_small.png"));
		}  catch (IOException e) { }
		try {
			smallBusS = ImageIO.read(getClass().getResource("./img/small_bus_small_S.png"));
		}  catch (IOException e) { }
		try {
			fuelBar = ImageIO.read(getClass().getResource("./img/fuel.png"));
		}  catch (IOException e) { }
		drivingBus = smallBus;
	}
	
	public void addBusStop(int index, BusStop busStop) {
		busStops[index] = busStop;
	}
	
	public void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {}
	}
	
	public void drawWaitingPeople(Graphics g) {
		for(int i=0;i<App.NUMBER_OF_STOPS;i++) {
	        Graphics2D g2 = (Graphics2D) g;
	        float thickness = 2;
	        Stroke oldStroke = g2.getStroke();
	        g2.setStroke(new BasicStroke(thickness));
	        final BusStop stop = busStops[i];
	        final int dir = stop.getSquareDir();
	        int x = App.BUS_STOP_X[i] - 65;
	        int y = App.BUS_STOP_Y[i] - 40;
	        if(dir == 1) {
	        	y -= 65; 
	        }
	        if(dir == 2) {
	        	x += 125; 
	        }
	        if(dir == 3) {
	        	y += 75;
	        }
	        if(dir == 4) {
	        	x -= 100;
	        }
	        g2.drawRect(x, y, 162, 100);
	        g2.setStroke(oldStroke);
	        
			final Queue<Man> waitingMen = busStops[i].getWaitSquare().getTravellers();
			int j=0;
			for(Man m : waitingMen) {
				g.drawImage(poppetjes[m.getDestination()], x + 2 + (j % PEOPLE_ON_A_ROW) *8, 
						y + 2 + (j/PEOPLE_ON_A_ROW)*20,this);
				j++;
			}
		}
	}

	
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        drawWaitingPeople(g);
        g.drawImage(drivingBus, xPos, yPos, this);
        final int sx2 = ((fuelProm*(300))/1000);
        final int dx2 = 51 + ((sx2*(5+fuelSize))/5);
        g.drawRect(50, 50, 301, 33);
        g.drawImage(fuelBar,51, 51,dx2, 83,0,0,sx2,32, this);
        if(fuelSize > 0) {
        	g.drawRect(351, 50, 60, 33);
        }
        if(fuelSize > 1) {
        	g.drawRect(411, 50, 60, 33);
        }
        if(fuelSize > 2) {
        	g.drawRect(471, 50, 60, 33);
        }
        if(fuelSize > 3) {
        	g.drawRect(531, 50, 60, 33);
        }
        if(fuelSize > 4) {
        	g.drawRect(591, 50, 60, 33);
        }
        if(fuelSize > 5) {
        	g.drawRect(651, 50, 60, 33);
        }
        if(fuelSize > 6) {
        	g.drawRect(711, 50, 60, 33);
        }
    }

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BufferedImage[] getPoppetjes() {
		return poppetjes;
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

	public JLabel getScoreLbl() {
		return scoreLbl;
	}

	public void setFuelProm(int fuelProm) {
		this.fuelProm = fuelProm;
	}

	public void changeDirection() {
		if(drivingBus == smallBus) {
			drivingBus = smallBusS;
		} else {
			drivingBus = smallBus;
		}
	}

	public void increaseTankSize() {
		fuelSize++;
	}

	public JButton getTankHundredliterBtn() {
		return tankHundredliterBtn;
	}

	public JButton getActivateStop4() {
		return activateStop;
	}

}
