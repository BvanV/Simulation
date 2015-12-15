import java.awt.Color;
import java.awt.Graphics;



public class Ship extends Block {
	private static final long serialVersionUID = 6494552242026331765L;
	private int xsize;
	private int ysize;
	private int destX;
	private TFE[] blokjes;
	private int status;
	private int size;
	private int CONTAINER_WIDTH 	= MainPanel.CONTAINER_WIDTH;
	private int CONTAINER_LENGTH	= MainPanel.CONTAINER_LENGTH;
	
	final int SH_WAITING 	= 0;
	final int SH_MOVING 	= 1;
	
	public Ship(int ind, int pYsize, int pXsize, int len, int wid, int ypos, int destinX) {
		super();
    	ysize	= pYsize;
    	xsize	= pXsize;
		size	= xsize * ysize;
        speedX 	= 10;
        speedY 	= 0;
        red 	= 160;
        green 	= 160;
        blue 	= 160;
        index	= ind;
        width 	= len;
        heigth 	= wid;
    	x 		= 0;
    	y 		= ypos;
    	destX	= destinX;
    	status	= SH_MOVING;
        MaxSpeedX = (width / 10) * (random(5) + 1);
        blokjes = new TFE[xsize*ysize];
        for(int i=0;i<ysize;i++) {
        	for(int j=0;j<xsize;j++) {
        		blokjes[i*xsize + j] = new TFE(i*xsize + j, CONTAINER_WIDTH, CONTAINER_LENGTH);
        		blokjes[i*xsize + j].setX(x + (CONTAINER_LENGTH / 2) + j * CONTAINER_LENGTH);
        		blokjes[i*xsize + j].setY(y + (CONTAINER_WIDTH / 2) + i * CONTAINER_WIDTH);
        	}
        }
    }

	public int random(int maxRange) {
        return (int) Math.round(Math.random() * maxRange);
    }
    
	public int getShipSize() {
		return this.size;
	}
	
	public TFE getTFE(int index) {
		if(index < blokjes.length) {
			return blokjes[index];
		}
		return null;
	}
	
    public ShipMessage move(CustomContainer container) {
    	if(status == SH_MOVING) {
    		if(x + speedX > destX) {
    			moveContainers(destX - x, 0, container);
    			x = destX;
    			status = SH_WAITING;
    			ShipMessage sm = new ShipMessage(index, false, true, x, 1, 1);
    			return sm;
    		} else {
    			moveContainers(speedX, 0, container);
    			x += speedX;
    		}
    	}
    	return new ShipMessage(index, false, false, x, -1, -1);
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
	
    /**
     * Move the containers on the ship
     * @param Xdist vertical distance, positive means to the right
     * @param Ydist vertical distance, positive means going down.
     */
    private void moveContainers(int Xdist, int Ydist, CustomContainer container) {
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

	public int getXsize() {
		return xsize;
	}

	public void setXsize(int xsize) {
		this.xsize = xsize;
	}

	public int getYsize() {
		return ysize;
	}

	public void setYsize(int ysize) {
		this.ysize = ysize;
	}

	public int getDestX() {
		return destX;
	}

	public void setDestX(int destX) {
		this.destX = destX;
	}

	public TFE[] getBlokjes() {
		return blokjes;
	}

	public void setBlokjes(TFE[] blokjes) {
		this.blokjes = blokjes;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getCONTAINER_WIDTH() {
		return CONTAINER_WIDTH;
	}

	public void setCONTAINER_WIDTH(int cONTAINER_WIDTH) {
		CONTAINER_WIDTH = cONTAINER_WIDTH;
	}

	public int getCONTAINER_LENGTH() {
		return CONTAINER_LENGTH;
	}

	public void setCONTAINER_LENGTH(int cONTAINER_LENGTH) {
		CONTAINER_LENGTH = cONTAINER_LENGTH;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getSH_WAITING() {
		return SH_WAITING;
	}

	public int getSH_MOVING() {
		return SH_MOVING;
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
  