package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class App implements Serializable, Runnable, ActionListener {
	
	private static final long serialVersionUID = -2720994541588677008L;
	
	public static final int WAITING = 0;
	public static final int DRIVING = 1;
	public static final int LEAVE_BUS = 2;
	public static final int ENTER_BUS = 3;
	public static final int NUMBER_OF_STOPS = 4;	
	public static final int[] BUS_STOP_X = {200, 1400, 700, 1400};
	public static final int[] BUS_STOP_Y = {700, 750, 200, 200};
	
	public static final int[] BUS_CAPACITIES = {8, 12, 20, 32, 48, 80};
	public static final int[] BUS_UPGRADE_PRICES = {100, 200, 300, 400, 500};
	
	public static final int[] FUEL_USE_PER_INTERVAL = {50, 55, 65, 80, 100, 125};
	public static final int[] TANK_SIZES = {50, 60, 70, 80, 90, 100, 110, 120};
	public static final int[] TANK_UPGRADE_PRICES = {50, 75, 100, 125, 150, 175, 200};
	public static final int[] ACTIVATE_COSTS = {500, 500, 500, 500};
	
	public static final int BUS_SPEED = 5;
	public static final int CROSS_TIME = 3000;
	public static final int INTERVAL_TIME = 10;
	
	private final Queue<Man> travellersInBus = new LinkedList<Man>();
	
	private boolean stop = false;
	
	private int id;
	private Venster venster;
	
	private final BusStop[] busStops = new BusStop[NUMBER_OF_STOPS];
	private boolean startNewGameBtnPressed 		= false;
	private boolean tankTenLiterBtnPressed 		= false;
	private boolean tankFiftyLiterBtnPressed 	= false;
	private boolean tankHundredLiterBtnPressed 	= false;
	private boolean goToShopBtnPressed 			= false;
	private boolean upgradeTankBtnPressed 		= false;
	private boolean addSeatsBtnPressed 			= false;
	private boolean activateStop4BtnPressed		= false;
	private boolean resumeGameBtnPressed 		= false;
	
	private int money = 0;
	private int fuel = 50_000;
	private int score = 0;
	private int tankSize = 0;
	private int busCapacity = 0;
	
	
	private final Random rand = new Random();
	
	private boolean playing = false;
	private int status = WAITING;
	private int waitTimeLeft = 0;
	private int fromPos = 0;
	private int toPos = 0;
	private int tripStatus = 0;
	
	public App() {
		super();
	}
	
	public static void main(String[] args) {
		(new Thread(new App())).start();
}

	public void init() {
		busStops[0] = new BusStop(0,BUS_STOP_X[0], BUS_STOP_Y[0], 4, 100);
		busStops[1] = new BusStop(1,BUS_STOP_X[1], BUS_STOP_Y[1], 2, 100);
		busStops[2] = new BusStop(2,BUS_STOP_X[2], BUS_STOP_Y[2], 1, 50);
		busStops[3] = new BusStop(3,BUS_STOP_X[3], BUS_STOP_Y[3], 2, 50);

		busStops[0].setActive(true);
		busStops[1].setActive(true);
		busStops[2].setActive(true);
		
		venster = new Venster("Player - "+id, this);
		
		for(int i=0;i<NUMBER_OF_STOPS;i++) {
			venster.addBusStop(i, busStops[i]);
		}
	}
	
	@Override
	public void run() {
		init();
		while(stop == false) {
			checkInput();
			runGame();	
	        venster.getGamePanel().getMoneyLabel().setText("$ " + money);
	        venster.getGamePanel().getFuelLabel().setText("l: " + fuel/1000);
	        venster.getGamePanel().setFuelProm(fuel / TANK_SIZES[tankSize]);
	        venster.getGamePanel().getScoreLbl().setText("Score: " + score);
			try {
				Thread.sleep(INTERVAL_TIME);
			} catch (InterruptedException e) {}
		}
	}

	public void checkInput() {
		if(startNewGameBtnPressed) {
			venster.getNewGamePanel().getNewGameFailedLabel().setText("");
			if(tryToStartNewGame()) {
				venster.setTitle(venster.getNewGamePanel().getNewGameNameTextbox().getText());
				venster.getNewGamePanel().getNewGameNameTextbox().setText("");
				venster.moveToTab(1);
				playing = true;
			}
			startNewGameBtnPressed = false;
		} 
		if (tankTenLiterBtnPressed) {
			tank(10,10);
			tankTenLiterBtnPressed = false;
		} 
		if (tankFiftyLiterBtnPressed) {
			tank(50, 45);
			tankFiftyLiterBtnPressed = false;
		} 
		if (tankHundredLiterBtnPressed) {
			tank(100, 80);
			tankHundredLiterBtnPressed = false;
		} 
		if (goToShopBtnPressed) {
			playing = false;
			venster.moveToTab(2);
			goToShopBtnPressed = false;
		} 
		if(upgradeTankBtnPressed) {
			upgradeTank();
			upgradeTankBtnPressed = false;
		}
		if(addSeatsBtnPressed) {
			addSeats();
			addSeatsBtnPressed = false;
		}
		if(activateStop4BtnPressed) {
			activateStop(3);
			activateStop4BtnPressed = false;
		}
		if(resumeGameBtnPressed) {
			venster.moveToTab(1);
			playing = true;
			resumeGameBtnPressed = false;
		}
	}

	public void runGame() {
		if(!playing) {
			return;
		}
		if(waitTimeLeft > 0) {
			waitTimeLeft -= INTERVAL_TIME;
		} else {
			switch(status) {			
				case WAITING	: busWait();	break;
				case DRIVING 	: moveBus();	break;
				case LEAVE_BUS	: leaveBus();	break;
				case ENTER_BUS 	: enterBus();	break;
			}
		}
		addWaitingPeople();
		venster.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(venster.getNewGamePanel().getNewGameStartButton())) {
			startNewGameBtnPressed = true;
		} else if(e.getSource().equals(venster.getGamePanel().getTankTenliterBtn())) {
			tankTenLiterBtnPressed = true;
		} else if(e.getSource().equals(venster.getGamePanel().getTankFiftyliterBtn())) {
			tankFiftyLiterBtnPressed = true;
		} else if(e.getSource().equals(venster.getGamePanel().getTankHundredliterBtn())) {
			tankHundredLiterBtnPressed = true;			
		} else if(e.getSource().equals(venster.getGamePanel().getGoToShopBtn())) {
			goToShopBtnPressed = true;
		} else if(e.getSource().equals(venster.getGamePanel().getActivateStop4())) {
			activateStop4BtnPressed = true;
		} else if(e.getSource().equals(venster.getShopPanel().getUpgradeTankBtn())) {
			upgradeTankBtnPressed = true;
		} else if(e.getSource().equals(venster.getShopPanel().getAddSeatsBtn())) {
			addSeatsBtnPressed = true;	
		} else if(e.getSource().equals(venster.getShopPanel().getResumeGameBtn())) {
			resumeGameBtnPressed = true;
		} else {
			System.out.println("unknown action recorded on "+ venster.getTitle());
			System.out.println(e);
		}
		
	}
	
	public boolean tryToStartNewGame() {
		if(venster.getNewGamePanel().getNewGameNameTextbox().getText().length() == 0) {
			venster.getNewGamePanel().getNewGameFailedLabel().setText("Voer eerst een naam in om mee te spelen!");
			return false;
		}
		return true;
	}
	
    
	public void tank(int liters, int price) {
		if(useMoney(price)) {
			fuel = Math.min(TANK_SIZES[tankSize] * 1_000, fuel + liters * 1_000);
		}
	}
	
	public void busWait() {	
		waitTimeLeft += 2000;
		status = ENTER_BUS;
	}
	
	/**
	 * Move the bus in the given direction:
	 * 	1: Up
	 *  2: Right
	 *  3: Down
	 *  4: Left
	 * @param dir
	 */
	public void moveBus() {
		if(fuel == 0) {
			return;
		}
		if(tripStatus < 1000) {
			int dist = getDist(fromPos, toPos);
			int extraPromille = (BUS_SPEED * 1000) / dist;
			tripStatus = Math.min(1000, tripStatus + extraPromille);
			venster.getGamePanel().setxPos(((1000 - tripStatus) * BUS_STOP_X[fromPos] + tripStatus * BUS_STOP_X[toPos]) / 1000);
			venster.getGamePanel().setyPos(((1000 - tripStatus) * BUS_STOP_Y[fromPos] + tripStatus * BUS_STOP_Y[toPos]) / 1000);
		} else {
			tripStatus = 0;
			fromPos = toPos;
			status = LEAVE_BUS;
		}
		fuel = Math.max(0, fuel - FUEL_USE_PER_INTERVAL[busCapacity]);
	}
	
	public int getDist(int from, int to) {
		int xs = busStops[from].getX();
		int ys = busStops[from].getY();
		int xd = busStops[to].getX();
		int yd = busStops[to].getY();
		int deltax = xd - xs;
		int deltay = yd - ys;
		return (int)Math.sqrt(deltax*deltax + deltay*deltay);
	}
	
	public void leaveBus() {
		for(Iterator<Man> it = travellersInBus.iterator();it.hasNext();) {
			Man m = it.next();
			if(m.getDestination() == toPos) {
				it.remove();
				addMoney(3);
			}
		}
		status = ENTER_BUS;
		waitTimeLeft += 1000;
	}
	
	/**
	 * Adds money and updates the score if neccessary
	 * @param amount
	 */
	public void addMoney(int amount) {
		money += amount;
		if(money > score) {
			score = money;
		}
	}
	
	/**
	 * checks if there is enough money, and subtract if possible
	 * @param amount request use of money
	 * @return true iff there is enough money, and the amount is subtracted
	 */
	public boolean useMoney(int amount) {
		if(amount > money) {
			return false;
		}
		money -= amount;
		return true;
	}
	
	public void enterBus() {
		if(fromPos == toPos && fromPos >= 0) {
			final Queue<Man> waitingMen = busStops[fromPos].getWaitSquare().getTravellers();
			while(travellersInBus.size() < BUS_CAPACITIES[busCapacity]
					&& waitingMen.size() > 0) {
				final Man m = waitingMen.poll();
				travellersInBus.add(m);
			}
		}
		waitTimeLeft += 1000;
		status = DRIVING;
		toPos = nextPos();
		venster.getGamePanel().changeDirection();
	}
	
	
	/**
	 * Determine the next destination
	 * currently moving from 0 -> 1 -> 0 -> 1 etc...
	 * @return
	 */
	public int nextPos() {
		for(int i=0;i<NUMBER_OF_STOPS;i++) {
			int goal = toPos + 1 + i;
			if(busStops[goal % NUMBER_OF_STOPS].isActive()) {
				return (goal % NUMBER_OF_STOPS);
			}
		}
		System.err.println("Could not find an active BusStop to move to");
		return -1;
	}
	
	
	public void addWaitingPeople() {
		for(BusStop b : busStops) {
			final WaitSquare ws = b.getWaitSquare();
			final int dest = rand.nextInt(NUMBER_OF_STOPS);
			if(rand.nextInt(ws.getCapacity()) > ws.getTravellers().size() 
					&& rand.nextInt(1000) < 30
					&& dest != b.getIndex()
					&& b.isActive()
					&& busStops[dest].isActive()) {
				ws.getTravellers().add(new Man(dest));	
			}
		}
	}
	
	public void upgradeTank() {
		if(tankSize < TANK_UPGRADE_PRICES.length && useMoney(TANK_UPGRADE_PRICES[tankSize])) {
			tankSize++;
			venster.getGamePanel().increaseTankSize();
		}
	}
	
	public void addSeats() {
		if(busCapacity < BUS_UPGRADE_PRICES.length && useMoney(BUS_UPGRADE_PRICES[busCapacity])) {
			busCapacity++;
		}
	}
	
	public void activateStop(int stop) {
		if(useMoney(ACTIVATE_COSTS[stop])) {
			busStops[stop].setActive(true);
		}
	}
}
