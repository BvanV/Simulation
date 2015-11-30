import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;


class PaintArea extends JPanel {

	private static final long serialVersionUID = 7471887303263865455L;
	private CustomContainer container;
	private newBlock water;
	private newField containerField;
	private int MAX_NUMBER_OF_BLOCKS = 10;
    private int canvasWidth;
    private int canvasHeight;	
    private newCrane[] cranes;
    private newShip[] ships;
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
    	water = new newBlock(0,0,w,WATER_HEIGHT,51,153,255);
    	containerField = new newField(CONTAINER_FIELD_OFFSET, WATER_HEIGHT,200, 500, 255, 204, 153, 4, 50);
    	cranes = new newCrane[1];
    	cranes[0] = new newCrane(0, CONTAINER_FIELD_OFFSET + 20, WATER_HEIGHT + 10 * CONTAINER_WIDTH);
    	ships = new newShip[MAX_NUMBER_OF_BLOCKS];
    }
	
    /**
	 * 
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
				ships[i] = new newShip(i, ysize, xsize, length, width, ypos, destinX);
				done = true;
			}
			i++;
		}

    }
    
    public void moveCrane() {
    	cranes[0].move(container);
    }
    
    public void moveShips() {
	   	for(int i=0;i<MAX_NUMBER_OF_BLOCKS;i++) {
			if(ships[i] != null) {
				if(ships[i].x < SCREEN_WIDTH) {
					ships[i].move(container);
				} else if (ships[i].getInactiveTime() < 10) {
					ships[i].setInactiveTime(ships[i].getInactiveTime() + 1);
					ships[i].releaseAll();
				} else {
					ships[i] = null;
				}
			}
		}	
    }
    
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        container.draw(g);
        water.draw(g);
        containerField.draw(g);
        cranes[0].draw(g);
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

    }

    public Dimension getPreferredSize() {

        return (new Dimension(canvasWidth, canvasHeight));
    }
    
    public static int random(int maxRange) {
        return (int) Math.round((Math.random() * maxRange));
    }

}
