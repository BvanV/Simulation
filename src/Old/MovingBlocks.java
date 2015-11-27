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

public class MovingBlocks extends JPanel implements MouseListener {

	/**
	 * 
	 */
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

    public MovingBlocks(int width, int height) {

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
    	if(Math.abs(a.x - b.x) < 50 && Math.abs(a.y - b.y) < 20) {
    		return true;
    	}
    	return false;
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
                JFrame f = new JFrame("Moving blocks");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setContentPane(new MovingBlocksv1_00(1800, 1000));
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
        // TODO Auto-generated method stub
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
        int x = 0;
        int y = random(980);
        int speedX = random(30);
        int speedY = 0;
        int radius = random(20);
        int red = random(255);
        int green = random(255);
        int blue = random(255);
        int i = 0;

        public Block() { 
        }

        public void draw(Graphics g) {

            g.setColor(new Color(red, green, blue));
            g.fillRect(x, y, 50, 20);
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