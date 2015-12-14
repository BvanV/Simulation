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
	
    private CustomContainer container;
    private PaintArea pArea;
    

    public static int random(int maxRange) {
        return (int) Math.round((Math.random() * maxRange));
    }

    public MainPanel() {
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
    	ShipMessage[] shmss = pArea.moveShips();
    	sendNewCraneJobs(shmss);
    	pArea.moveCrane();
    }
    
    public void sendNewCraneJobs(ShipMessage[] sms) {
    	if(sms != null) {
    		for(int i=0; i<sms.length;i++) {
    			Crane[] cs = pArea.getCranes();
    			if(cs != null && sms[i] != null) {
    				cs[0].addJob(new CraneJob(sms[i].getShipIndex(),
    						pArea.getShips()[sms[i].getShipIndex()].getYsize(),0,0,0,0));
    			}
    		}
    	}
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
   		pArea.addShip();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }
      

}