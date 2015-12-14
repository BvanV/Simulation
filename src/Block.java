import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Block extends JPanel {
	private static final long serialVersionUID = -6358405159987809616L;
	protected int x;
    protected int y;
    protected int color;
    protected int width;
    protected int heigth;
    protected int MaxSpeedX;
    protected int speedX; //0,1,2,3 or 4 (0, 25, 50, 75 or 100% of max speed)
    protected int speedY;
    protected int red;
    protected int green;
    protected int blue;
    protected int index;
    protected int inactiveTime; // wait some time before removing.
//    
//    //a block can be a ship, it then contains other blocks
//    protected int size = 0;
//    newTFE[] blokjes;
    
    public Block() {}
    
    public Block(int x, int y, int w, int h, int r, int g, int b) {
    	this.x 	= x;
    	this.y 	= y;
    	width 	= w;
    	heigth 	= h;
    	red 	= r;
    	green 	= g;
    	blue 	= b;
    }
 
    public void draw(Graphics g) {
        g.setColor(new Color(red, green, blue));
        g.fillRect(x, y, width, heigth);
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

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
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

	public int getMaxSpeedX() {
		return MaxSpeedX;
	}

	public void setMaxSpeedX(int maxSpeedX) {
		MaxSpeedX = maxSpeedX;
	}

	public int getSpeedX() {
		return speedX;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public int getSpeedY() {
		return speedY;
	}

	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}

	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getInactiveTime() {
		return inactiveTime;
	}

	public void setInactiveTime(int inactiveTime) {
		this.inactiveTime = inactiveTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
}