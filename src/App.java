import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Random;

public class App implements Serializable, Runnable, ActionListener {
	
	private static final long serialVersionUID = -2720994541588677008L;
	
	public static final int WAITING = 0;
	public static final int DRIVING = 1;
	public static final int LEAVE_BUS = 2;
	public static final int ENTER_BUS = 3;
	public static final int NUMBER_OF_STOPS = 2;
	public static final int BUS_CAPACITY = 8;
	public static final int FUEL_USE_ON_TRIP = 10;
	public static final int TANK_SIZE = 50;
	public static final int CROSS_TIME = 3000;
	public static final int INTERVAL_TIME = 10;
	
	private boolean stop = false;
	
	private int id;
	private Venster venster;
	
	private boolean startNewGameBtnPressed = false;
	private boolean tankTenLiterBtnPressed = false;
	private boolean tankFiftyLiterBtnPressed = false;
	private boolean goToShopBtnPressed = false;
	
	private int money = 0;
	private int fuel = 50_000;
	
	public static final int[] xPositions = {40, 1600};
	public static final int[] yPositions = {760, 760};
	private int[] maxWaitingPeople = {100, 100};
	private final Random rand = new Random();
	
	private int status = WAITING;
	private int waitTimeLeft = 0;
	private int peopleInBus = 0;
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
		venster = new Venster("Player - "+id, this);
	}
	
	@Override
	public void run() {
		init();
		while(stop == false) {
			checkInput();
			runGame();	
	        venster.getGamePanel().getMoneyLabel().setText("$ "+money);
	        venster.getGamePanel().getFuelLabel().setText("l: "+fuel/1000);
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
			}
			startNewGameBtnPressed = false;
		} else if (tankTenLiterBtnPressed) {
			tank(10,10);
			tankTenLiterBtnPressed = false;
		} else if (tankFiftyLiterBtnPressed) {
			tank(50, 45);
			tankFiftyLiterBtnPressed = false;
		} else if (goToShopBtnPressed) {
			venster.moveToTab(2);
			goToShopBtnPressed = false;
		}
	}
	
	public void runGame() {
		if(waitTimeLeft > 0) {
			waitTimeLeft -= INTERVAL_TIME;
		} else {
			switch(status) {
				case WAITING 	: busWait();	break;										
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
			System.out.println("start");
			startNewGameBtnPressed = true;
		} else if(e.getSource().equals(venster.getGamePanel().getTankTenliterBtn())) {
			System.out.println("10l");
			tankTenLiterBtnPressed = true;
		} else if(e.getSource().equals(venster.getGamePanel().getTankFiftyliterBtn())) {
			System.out.println("50l");
			tankFiftyLiterBtnPressed = true;
		} else if(e.getSource().equals(venster.getGamePanel().getGoToShopBtn())) {
			System.out.println("go2shop");
			goToShopBtnPressed = true;
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
		if(money >= price) {
			money -= price;
			fuel = Math.min(TANK_SIZE * 1_000, fuel + liters * 1_000);
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
			tripStatus = Math.min(1000, tripStatus + (1000 / (CROSS_TIME/INTERVAL_TIME)));
			venster.getGamePanel().setxPos(((1000 - tripStatus) * xPositions[fromPos] + tripStatus * xPositions[toPos]) / 1000);
			venster.getGamePanel().setyPos(((1000 - tripStatus) * yPositions[fromPos] + tripStatus * yPositions[toPos]) / 1000);
		} else {
			tripStatus = 0;
			fromPos = toPos;
			status = LEAVE_BUS;
		}
		final int fuelUse = (1000 * FUEL_USE_ON_TRIP * INTERVAL_TIME) / CROSS_TIME;
		fuel = Math.max(0, fuel - fuelUse);
	}
	
	public void leaveBus() {
		money += peopleInBus * 2;
		peopleInBus = 0;
		status = ENTER_BUS;
		waitTimeLeft += 1000;
	}
	
	public void enterBus() {
		if(fromPos == toPos && fromPos >= 0 && fromPos < venster.getGamePanel().getWaitingPeople().length && 
				venster.getGamePanel().getWaitingPeople()[fromPos] > 0 && peopleInBus < BUS_CAPACITY) {
			final int enteringPeople = Math.min(venster.getGamePanel().getWaitingPeople()[fromPos], BUS_CAPACITY - peopleInBus);
			peopleInBus += enteringPeople;
			venster.getGamePanel().getWaitingPeople()[fromPos] -= enteringPeople;
		}
		waitTimeLeft += 1000;
		status = DRIVING;
		toPos = nextPos();
	}
	
	
	/**
	 * Determine the next destination
	 * currently moving from 0 -> 1 -> 0 -> 1 etc...
	 * @return
	 */
	public int nextPos() {
		return 1 - toPos;
	}
	
	
	private void addWaitingPeople() {
		for(int i=0;i<NUMBER_OF_STOPS;i++) {
			if(rand.nextInt(maxWaitingPeople[i]) > venster.getGamePanel().getWaitingPeople()[i] && rand.nextBoolean() && rand.nextBoolean()) {
				venster.getGamePanel().getWaitingPeople()[i]++;
			}
		}
	}
}
