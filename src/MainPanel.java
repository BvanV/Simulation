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

	final static int SCREEN_WIDTH 			= Controller.SCREEN_WIDTH;
	final static int SCREEN_HEIGHT 			= Controller.SCREEN_HEIGHT;		
	final static int WATER_HEIGHT			= Controller.WATER_HEIGHT;
	final static String LOG_TEXT_HEADER 	= Controller.LOG_TEXT_HEADER;
	final static int LOG_SIZE 				= Controller.LOG_SIZE;
	final static int MAX_NUMBER_OF_SHIPS	= Controller.MAX_NUMBER_OF_SHIPS;
	
	private Ship[] ships;
	private Crane[] cranes;
	private Field containerField;
	private JLabel label;
	private Block water;

	private String[] logText;

    private int[] lossLocation;
    private boolean mousePressed = false;

    public MainPanel(Ship[] s, Crane[] c, Field f) {
    	ships = s;
    	cranes = c;
    	containerField = f;
    	lossLocation = new int[4];
    	for(int i=0;i<4;i++) {
    		lossLocation[i] = -1;
    	}
        this.addMouseListener(this);
      	
    	//TODO remove magic numbers
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
    	this.setLayout(null);
    	
    	water = new Block(0,0,SCREEN_WIDTH,WATER_HEIGHT,0,0,255);
    	label = new JLabel("<html><h2>"+LOG_TEXT_HEADER+"</h2></html>");
    	label.setBounds(50, 400, 300, 300);  
    	this.add(label);
    	
    	logText = new String[10];
    	for(int i=0;i<LOG_SIZE;i++) {
    		logText[i] = "";
    	}
    }
  
    /**
     * Add a new log text to the Control log.
     * @param log
     */
    public synchronized void addNewLogText(String log) {
    	System.out.println("Tried to add new LogText!");
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
    	System.out.println("New text" + newText);
    }
    
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		water.draw(g);
		containerField.draw(g);
		for(int i=0;i<MAX_NUMBER_OF_SHIPS;i++) {
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
			for(int j=0;j<containerField.getYsize();j++) {
				if(containerField.getTFE(i,j) != null) {
					containerField.getTFE(i,j).draw(g);
				}
			}
		}            
		cranes[0].draw(g);
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

	public Block getWater() {
		return water;
	}

	public void setWater(Block water) {
		this.water = water;
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

	public static int getWaterHeight() {
		return WATER_HEIGHT;
	}
}