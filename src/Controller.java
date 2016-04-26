import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;

public class Controller implements Serializable {

	private static final long serialVersionUID = 3578196269655089020L;
	final static int SCREEN_WIDTH 			= 1800;
	final static int SCREEN_HEIGHT 			= 1000;		
	final static int CONTAINER_WIDTH 		= 8;
	final static int CONTAINER_LENGTH 		= (CONTAINER_WIDTH * 6096) / 2438;
	final static int WATER_HEIGHT			= 300;
	final static int CONTAINER_FIELD_WIDTH 	= 4 * CONTAINER_LENGTH + 4 * CONTAINER_WIDTH;
	final static int CONTAINER_FIELD_OFFSET	= 1080;
	final static int UPDATE_RATE 			= 30;
	final static int MAX_NUMBER_OF_SHIPS 	= 10;
	final static int MIN_SHIP_DISTANCE  	= 2 * CONTAINER_LENGTH;
	final static int NUMBER_OF_CRANES		= 1;
	final static int LOG_SIZE 				= 10;
	final static int NUMBER_OF_LOCATIONS	= 7;
	final static String LOG_TEXT_HEADER 	= "Control log"; 
	final private int[] LOSS_LOC_OFFSET		= new int[]{-400, 0, 360, 720, CONTAINER_FIELD_OFFSET, 1440, SCREEN_WIDTH};
	
	
	private int removeColor = 3;
	private MainPanel mp;

	
	private Ship[] ships;
	private Crane[] cranes;
	private Field containerField;
    private int[] lossLocation;	
    private LossList[] lossLists;
    
	public Controller(){
		containerField = new Field(CONTAINER_FIELD_OFFSET, WATER_HEIGHT,200, 500, 255, 204, 153, 4, 50);
    	cranes = new Crane[1];
    	cranes[0] = new Crane(0, CONTAINER_FIELD_OFFSET + 20, WATER_HEIGHT + 10 * CONTAINER_WIDTH);
    	cranes[0].setContainerField(containerField);
    	ships = new Ship[MAX_NUMBER_OF_SHIPS];
    	lossLists = new LossList[MAX_NUMBER_OF_SHIPS];
    	
		mp =  new MainPanel(ships, cranes, containerField);
    	lossLocation = new int[NUMBER_OF_LOCATIONS];
    	for(int i=0;i<NUMBER_OF_LOCATIONS;i++) {
    		lossLocation[i] = -1;
    	}
	}

	public void runAgain() {
        update();
        mp.repaint();
	}
	
    public void start() {
        Thread t = new Thread() {
            public void run() {
            }
        };
        t.start();
    }

    public void update() {
    	checkButtonPressed();
    	updateShips();
    	updateCranes();
    	collectShipMessages();
    	collectCraneMessages();
    }
    
    public void checkButtonPressed() {
    	if(mp.isMousePressed()) {
    		addShip();
    		mp.setMousePressed(false);
    	}
    }
    
    public void updateShips() {
    	//TODO change setShipDestinations()
    	setShipDestinations();
    	moveShips();
    }
    
    public void updateCranes() {
       	moveCrane();
    }
    
    public void collectShipMessages() {
    	//TODO collect ship messages and handle them.
    }
    
    public void collectCraneMessages() {
    	//TODO collect Crane messages and handle them
    }
    
    /**
     * pre: All conditions of j are satisfied
     * @param j the job
     */
    public void applyJob(Job j) {
    	if(j instanceof MoveJob) {
    		ships[((MoveJob) j).getShip()].setDestX(((MoveJob) j).getDestX());
    	}
    }
    
    /**
     * Checks all the conditions in a list
     * @param conditions
     * @return true iff all conditions are met or if the list is empty
     */
	public boolean allConditionsMet(Condition[] conditions) {
		if(conditions == null) {
			return true;
		}
		for(int i=0;i<conditions.length;i++) {
			if(conditions[i] != null && !checkCondition(conditions[i])) {
				return false;
			}
		}
		return true;
	}
    
