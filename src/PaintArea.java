import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

class PaintArea extends JPanel {

	private static final long serialVersionUID = 7471887303263865455L;
	private CustomContainer container;
	private Block water;
	private Field containerField;
    private int canvasWidth;
    private int canvasHeight;	
    private Crane[] cranes;
    private Ship[] ships;
	private int MAX_NUMBER_OF_BLOCKS 	= MainPanel.MAX_NUMBER_OF_BLOCKS;
    private int SCREEN_WIDTH 			= MainPanel.SCREEN_WIDTH;
    private int CONTAINER_LENGTH 		= MainPanel.CONTAINER_LENGTH;
    private int CONTAINER_WIDTH 		= MainPanel.CONTAINER_WIDTH;
    private int WATER_HEIGHT 			= MainPanel.WATER_HEIGHT;
    private int CONTAINER_FIELD_OFFSET 	= MainPanel.CONTAINER_FIELD_OFFSET;
	
    public PaintArea() {}
    
    public PaintArea(int w, int h, CustomContainer c) {
    	canvasWidth 	= w;
    	canvasHeight 	= h;
    	container 		= c;
    	
    	//TODO remove magic numbers
    	water = new Block(0,0,w,WATER_HEIGHT,51,153,255);
    	containerField = new Field(CONTAINER_FIELD_OFFSET, WATER_HEIGHT,200, 500, 255, 204, 153, 4, 50);
    	cranes = new Crane[1];
    	cranes[0] = new Crane(0, CONTAINER_FIELD_OFFSET + 20, WATER_HEIGHT + 10 * CONTAINER_WIDTH);
    	ships = new Ship[MAX_NUMBER_OF_BLOCKS];
    }

    /**
     * Add a Ship to the panel
     */
    public void addShip() {
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

	public CustomContainer getContainer() {
		return container;
	}

	public void setContainer(CustomContainer container) {
		this.container = container;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
