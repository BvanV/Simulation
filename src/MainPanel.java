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

	private static int SCREEN_WIDTH = 1200;
	private static int SCREEN_HEIGHT = 700;
	private static int WATER_HEIGHT = 300;
	private static int MAX_NUMBER_OF_BLOCKS = 20;
	static int CONTAINER_LENGTH = 20;
	static int CONTAINER_WIDTH = (CONTAINER_LENGTH * 2438)/6096;
	private static int CONTAINER_FIELD_WIDTH = 4 * CONTAINER_LENGTH + 4 * CONTAINER_WIDTH;
	static int CONTAINER_FIELD_OFFSET = SCREEN_WIDTH - CONTAINER_FIELD_WIDTH - (10 * CONTAINER_LENGTH);
	private static int FIELD_WIDTH = CONTAINER_LENGTH * 20;
	private static final long serialVersionUID = 1L;
    private CustomContainer container;
    private PaintArea pArea;
    public static final int UPDATE_RATE = 30;

    public static int random(int maxRange) {
        return (int) Math.round((Math.random() * maxRange));
    }

    public MainPanel(int width, int height) {

        SCREEN_HEIGHT = height;
        SCREEN_WIDTH = width;
        
        container = new CustomContainer(SCREEN_HEIGHT, SCREEN_WIDTH);

        pArea = new PaintArea(width, height, container);
        this.setLayout(new BorderLayout());
        this.add(pArea, BorderLayout.CENTER);
        this.addMouseListener(this);
        for(int i=0;i<(SCREEN_WIDTH/(CONTAINER_LENGTH * 20) + 1);i++) {
        	for(int j=0; j<2;j++) {
//        		field[i][0][j] = -1; 
        	}
        }
      
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
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
        t.start();
    }

    public void update() {
//    	for(int i=0;i<MAX_NUMBER_OF_BLOCKS;i++) {
//    		if(blocks[i] != null) {
//    			if(blocks[i].x < SCREEN_WIDTH) {
//    				blocks[i].move(container);
//    			} else if (blocks[i].inactiveTime < 10) {
//    				blocks[i].inactiveTime++;
//    				blocks[i].releaseAll();
//    			} else {
//    				blocks[i] = null;
//    			}
//    		}
//    	}
    	pArea.moveCrane();
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
    	System.out.println("Mouse pressed!");
   		pArea.addShip();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }
      

}