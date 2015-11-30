import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class newBlock extends JPanel {
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
    
    public newBlock() {}
    
    public newBlock(int x, int y, int w, int h, int r, int g, int b) {
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
    
}