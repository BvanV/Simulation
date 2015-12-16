import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Controller {
	final static int SCREEN_WIDTH 			= 1800;
	final static int SCREEN_HEIGHT 			= 1000;		
	final static int CONTAINER_WIDTH 		= 8;
	final static int CONTAINER_LENGTH 		= (CONTAINER_WIDTH * 6096) / 2438;
	final static int WATER_HEIGHT			= 300;
	final static int CONTAINER_FIELD_WIDTH 	= 4 * CONTAINER_LENGTH + 4 * CONTAINER_WIDTH;
	final static int CONTAINER_FIELD_OFFSET	= SCREEN_WIDTH - CONTAINER_FIELD_WIDTH - (10 * CONTAINER_LENGTH);
	final static int UPDATE_RATE 			= 30;
	final static int MAX_NUMBER_OF_BLOCKS 	= 10;
	final static int MIN_SHIP_DISTANCE  	= 2 * CONTAINER_LENGTH;
	final private int[] LOSS_LOC_OFFSET		= new int[]{200, 600, 1000, CONTAINER_FIELD_OFFSET};

	private MainPanel mp;

	private Ship[] ships;
	private Crane[] cranes;
    private int[] lossLocation;	
	
	public Controller(MainPanel mainPanel) {
		mp = mainPanel;
    	lossLocation = new int[4];
    	for(int i=0;i<4;i++) {
    		lossLocation[i] = -1;
    	}
	}

    public void start() {
        Thread t = new Thread() {
            public void run() {
                while (true) {
                    update();
                    mp.repaint();
                    try {
                        Thread.sleep(1000 / UPDATE_RATE);
                    } catch (InterruptedException e) { }
                }
            }
        };
        t.start();
    }

    public void update() {
    	if(mp.isMousePressed()) {
    		addShip();
    		mp.setMousePressed(false);
    	}
    	setShipDestinations();
    	ShipMessage[] shmss = mp.pArea.moveShips();
    	sendNewCraneJobs(shmss);
    	mp.pArea.moveCrane();
    }
    
    public static int random(int maxRange) {
        return (int) Math.round((Math.random() * maxRange));
    }
    
    /**
     * Prevent ships from collisions
     */
    public void setShipDestinations() {
    	Ship[] ships = mp.pArea.getShips();
    	for(int i=0;i<ships.length;i++) {
    		if(ships[i] != null) {
	    		int lossLoc = getLossLocation(i);
	    		int prevX = 0;
	    		if(lossLoc < 3) {
	    			if(lossLocation[lossLoc + 1] >= 0 && ships[lossLocation[lossLoc + 1]] != null) {
	    				prevX = ships[lossLocation[lossLoc + 1]].getX();
	    			}
	    		} else {
	    			prevX = SCREEN_WIDTH * 2; 
	    		}
	    		int xLossPlace;
	    		if(lossLoc < 0) {
	    			xLossPlace = 0;
	    		} else {
	    			xLossPlace = LOSS_LOC_OFFSET[lossLoc];
	    		}
	    		int destShip = Math.min(prevX, xLossPlace);
	    		ships[i].setDestX(destShip);
    		}
    	}
    }
    
    public int getLossLocation(int index) {
    	for(int i=0;i<4;i++) {
    		if(lossLocation[i] == index) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    public void sendNewCraneJobs(ShipMessage[] sms) {
    	if(sms != null) {
    		for(int i=0; i<sms.length;i++) {
    			Crane[] cs = mp.pArea.getCranes();
    			if(cs != null && sms[i] != null && sms[i].isRemoveTFE() && sms[i].getShipIndex() == lossLocation[3] ) {
        			mp.pArea.addNewLogText(getShipLogMessage(sms[i]));
        			CraneJob cj = new CraneJob(sms[i].getShipIndex(),
    											mp.pArea.getShips()[sms[i].getShipIndex()].getYsize(),
    											sms[i].getRmTFEX(),
    											sms[i].getRmTFEX(),
    											0,0); 
    				cs[0].addJob(cj);
    				mp.pArea.addNewLogText(getCraneLogMessage(cj));
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
       
    
    /**
     * @return whether it is possible to add a new ship
     */
    public boolean canAddShip() {
    	for(int i=0;i<4;i++) {
    		if(lossLocation[i] != -1) {
    			if(mp.pArea.getShips()[lossLocation[i]].getX() < MIN_SHIP_DISTANCE) {
    				return false;
    			}
    		}
    	}
    	return true;
    }
    
    /**
     * Tries to add ship, if this is possible
     * @return whether a ship is added
     */
    public boolean addShip() {
    	mp.pArea.addNewLogText("Nieuw schip verzoekt toegang");
    	if(lossLocation[0] != -1 || !canAddShip()) {
    		return false;
    	}
    	ShipMessage s = mp.pArea.addShip();
   		if(s != null) {
   			boolean done = false;
   			int i = 3;
   			while(!done && i >= 0) {
   				if(lossLocation[i] == -1) {
   					lossLocation[i] = s.getShipIndex();
   					done = true;
   				}
   				i--;
   			}
   			mp.pArea.addNewLogText(getShipLogMessage(s));
   		}
    	return true;
    }
  
}
