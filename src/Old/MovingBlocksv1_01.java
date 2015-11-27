package Old;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 
 * @author Bart van Vuuren
 * 
 * Newest functionalities
 * 
 * v1.00 added lanes for the blocks to move in
 * v1.01 grouped blocks into a large block
 */
public class MovingBlocksv1_01 extends JPanel implements MouseListener {

	/**
	 * 
	 */
	private static String TITLE = "Moving blocks v1.01";
	private static final long serialVersionUID = 1L;
    protected ArrayList<Block> blocks = new ArrayList<Block>(20);
    private Container container;
    private DrawCanvas canvas;
    private int canvasWidth;
    private int canvasHeight;
    public static final int UPDATE_RATE = 30;
    int x = random(480);
    int y = random(480);
    int speedX = random(30);
    int speedY = random(30);
    int radius = random(20);
    int red = random(255);
    int green = random(255);
    int blue = random(255);
    int count = 0;

    public static int random(int maxRange) {
        return (int) Math.round((Math.random() * maxRange));
    }

    public MovingBlocksv1_01(int width, int height) {

        canvasWidth = width;
        canvasHeight = height;

        container = new Container();

        canvas = new DrawCanvas();
        this.setLayout(new BorderLayout());
        this.add(canvas, BorderLayout.CENTER);
        this.addMouseListener(this);

        start();

    }

    public void start() {

        Thread t = new Thread() {
            public void run() {

                while (true) {

                    update();
                    repaint();
                    try {
                        Thread.sleep(1000 / UPDATE_RATE);
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
        t.start();
    }

    public void update() {
    	boolean[] removeList = new boolean[blocks.size()];
        for (Block block : blocks) {
            block.move(container);
            removeList[blocks.indexOf(block)] = false; 
            if(collides(block)) {
            	System.out.println("Collision detected");
            	java.awt.Toolkit.getDefaultToolkit().beep();
            	removeList[blocks.indexOf(block)] = true;
            }
        }
        for(int i=0;i<removeList.length;i++) {
        	if(removeList[i]) {
        		blocks.remove(i);
        	}
        }
    }

    /**
     * checks if Block a collides with a block with a higher index
     * @param a
     * @return true if Block a collides
     */
    public boolean collides(Block a) {
    	for(int i=blocks.indexOf(a) + 1;i<blocks.size();i++) {
    		if(collision(a, blocks.get(i))) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * assumse that a and b are different block.
     * @param a
     * @param b
     * @return true if there is a collision.
     */
    public boolean collision(Block a, Block b) {
    	if(a.y + a.heigth < b.y) {
    		return false;
    	} else if (b.y + b.heigth < a.y) {
    		return false;
    	} else if (a.x + a.width < b.y) {
    		return false;
    	} else if (b.x + b.width < a.x) {
    		return false;
    	} else {
    		return true;
    	}
    }
    
    class DrawCanvas extends JPanel {

        public void paintComponent(Graphics g) {

            super.paintComponent(g);
            container.draw(g);
            for (Block block : blocks) {
                block.draw(g);
            }
        }

        public Dimension getPreferredSize() {

            return (new Dimension(canvasWidth, canvasHeight));
        }
    }

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame f = new JFrame(TITLE);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setContentPane(new MovingBlocksv1_01(1800, 1000));
                f.pack();
                f.setVisible(true);
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        count++;
        blocks.add(new Block(4));
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mousePressed(MouseEvent e) {

        count++;
        blocks.add(new Block());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    public static class Block {

        public int random(int maxRange) {
            return (int) Math.round(Math.random() * maxRange);
        }
        int x;
        int y;
        int speedX;
        int speedY;
        int radius;
        int red;
        int green;
        int blue;
        int i;
        int width;
        int heigth;

        public Block() { 
        	x = 0;
            y = 25*random(39);
            speedX = 1 + random(29);
            speedY = 0;
            radius = random(20);
            red = random(255);
            green = random(255);
            blue = random(255);
            i = 0;
            width = 50;
            heigth = 20;
        }
        
        public Block(int size) {
        	if(size == 4) {
            	x = 0;
                y = 25*random(38);
                speedX = 1+random(19);
                speedY = 0;
                radius = random(20);
                red = random(255);
                green = random(255);
                blue = random(255);
                i = 0;        	
                width = 110;
                heigth = 45;
        	}
        }

        public void draw(Graphics g) {

            g.setColor(new Color(red, green, blue));
            g.fillRect(x, y, width, heigth);
        }

        public void move(Container container) {

            x += speedX;
            y += speedY;

            if (x - radius < 0) {

                speedX = -speedX;
                x = radius;
            } else if (x + radius > 1800) {

                speedX = -speedX;
                x = 1800 - radius;
            }

            if (y - radius < 0) {

                speedY = -speedY;
                y = radius;
            } else if (y + radius > 1000) {

                speedY = -speedY;
                y = 1000 - radius;
            }
        }
    }

    public static class Container {

        private static final int HEIGHT = 1000;
        private static final int WIDTH = 1800;
        private static final Color COLOR = Color.WHITE;

        public void draw(Graphics g) {

            g.setColor(COLOR);
            g.fillRect(0, 0, WIDTH, HEIGHT);
        }
    }
}