	/**
	 * checks if a condition is met
	 * @param c	the condition
	 * @return	whether the condition is met
	 */
    public boolean checkCondition(Condition c) {
    	if(c instanceof LocationReservationCondition) {
    		LocationReservationCondition cond = (LocationReservationCondition) c;
    		int loc = cond.getReservedLocation();
    		int ship = cond.getShipIndex();
    		return lossLocation[loc] == ship;    		
    	}
    	return c.isMet();
    }
    
    /**
     * move the crane
     */
    public void moveCrane() {
    	CraneMessage cm = cranes[0].move();
    	if(cm != null) {
    		mp.addNewLogText("Kraan 0: Container("+cm.getxOnShip()+ ", "+cm.getyOnShip()+") verwijderd van schip "+lossLocation[3]);
    		int shIndex = lossLocation[3];
			boolean done = false;
			int j = 0;
			while(!done && j<ships[shIndex].getBlokjes().length) {
				if(ships[lossLocation[3]].getTFE(j) != null && ships[shIndex].getTFE(j).getColor() == removeColor) {
					CraneJob cj = new CraneJob(ships[shIndex],
												shIndex,
												ships[shIndex].getYsize(),
												j % ships[shIndex].getXsize(),
												j / ships[shIndex].getXsize(),
												containerField.getNextFreeX(),
												containerField.getNextFreeY());
					containerField.reserveNext();
					cranes[0].addJob(cj);
					mp.addNewLogText(getCraneLogMessage(cj));
					done = true;
				}
				j++;
			}
			if(!done) {
				ships[shIndex].setDestX(SCREEN_WIDTH);
				ships[shIndex].setStatus(1);
				lossLocation[3] = -1;
				lossLocation[4] = shIndex;
			}
    	}
    }
    
    /**
     * Prevent ships from collisions
     */
    public void setShipDestinations() {
    	for(int i=0;i<ships.length;i++) {
    		if(ships[i] != null && ! ships[i].isLock()) {
	    		int lossLoc = getLossLocation(i);
	    		if(lossLoc == NUMBER_OF_LOCATIONS - 1) {
	    			ships[i] = null;
	    			lossLocation[lossLoc] = -1;
	    			mp.addNewLogText("Schip "+i+" heeft de haven verlaten.");
	    		} else if(ships[i].getX() >= LOSS_LOC_OFFSET[lossLoc + 1]) {
	    			lossLocation[lossLoc] = -1;
	    			mp.addNewLogText("Schip "+i+" heeft vak "+lossLoc+" verlaten.");
	    		}
	    		if(ships[i] != null) {
	    			lossLoc = getLossLocation(i);
	    			if(lossLoc < NUMBER_OF_LOCATIONS && lossLocation[lossLoc + 1] == -1) {
	    				lossLocation[lossLoc + 1] = i;
	    				ships[i].setDestX(LOSS_LOC_OFFSET[lossLoc + 1]);
	    				mp.addNewLogText("Schip "+i+" heeft vak "+(lossLoc+1)+" gereserveerd.");
	    			}
	    			if(lossLoc == NUMBER_OF_LOCATIONS - 3) {
	    				ships[i].setLock(true);
	    				mp.addNewLogText("Schip "+i+" kan gelost worden.");
	    			}
	    		}
    		}
    	}
    }
    
