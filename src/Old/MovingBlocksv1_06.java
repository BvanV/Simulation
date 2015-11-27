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
 * v1.01 added large blocks
 * v1.02 fixed return rules
 * v1.03 Avoid collisions
 * v1.04 Added block destination
 * v1.05 Simplified: blocks only move to the right, field are larger, the maximum amount of blocks is fixed
 * v1.06 using less magic numbers, created subclass Ship
 */
public class MovingBlocksv1_06 extends JPanel implements MouseListener {

	/**
	 * 
	 */
	private static String TITLE = "Moving blocks v1.06";
	private static int SCREEN_WIDTH = 1800;
	private static int SCREEN_HEIGHT = 1000;
	private static int WATER_HEIGHT = 300;
	private static int NUMBER_OF_WATER_LANES = WATER_HEIGHT / 25;
	private static int MAX_NUMBER_OF_BLOCKS = 20;
	private static int CONTAINER_FIELD_WIDTH = 120;
	private static int CONTAINER_FIELD_HEIGTH = 500;
	private static int CONTAINER_LENGTH = 20;
	private static int CONTAINER_WIDTH = (CONTAINER_LENGTH * 2438)/6096;
	private static int[][] field = new int[SCREEN_WIDTH/100+1][NUMBER_OF_WATER_LANES];		// reserve fields
	private static final long serialVersionUID = 1L;
    protected static Block[] blocks = new Block[MAX_NUMBER_OF_BLOCKS];
    private static Block water;
    private static Block container_field;
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
    int count = 0;

    public static int random(int maxRange) {
        return (int) Math.round((Math.random() * maxRange));
    }

    public MovingBlocksv1_06(int width, int height) {

        canvasWidth = width;
        canvasHeight = height;

        container = new Container();

        canvas = new DrawCanvas();
        this.setLayout(new BorderLayout());
        this.add(canvas, BorderLayout.CENTER);
        this.addMouseListener(this);
        for(int i=0;i<(SCREEN_WIDTH/100);i++) {
        	for(int j=0; j<NUMBER_OF_WATER_LANES;j++) {
        		field[i][j] = -1; 
        	}
        }
        water = new Block(0,0,1800,300,51,153,255);
        container_field = new Block(200, WATER_HEIGHT,CONTAINER_FIELD_WIDTH, CONTAINER_FIELD_HEIGTH, 255, 204, 153);
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
    	for(int i=0;i<MAX_NUMBER_OF_BLOCKS;i++) {
    		if(blocks[i] != null) {
    			if(blocks[i].x < SCREEN_WIDTH) {
    				blocks[i].move(container);
    			} else if (blocks[i].inactiveTime < 10) {
    				blocks[i].inactiveTime++;
    			} else {
    				blocks[i] = null;
    			}
    		}
    	}    
    }
      
    class DrawCanvas extends JPanel {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void paintComponent(Graphics g) {

