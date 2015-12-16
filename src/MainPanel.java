import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;




import java.util.ArrayList;

import javax.swing.JLabel;
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
	final static int MIN_SHIP_DISTANCE  	= 2 * CONTAINER_LENGTH;
	final private int[] LOSS_LOC_OFFSET		= new int[]{200, 600, 1000, CONTAINER_FIELD_OFFSET};
	
	// taken from PaintArea
	private JLabel label;
	private String[] logText;
	private String LOG_TEXT_HEADER = "Control log"; 
	private int LOG_SIZE = 10;
	private Block water;
	private Field containerField;
    private int canvasWidth;
    private int canvasHeight;	
    private Crane[] cranes;
    private Ship[] ships;
	
    private CustomContainer container;
    private int[] lossLocation;
    private boolean mousePressed = false;

    public MainPanel() {
    	lossLocation = new int[4];
    	for(int i=0;i<4;i++) {
    		lossLocation[i] = -1;
    	}
        container = new CustomContainer(SCREEN_HEIGHT, SCREEN_WIDTH);
        this.setLayout(new BorderLayout());
        this.addMouseListener(this);
        
    	canvasWidth 	= SCREEN_WIDTH;
    	canvasHeight 	= SCREEN_HEIGHT;
    	logText 		= new String[10];
    	for(int i=0;i<LOG_SIZE;i++) {
    		logText[i] = "";
    	}
    	
    	//TODO remove magic numbers
    	this.setLayout(null);
    	water = new Block(0,0,SCREEN_WIDTH,WATER_HEIGHT,51,153,255);
    	containerField = new Field(CONTAINER_FIELD_OFFSET, WATER_HEIGHT,200, 500, 255, 204, 153, 4, 50);
    	cranes = new Crane[1];
    	cranes[0] = new Crane(0, CONTAINER_FIELD_OFFSET + 20, WATER_HEIGHT + 10 * CONTAINER_WIDTH);
    	ships = new Ship[MAX_NUMBER_OF_BLOCKS];
    	label = new JLabel("<html><h2>"+LOG_TEXT_HEADER+"</h2></html>");
    	label.setBounds(50, 400, 300, 300);  
    	this.add(label);
    }

    /**
     * Add a Ship to the panel
     */
    public ShipMessage addShip() {
		int i = 0;
		boolean done = false;
		while(i<MAX_NUMBER_OF_BLOCKS && !done) {
			if(ships[i] == null) {
				int ysize	= 2 + random(3);
		    	int xsize	= 2*ysize + random(2);
		    	int length	= (xsize + 1) * (CONTAINER_LENGTH);
		    	int width 	= (ysize + 1) * (CONTAINER_WIDTH);
		    	int ypos	= WATER_HEIGHT - width;
		    	int destinX	= CONTAINER_FIELD_OFFSET;
				ships[i] = new Ship(i, ysize, xsize, length, width, ypos, destinX);
				done = true;
			}
			i++;
		}
		if(done) {
			return new ShipMessage(i-1, true, false, 0, -1, -1);
		}
		return null;
    }
    
    /**
     * Add a new log text to the Control log.
     * @param log
     */
    public void addNewLogText(String log) {
    	// shift the log texts
    	for(int i=LOG_SIZE-1; i>0; i--) {
    		logText[i] = logText[i-1];
    	}
    	logText[0] = log;
    	String newText = "<html><h2>" + LOG_TEXT_HEADER + "</h2><br>";
    	for(int i=0;i<LOG_SIZE;i++) {
    		newText = newText + logText[i] + "<br>";
    	}
    	newText = newText + "</html>";
    	label.setText(newText);
    }
    
    /**
     * move the crane
     */
    public void moveCrane() {
    	cranes[0].move(container);
    }
    
    public ShipMessage[] moveShips() {
    	ArrayList<ShipMessage> shipMessages = new ArrayList<ShipMessage>();
	   	for(int i=0;i<MAX_NUMBER_OF_BLOCKS;i++) {
			if(ships[i] != null) {
				if(ships[i].x < SCREEN_WIDTH) {
					ShipMessage sm = ships[i].move(container);
					if(sm != null) {
						shipMessages.add(sm);
					}
				} else if (ships[i].getInactiveTime() < 10) {
					ships[i].setInactiveTime(ships[i].getInactiveTime() + 1);
					ships[i].releaseAll();
				} else {
					ships[i] = null;
				}
			}
		}
	   	return shipMessages.toArray(new ShipMessage[1]);
    }
    
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        container.draw(g);
        water.draw(g);
        containerField.draw(g);
        for(int i=0;i<MAX_NUMBER_OF_BLOCKS;i++) {
        	if(ships[i] != null) {
        		ships[i].draw(g);
        		if(ships[i].getShipSize() > 1) {
        			for(int j=0;j<ships[i].getShipSize();j++) {
        				if(ships[i].getTFE(j) != null) {
        					ships[i].getTFE(j).draw(g);
        				}
        			}
        		}
        	}
        }
        for(int i=0;i<containerField.getXsize();i++) {
//        	for(int j=0;j<container_field.ysize;j++) {
//        		if(container_field.blocks[i][j] != null) {
//            		container_field.blocks[i][j].draw(g);
//        		}
//        	}
        }            
        cranes[0].draw(g);
    }

    public Dimension getPreferredSize() {

        return (new Dimension(canvasWidth, canvasHeight));
    }
    
    public static int random(int maxRange) {
        return (int) Math.round((Math.random() * maxRange));
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
    	mousePressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }


	public boolean isMousePressed() {
		return mousePressed;
	}


	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}

	public JLabel getLabel() {
		return label;
	}

	public void setLabel(JLabel label) {
		this.label = label;
	}

	public String[] getLogText() {
		return logText;
	}

	public void setLogText(String[] logText) {
		this.logText = logText;
	}

	public String getLOG_TEXT_HEADER() {
		return LOG_TEXT_HEADER;
	}

	public void setLOG_TEXT_HEADER(String lOG_TEXT_HEADER) {
		LOG_TEXT_HEADER = lOG_TEXT_HEADER;
	}

	public int getLOG_SIZE() {
		return LOG_SIZE;
	}

	public void setLOG_SIZE(int lOG_SIZE) {
		LOG_SIZE = lOG_SIZE;
	}

	public Block getWater() {
		return water;
	}

	public void setWater(Block water) {
		this.water = water;
	}

	public Field getContainerField() {
		return containerField;
	}

	public void setContainerField(Field containerField) {
		this.containerField = containerField;
	}

	public int getCanvasWidth() {
		return canvasWidth;
	}

	public void setCanvasWidth(int canvasWidth) {
		this.canvasWidth = canvasWidth;
	}

	public int getCanvasHeight() {
		return canvasHeight;
	}

	public void setCanvasHeight(int canvasHeight) {
		this.canvasHeight = canvasHeight;
	}

	public Crane[] getCranes() {
		return cranes;
	}

	public void setCranes(Crane[] cranes) {
		this.cranes = cranes;
	}

	public Ship[] getShips() {
		return ships;
	}

	public void setShips(Ship[] ships) {
		this.ships = ships;
	}

	public CustomContainer getContainer() {
		return container;
	}

	public void setContainer(CustomContainer container) {
		this.container = container;
	}

	public int[] getLossLocation() {
		return lossLocation;
	}

	public void setLossLocation(int[] lossLocation) {
		this.lossLocation = lossLocation;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static int getScreenWidth() {
		return SCREEN_WIDTH;
	}

	public static int getScreenHeight() {
		return SCREEN_HEIGHT;
	}

	public static int getContainerWidth() {
		return CONTAINER_WIDTH;
	}

	public static int getContainerLength() {
		return CONTAINER_LENGTH;
	}

	public static int getWaterHeight() {
		return WATER_HEIGHT;
	}

	public static int getContainerFieldWidth() {
		return CONTAINER_FIELD_WIDTH;
	}

	public static int getContainerFieldOffset() {
		return CONTAINER_FIELD_OFFSET;
	}

	public static int getUpdateRate() {
		return UPDATE_RATE;
	}

	public static int getMaxNumberOfBlocks() {
		return MAX_NUMBER_OF_BLOCKS;
	}

	public static int getMinShipDistance() {
		return MIN_SHIP_DISTANCE;
	}

	public int[] getLOSS_LOC_OFFSET() {
		return LOSS_LOC_OFFSET;
	}
      

}