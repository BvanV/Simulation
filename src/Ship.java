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
	private boolean lock;
	
	private final int CONTAINER_WIDTH 	= Controller.CONTAINER_WIDTH;
	private final int CONTAINER_LENGTH	= Controller.CONTAINER_LENGTH;
	
	final int SH_WAITING 	= 0;
	final int SH_MOVING 	= 1;
	
	public Ship(int pYsize, int pXsize, int len, int wid, int ypos) {
		super();
    	ysize	= pYsize;
    	xsize	= pXsize;
		size	= xsize * ysize;
        speedX 	= 10;
        speedY 	= 0;
        red 	= 160;
        green 	= 160;
        blue 	= 160;
        width 	= len;
        heigth 	= wid;
    	x 		= 0 - width;
    	y 		= ypos;
    	destX 	= x;
    	status	= SH_MOVING;
    	lock 	= false;
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
	
	/**
	 * remove TFE with index (x, y)
	 * @param x
	 * @param y
	 * @return the TFE
	 */
	public TFE removeTFE(int x, int y) {
		TFE t = blokjes[xsize * y + x];
		blokjes[xsize * y + x] = null;
		return t;
	}
	
    public ShipMessage move() {
    	if(!lock) {
    		if(x + speedX > destX) {    			
    			moveContainers(destX - x, 0);
    			x = destX;
    			ShipMessage sm = new ShipMessage(index, false, true, x, 0, 0);
    			return sm;
    		} else {
    			moveContainers(speedX, 0);
    			x += speedX;
    		}
    	}
    	return new ShipMessage(index, false, false, x, -1, -1);
    }
    	
	
    /**
     * Move the containers on the ship
     * @param Xdist vertical distance, positive means to the right
     * @param Ydist vertical distance, positive means going down.
     */
    private void moveContainers(int Xdist, int Ydist) {
    	for(int i=0;i<size;i++) {
    		if(blokjes[i] != null) {
    			blokjes[i].move(Xdist, 0);
    		}
    	}
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

	public int getCONTAINER_LENGTH() {
		return CONTAINER_LENGTH;
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

	public boolean isLock() {
		return lock;
	}

	public void setLock(boolean lock) {
		this.lock = lock;
	}
}
  