            super.paintComponent(g);
            container.draw(g);
            water.draw(g);
            container_field.draw(g);
            for(int i=0;i<MAX_NUMBER_OF_BLOCKS;i++) {
            	if(blocks[i] != null) {
            		blocks[i].draw(g);
            		if(blocks[i].size > 1) {
            			for(int j=0;j<blocks[i].size;j++) {
            				blocks[i].blokjes[j].draw(g);
            			}
            		}
            	}
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
                f.setContentPane(new MovingBlocksv1_06(SCREEN_WIDTH, SCREEN_HEIGHT));
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
//        blocks.add(new Block(4));
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mousePressed(MouseEvent e) {
    	if(addZoneIsFree()) {
    		int i = 0;
    		boolean done = false;
    		while(i<MAX_NUMBER_OF_BLOCKS && !done) {
    			if(blocks[i] == null) {
    				blocks[i] = new Ship(i);
    				done = true;
    			}
    			i++;
    		}
    	}
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    public boolean addZoneIsFree() {
    	for(int i=0;i<MAX_NUMBER_OF_BLOCKS;i++) {
    		if(blocks[i] != null && blocks[i].x < 120) {
    			return false;
    		}
    	}
    	return true;
    }
    
    public static class Block {

        public int random(int maxRange) {
            return (int) Math.round(Math.random() * maxRange);
        }
        int x;
        int y;
        int MaxSpeedX;
        int speedX; //0,1,2,3 or 4 (0, 25, 50, 75 or 100% of max speed)
        int speedY;
        int radius;
        int red;
        int green;
        int blue;
        int i;
        int width;
        int heigth;
        int index;
        int inactiveTime; // wait some time before removing.
        
        //a block can be a ship, it then contains other blocks
        int size = 0;
        Block[] blokjes;
       
        // used by subclasses
        public Block() {}
        
        // used for the ffe's
        public Block(int index) { 
        	x = 0;
            y = 25*random(NUMBER_OF_WATER_LANES - 1);
            MaxSpeedX = 4 + random(5);
            speedX = 0;
            speedY = 0;
            radius = random(20);
            red = 255*random(1);
            green = 255*random(1);
            blue = 255*random(1);
            i = 0;
            width = CONTAINER_LENGTH;
            heigth = CONTAINER_WIDTH; 
            inactiveTime = 0;
        }
        
        public Block(int index, int xsize, int ysize) {
        	ysize	= 2 + random(3);
        	xsize	= 2*ysize + random(2);
    		size	= xsize * ysize;
            speedX 	= 0;
            speedY 	= 0;
            red 	= 160;
            green 	= 160;
            blue 	= 160;
            i 		= 0;        	
            width 	= 2 * (CONTAINER_LENGTH / 10) + (xsize * CONTAINER_LENGTH);
            heigth 	= 2 * (CONTAINER_LENGTH / 10) + (ysize * CONTAINER_WIDTH);
        	x 		= 0 - width;
        	y 		= random(WATER_HEIGHT) - heigth;
            MaxSpeedX = 4 + random(5);
            blokjes = new Block[xsize*ysize];
            for(int i=0;i<ysize;i++) {
            	for(int j=0;j<xsize;j++) {
            		blokjes[i*xsize + j] = new Block(i*xsize + j);
            		blokjes[i*xsize + j].x = x + (CONTAINER_LENGTH / 10) + j * CONTAINER_LENGTH;
            		blokjes[i*xsize + j].y = y + (CONTAINER_LENGTH / 10) + i * CONTAINER_WIDTH;
            	}
            }
        }

        //Special Blocks, like the water
        public Block(int px, int py, int pwidth, int pheigth, int pred, int pgreen, int pblue) {
        	x 		= px;
            y 		= py;
            width 	= pwidth;
            heigth 	= pheigth;      	
            red 	= pred;
            green 	= pgreen;
            blue 	= pblue;
        }
        
        public void draw(Graphics g) {
            g.setColor(new Color(red, green, blue));
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
        	//moving to the right
        	if(MaxSpeedX > 0) {
        		int rbound = Math.min(SCREEN_WIDTH/100 - 2,x/100);
        		for(int i=0;i<rbound-1;i++) {
        			if(field[i][y/25] == this.index) {
        				field[i][y/25] = -1;
        			}
        		}
        	} else { //moving to the left
        		int lbound = Math.max(3, x/100);
        		for(int i=lbound+1;i<SCREEN_WIDTH/100;i++) {
        			if(field[i][y/25] == this.index) {
        				field[i][y/25] = -1;
        			}
        		}
        	}

        }
        
        public void move(Container container) {
        	releaseReservedSpace();
        	speedX = reserveSpace(this.index);
            x += MaxSpeedX;
            if(size > 1) {
            	for(int i=0;i<size;i++) {
            		blokjes[i].x += MaxSpeedX;
            	}
            }
            y += speedY;
/*
            if (y < 0) {
                speedY = -speedY;
                y = 0;
            } else if (y + heigth > SCREEN_HEIGHT) {
                speedY = -speedY;
                y = SCREEN_HEIGHT - heigth;
            }
*/
        }
    }
    
    public static class Ship extends Block {

		public Ship(int ind) {
			super();
        	int ysize	= 2 + random(3);
        	int xsize	= 2*ysize + random(2);
    		size	= xsize * ysize;
            speedX 	= 0;
            speedY 	= 0;
            red 	= 160;
            green 	= 160;
            blue 	= 160;
            index	= ind;        	
            width 	= 2 * (CONTAINER_LENGTH / 10) + (xsize * CONTAINER_LENGTH);
            heigth 	= 2 * (CONTAINER_LENGTH / 10) + (ysize * CONTAINER_WIDTH);
        	x 		= 0 - width;
        	y 		= random(WATER_HEIGHT) - heigth;
            MaxSpeedX = 4 + random(5);
            blokjes = new Block[xsize*ysize];
            for(int i=0;i<ysize;i++) {
            	for(int j=0;j<xsize;j++) {
            		blokjes[i*xsize + j] = new Block(i*xsize + j);
            		blokjes[i*xsize + j].x = x + (CONTAINER_LENGTH / 10) + j * CONTAINER_LENGTH;
            		blokjes[i*xsize + j].y = y + (CONTAINER_LENGTH / 10) + i * CONTAINER_WIDTH;
            	}
            }
        }
    }
    
    public static class Container {

        private static final int HEIGHT = SCREEN_HEIGHT;
        private static final int WIDTH = SCREEN_WIDTH;
        private static final Color COLOR = Color.WHITE;

        public void draw(Graphics g) {

            g.setColor(COLOR);
            g.fillRect(0, 0, WIDTH, HEIGHT);
        }
    }
}