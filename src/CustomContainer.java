import java.awt.Color;
import java.awt.Graphics;

public class CustomContainer {

    private static int HEIGHT;
    private static int WIDTH;
    private static final Color COLOR = Color.WHITE;

    public CustomContainer(int h, int w) {
    	HEIGHT = h;
    	WIDTH = w;    	
    }
    
    public void draw(Graphics g) {
        g.setColor(COLOR);
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }
}