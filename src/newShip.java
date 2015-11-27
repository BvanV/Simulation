import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;



public class newShip extends newBlock {
	int xsize;
	int ysize;
	int destX;
	boolean done;
	int tempCounter;
	newTFE[] blokjes;
	int status;
	int moveLimit;
	
	final int SH_WAITING 	= 0;
	final int SH_MOVING 	= 1;
	
	public newShip(int ind, int pYsize, int pXsize, int len, int wid, int ypos, int destinX) {
		super();
    	ysize	= 2 + random(3);
    	xsize	= 2*ysize + random(2);
		size	= xsize * ysize;
        speedX 	= 10;
        speedY 	= 0;
        red 	= 160;
        green 	= 160;
        blue 	= 160;
        index	= ind;
        done	= false;
        tempCounter = 0;
        width 	= len;
        heigth 	= wid;
    	x 		= 0 - width;
    	y 		= ypos;
    	destX	= destinX;
    	status	= SH_MOVING;
        MaxSpeedX = (width / 10) * (random(5) + 1);
        blokjes = new newTFE[xsize*ysize];
        for(int i=0;i<ysize;i++) {
        	for(int j=0;j<xsize;j++) {
        		blokjes[i*xsize + j] = new newTFE(i*xsize + j, MainPanel.CONTAINER_WIDTH, MainPanel.CONTAINER_LENGTH);
        		blokjes[i*xsize + j].setX(x + (MainPanel.CONTAINER_LENGTH / 2) + j * MainPanel.CONTAINER_LENGTH);
        		blokjes[i*xsize + j].setY(y + (MainPanel.CONTAINER_WIDTH / 2) + i * MainPanel.CONTAINER_WIDTH);
        	}
        }
    }

	public int random(int maxRange) {
        return (int) Math.round(Math.random() * maxRange);
    }
    
    public void move(MainPanel.Container container) {
    	if(status == SH_MOVING) {
    		if(x + speedX > destX) {
    			moveContainers(destX - x, 0, container);
    			x = destX;
    		} else {
    			x += speedX;
    		}
    	}
    	
    	
//		if(!done) {
//			reserveSpace();
//			int next = getNextField();
//			if(x >= destX) {
//				if(xsize > 4 && x < MovingBlocksv2_03.CONTAINER_FIELD_OFFSET){
//					destX += MovingBlocksv2_03.CONTAINER_LENGTH * 4;
//					done = true;
//					cranes[0].done = false;
//					cranes[0].ShipToHandle = index;
//				} else {
//					destX = SCREEN_WIDTH;
//					done = true;
//					cranes[0].done = false;
//					cranes[0].ShipToHandle = index;
//				}
//			} else if(next >= field.length || field[next][0][0] == index) {
//				x += MaxSpeedX;
//				int correction = 0;
//				if(x > destX) {
//					correction = x - destX;
//					x = destX;
//				}
//	        	for(int i=0;i<size;i++) {
//	        		if(blokjes[i] != null) {
//	        			blokjes[i].setX(blokjes[i].getX() +(MaxSpeedX - correction));
//	        		}
//	        	}
//			}
//			releaseSpace();
//		}
	}
	
    /**
     * Move the containers on the ship
     * @param Xdist vertical distance, positive means to the right
     * @param Ydist vertical distance, positive means going down.
     */
    private void moveContainers(int Xdist, int Ydist, MainPanel.Container container) {
    	for(int i=0;i<size;i++) {
    		if(blokjes[i] != null) {
    			blokjes[i].move(Xdist, 0, container);
    		}
    	}
    }
    
	public void releaseAll() {
//		for(int i=0; i< field.length;i++) {
//			if(field[i][0][0] == index) {
//				field[i][0][0] = -1;
//			}
//		}
	}
				
    public void draw(Graphics g) {
        g.setColor(new Color(red, green, blue));
        g.fillRect(x, y, width, heigth);
    }
	
	/**
	 * 
	 * @return the index of the next field to enter. Returns -1 if there is no next field to enter
	 */
//	public int getNextField() {
//		int nextfield = (x + width) / FIELD_WIDTH;
//		nextfield++;
//		return nextfield; 
//	}
	
	/**
	 * 
	 * @return true if it succeeded to reserve the next field, false otherwise.
	 */
//	public void reserveSpace() {
//		int next = getNextField();
//		if(next != -1 && next < field.length) {
//			int goal = field[next][0][0];
//			if(goal == -1 || goal == index) {
//				field[next][0][0] = index;
//			}
//			
//		}
//		
//	}
//
//	/**
//	 * 
//	 * @return the index of the last field passed
//	 */
//	public int getPreviousField() {
//		int prevfield = x / FIELD_WIDTH;
//		prevfield--;
//		if(prevfield < 0) {
//			return -1;
//		} else if(prevfield >= field.length) {
//			return -1;
//		}
//		return prevfield; 
//	}
//
//
//	public void releaseSpace() {
//		int prev = getPreviousField();
//		if(prev != -1 && field[prev][0][0] == index) {
//			field[prev][0][0] = -1;
//		}
//	}
}
  