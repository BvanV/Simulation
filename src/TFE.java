import java.awt.Color;
import java.awt.Graphics;

public class TFE extends Block {

	private static final long serialVersionUID = -4011709659923809368L;

	public TFE() {}
    
    /**
     * used for containers on a ship
     * @param index
     * @param givenHeigth height
     * @param givenWidth width
     */
    public TFE(int index, int givenHeight, int givenWidth) {
        color	= random(7);
        width 	= givenWidth;
        heigth 	= givenHeight;
    }

    public int random(int maxRange) {
        return (int) Math.round(Math.random() * maxRange);
    }
    
    
    public void draw(Graphics g) {
        g.setColor(new Color(255 * (color / 4), 
        				255 * ((color % 4) / 2), 
        				255 * (color % 2)));
        g.fillRect(x, y, width, heigth);
    }

    /**
     * Tries to reserve as much space as necessary for the next 4 timestamps
     * @param block. The index of the current block
     * @return the maximum allowable speedindex(0-4)
     */
    public static int reserveSpace(int index) {
    	// 1 timestamp ahead
    	// find the left bound of the ship for t=t+1
    	return 1;
    	
    }
    
    public void releaseReservedSpace() {
    }
    
    public void move(int Xdist, int Ydist, CustomContainer container) { 
    	x += Xdist;
    	y += Ydist;
    }

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeigth() {
		return heigth;
	}

	public void setHeigth(int heigth) {
		this.heigth = heigth;
	}

}