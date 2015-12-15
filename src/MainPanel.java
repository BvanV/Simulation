import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import javax.swing.JPanel;

/**
 * 
 * @author Bart van Vuuren
 * 
 * Newest functionalities
 * 
 * v1.00 added lanes for the blocks to move in
 * v1.01 added large blocks
 * v1.02 fixed return rules
 * v1.03 Avoid collisions
 * v1.04 Added block destination
 * v1.05 Simplified: blocks only move to the right, field are larger, the maximum amount of blocks is fixed
 * v1.06 using less magic numbers, created subclass Ship
 * v1.07 Collision avoidance by claiming fields. Ships move to destination
 * v1.08 Build a crane 
 * 
 * v2.00 Move the crane to the 'first' container of a Ship
 * v2.01 Remove a container from a ship
 * v2.02 Place container on the field and let the ship continue
 * 
 * v3.00 Code improvements
 * 
 */
public class MainPanel extends JPanel implements MouseListener {

	private static final long serialVersionUID = -7593664380166699676L;

	final static int SCREEN_WIDTH 			= 1800;
	final static int SCREEN_HEIGHT 			= 1000;		
	final static int CONTAINER_WIDTH 		= 8;
	final static int CONTAINER_LENGTH 		= (CONTAINER_WIDTH * 6096) / 2438;
	final static int WATER_HEIGHT			= 300;
	final static int CONTAINER_FIELD_WIDTH 	= 4 * CONTAINER_LENGTH + 4 * CONTAINER_WIDTH;
	final static int CONTAINER_FIELD_OFFSET	= SCREEN_WIDTH - CONTAINER_FIELD_WIDTH - (10 * CONTAINER_LENGTH);
	final static int UPDATE_RATE 			= 30;
	final static int MAX_NUMBER_OF_BLOCKS 	= 10;
	final private int[] LOSS_LOC_OFFSET		= new int[]{200, 600, 1000, CONTAINER_FIELD_OFFSET};
	
    private CustomContainer container;
    private PaintArea pArea;
    private WaterSpace waterSpace;
    private int[] lossLocation;
    

    public static int random(int maxRange) {
        return (int) Math.round((Math.random() * maxRange));
    }

    public MainPanel() {
    	lossLocation = new int[4];
    	for(int i=0;i<4;i++) {
    		lossLocation[i] = -1;
    	}
    	waterSpace = new WaterSpace(null, null, SCREEN_WIDTH);
        container = new CustomContainer(SCREEN_HEIGHT, SCREEN_WIDTH);
        pArea = new PaintArea(SCREEN_WIDTH, SCREEN_HEIGHT, container);
        this.setLayout(new BorderLayout());
        this.add(pArea, BorderLayout.CENTER);
        this.addMouseListener(this);
        start();
    }

    public void start() {
        Thread t = new Thread() {
            public void run() {
                while (true) {
                    update();
                    repaint();
                    try {
                        Thread.sleep(1000 / UPDATE_RATE);
                    } catch (InterruptedException e) { }
                }
            }
        };
        t.start();
    }

    public void update() {
    	setShipDestinations();
    	ShipMessage[] shmss = pArea.moveShips();
    	sendNewCraneJobs(shmss);
    	pArea.moveCrane();
    }
    
    /**
     * Prevent ships from collisions
     */
    public void setShipDestinations() {
    	Ship[] ships = pArea.getShips();
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
    			Crane[] cs = pArea.getCranes();
    			if(cs != null && sms[i] != null && sms[i].isRemoveTFE()) {
        			pArea.addNewLogText(getShipLogMessage(sms[i]));
        			CraneJob cj = new CraneJob(sms[i].getShipIndex(),
    											pArea.getShips()[sms[i].getShipIndex()].getYsize(),
    											sms[i].getRmTFEX(),
    											sms[i].getRmTFEX(),
    											0,0); 
    				cs[0].addJob(cj);
    				pArea.addNewLogText(getCraneLogMessage(cj));
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
     * Keep track of the position of the current ship
     * @param s
     */
    public void addWaterSpaceShip(Ship s) {
    	WaterSpace w = new WaterSpace(s, waterSpace, s.getX() + s.getWidth());
    	waterSpace.setLeft(w);
    }
    
    /**
     * add free space after the last ship
     * @param s the last ship entered
     */
    public void addWaterSpaceFree(Ship s) {
    	WaterSpace w = new WaterSpace(null, waterSpace, s.getX());
    	waterSpace.setLeft(w);
    }
    
    /**
     * Tries to add ship, if this is possible
     * @return whether a ship is added
     */
    public boolean addShip() {
    	if(lossLocation[0] != -1) {
    		return false;
    	}
    	ShipMessage s = pArea.addShip();
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
   			pArea.addNewLogText(getShipLogMessage(s));
   			addWaterSpaceShip(pArea.getShips()[s.getShipIndex()]);
   		}
    	return true;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mousePressed(MouseEvent e) {
    	addShip();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }
      

}