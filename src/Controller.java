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
    	mp.setShipDestinations();
    	ShipMessage[] shmss = mp.pArea.moveShips();
    	mp.sendNewCraneJobs(shmss);
    	mp.pArea.moveCrane();
    }
    
}