    public int getLossLocation(int index) {
    	for(int i=0;i<NUMBER_OF_LOCATIONS;i++) {
    		if(lossLocation[i] == index) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    public void sendNewCraneJobs(ShipMessage[] sms) {
    	if(sms != null) {
    		for(int i=0; i<sms.length;i++) {
    			if(cranes != null && sms[i] != null && sms[i].isRemoveTFE() && sms[i].getShipIndex() == lossLocation[3] ) {
    				int shIndex = sms[i].getShipIndex();
        			boolean done = false;
        			int j = 0;
        			while(!done && j<ships[shIndex].getBlokjes().length) {
      					if(ships[shIndex].getTFE(j) != null && ships[shIndex].getTFE(j).getColor() == removeColor) {
      						CraneJob cj = new CraneJob(ships[shIndex],
        										shIndex,
    											ships[shIndex].getYsize(),
    											j % ships[shIndex].getXsize(),
    											j / ships[shIndex].getXsize(),
    											containerField.getNextFreeX(),
												containerField.getNextFreeY()); 
      						containerField.reserveNext();
      						cranes[0].addJob(cj);
      						mp.addNewLogText(getCraneLogMessage(cj));
      						done = true;
      					}
      					j++;
        			}
    			}
    		}
    	}
    }
    
    public String getCraneLogMessage(CraneJob c) {
    	return "Kraan 0: Los schip " + c.getShipIndex() + ": "
    			+ "verplaats container S("+ c.getTFEOnBoardX() + ", " + c.getTFEOnBoardY() + ") "
    			+ "naar vak H(" + c.getTFEOnFieldX() + ", " + c.getTFEOnFieldY() +").";
    }
    
    public String getShipLogMessage(ShipMessage s) {
    	if(s.isRemoveTFE()) {
    		return "Schip " + s.getShipIndex() + ": "
    				+ "verwijder container met coordinaten " +
    				"(" + s.getRmTFEX() + ", " + s.getRmTFEY() + ").";
    	}
    	if(s.isEntersArea()) {
    		return "<b>Schip " + s.getShipIndex() + "</b> vaart de haven binnen.";
    	}
    	return "";
    }
       
//    
//    
//    /**
//     * @return whether it is possible to add a new ship
//     */
//    public boolean canAddShip() {
//    	for(int i=0;i<NUMBER_OF_LOCATIONS;i++) {
//    		if(lossLocation[i] != -1) {
//    			if(ships[lossLocation[i]].getX() < MIN_SHIP_DISTANCE) {
//    				return false;
//    			}
//    		}
//    	}
//    	return true;
//    }
    
    /**
     * Tries to add ship, if this is possible
     * @return whether a ship is added
     */
    public boolean addShip() {
    	mp.addNewLogText("Nieuw schip verzoekt toegang");
    	if(lossLocation[0] != -1) {
    		System.out.println("Check1: false");
    		return false;
    	}
    	System.out.println("Check1: OK");
    	Ship newShip = createShip();
    	if(newShip == null) {
    		return false;
    	}
    	return true;
    }

    /**
     * Add a Ship to the panel
     */
    public Ship createShip() {
		int i = 0;
		while(i<MAX_NUMBER_OF_SHIPS) {
			if(ships[i] == null) {
				int ysize	= 2 + random(3);
		    	int xsize	= 2*ysize + random(2);
		    	int length	= (xsize + 1) * (CONTAINER_LENGTH);
		    	int width 	= (ysize + 1) * (CONTAINER_WIDTH);
		    	int ypos	= WATER_HEIGHT - width;
				ships[i] = new Ship(ysize, xsize, length, width, ypos);
		    	lossLocation[0] = i;
		    	lossLists[i] = new LossList(ships[i]);
		    	//TODO decide which TFE's to loss
		    	lossLists[i].addTFEToLoss(0, 0);
		    	lossLists[i].addTFEToLoss(1, 0);
		    	lossLists[i].addTFEToLoss(1, 1);
		    	lossLists[i].addTFEToLoss(2, 1);
				return ships[i];
			}
			i++;
		}
		return null;
    }
          
    public ShipMessage[] moveShips() {
    	ArrayList<ShipMessage> shipMessages = new ArrayList<ShipMessage>();
	   	for(int i=0;i<MAX_NUMBER_OF_SHIPS;i++) {
			if(ships[i] != null) {
				if(ships[i].getX() < SCREEN_WIDTH) {
					ShipMessage sm = ships[i].move();
					if(sm != null) {
						shipMessages.add(sm);
					}
				} else if (ships[i].getInactiveTime() < 10) {
					ships[i].setInactiveTime(ships[i].getInactiveTime() + 1);
				} else {
					ships[i] = null;
				}
			}
		}
	   	return shipMessages.toArray(new ShipMessage[1]);
    }
   
    public Dimension getPreferredSize() {
        return (new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
    }
    
    public static int random(int maxRange) {
        return (int) Math.round((Math.random() * maxRange));
    }

	public synchronized MainPanel getMp() {
		System.out.println("Tried to acces Mp");
		return mp;
	}

	public void setMp(MainPanel mp) {
		this.mp = mp;
	}

    
}
