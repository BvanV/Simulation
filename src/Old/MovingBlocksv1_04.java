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
 * v1.04 Blocks move to a destination
 * 
 */
public class MovingBlocksv1_04 extends JPanel implements MouseListener {

	/**
	 * 
	 */
	private static String TITLE = "Moving blocks v1.04";
	private static int SCREEN_WIDTH = 1800;
	private static int SCREEN_HEIGHT = 400;
	private static int NUMBER_OF_WATER_LANES = 10;
	private static int[][] field = new int[SCREEN_WIDTH/100+1][NUMBER_OF_WATER_LANES];		// reserve fields
	private static final long serialVersionUID = 1L;
    protected static ArrayList<Block> blocks = new ArrayList<Block>(20);
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

    public MovingBlocksv1_04(int width, int height) {

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
            if(block.finished) {
            	removeList[blocks.indexOf(block)] = true;
            }
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
    	} else if (a.x + a.width < b.x) {
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
                f.setContentPane(new MovingBlocksv1_04(SCREEN_WIDTH, SCREEN_HEIGHT));
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
    		blocks.add(new Block());
    	}
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    public boolean addZoneIsFree() {
    	for(Block block : blocks) {
    		if(block.x < 120) {
    			return false;
    		}
    	}
    	return true;
    }
    
    public static class Block {

        public int random(int maxRange) {
            return (int) Math.round(Math.random() * maxRange);
        }
        int destX;
        int destY;
        int stay = 10;
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
        boolean finished = false;
        

        public Block() { 
        	x = 0;
            y = 25*random(9);
            destX = 250 + random(1200);
            destY = 225;
            stay = 30;
            MaxSpeedX = 4 + random(26);
            speedX = 0;
            speedY = 25;
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
                y = 25*random(8);
                MaxSpeedX = 4 + random(16);
                speedX = 0;
                speedY = 0;
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
    
        /**
         * Tries to reserve as much space as necessary for the next 4 timestamps
         * @param block. The index of the current block
         * @return the maximum allowable speedindex(0-4)
         */
        public static int reserveXSpace(Block b) {
        	// 1 timestamp ahead
        	// find the left bound of the ship for t=t+1
        	int index = blocks.indexOf(b);
        	int left = 0;
        	if(b.x + b.MaxSpeedX > SCREEN_WIDTH - b.width) {
        		left = SCREEN_WIDTH - b.width -1;
        	} else if(b.x + b.MaxSpeedX < 120) {
        		left = 120;
        	} else {
        		left = b.x + b.MaxSpeedX;
        	}
        	
        	for(int i=left/100;i<=(left+b.width)/100;i++) {
        		if(field[i][(b.y)/25] != index && field[i][(b.y)/25] != -1) {
        			return 0;
        		} else {
        			field[i][(b.y)/25] = index;
        		}
        	}
        	
        	// 2 timestamps ahead
        	// find the left bound of the ship for t=t+1
        	if(b.x + b.MaxSpeedX > SCREEN_WIDTH - b.width) {
        		left = SCREEN_WIDTH - b.width -b.MaxSpeedX;
        	} else if (b.x + 2*b.MaxSpeedX > SCREEN_WIDTH - b.width) {
        		left = SCREEN_WIDTH - b.width -1;
        	} else if(b.x + b.MaxSpeedX < 120) {
        		if(b.MaxSpeedX > 0) {
        			left = b.x + 2*b.MaxSpeedX;      			
        		}
        		left = 120 - b.MaxSpeedX;
        	} else if(b.x + 2*b.MaxSpeedX < 120) {
        		left = 120;
        	} else {
        		left = b.x + 2*b.MaxSpeedX;
        	}
        	        	
        	for(int i=left/100;i<=(left+b.width)/100;i++) {
        		if(field[i][(b.y)/25] != index && field[i][(b.y)/25] != -1) {
        			return 1;
        		} else {
        			field[i][(b.y)/25] = index;
        		}
        	}
        	     	
        	// 3 timestamps ahead
        	if(b.x + b.MaxSpeedX > SCREEN_WIDTH - b.width) {
        		left = SCREEN_WIDTH - 2*b.width -b.MaxSpeedX;
        	} else if (b.x + 2*b.MaxSpeedX > SCREEN_WIDTH - b.width) {
        		left = SCREEN_WIDTH - b.width -1 - b.MaxSpeedX;
        	} else if (b.x + 3*b.MaxSpeedX > SCREEN_WIDTH - b.width) {
        		left = SCREEN_WIDTH - b.width -1;
        	} else if(b.x + b.MaxSpeedX < 120) {
        		if(b.MaxSpeedX > 0) {
        			left = b.x + 3*b.MaxSpeedX;      			
        		}
        		left = 120 - 2*b.MaxSpeedX;
        	} else if(b.x + 2*b.MaxSpeedX < 120) {
        		left = 120 - b.MaxSpeedX;
        	} else if (b.x + 3*b.MaxSpeedX < 120) {
        		left = 120;
        	} else {
        		left = b.x + 3*b.MaxSpeedX;
        	}
        	        	
        	for(int i=left/100;i<=(left+b.width)/100;i++) {
        		if(field[i][(b.y)/25] != index && field[i][(b.y)/25] != -1) {
        			return 2;
        		} else {
        			field[i][(b.y)/25] = index;
        		}
        	}        	

        	// 4 timestamps ahead
        	if(b.x + b.MaxSpeedX > SCREEN_WIDTH - b.width) {
        		left = SCREEN_WIDTH - 3*b.width -b.MaxSpeedX;
        	} else if (b.x + 2*b.MaxSpeedX > SCREEN_WIDTH - b.width) {
        		left = SCREEN_WIDTH - b.width -1 - 2*b.MaxSpeedX;
        	} else if (b.x + 3*b.MaxSpeedX > SCREEN_WIDTH - b.width) {
        		left = SCREEN_WIDTH - b.width -1 - b.MaxSpeedX;
        	} else if (b.x + 4*b.MaxSpeedX > SCREEN_WIDTH - b.width) {
        		left = SCREEN_WIDTH - b.width -1;
        	} else if(b.x + b.MaxSpeedX < 120) {
        		if(b.MaxSpeedX > 0) {
        			left = b.x + 4*b.MaxSpeedX;      			
        		}
        		left = 120 - 3*b.MaxSpeedX;
        	} else if(b.x + 2*b.MaxSpeedX < 120) {
        		left = 120 - 2*b.MaxSpeedX;
        	} else if(b.x + 3*b.MaxSpeedX < 120) {
        		left = 120 - b.MaxSpeedX;
        	} else if(b.x + 4*b.MaxSpeedX < 120) {
        		left = 120;
        	} else {
        		left = b.x + 4*b.MaxSpeedX;
        	}
        	        	
        	for(int i=left/100;i<=(left+b.width)/100;i++) {
        		if(field[i][(b.y)/25] != index && field[i][(b.y)/25] != -1) {
        			return 3;
        		} else {
        			field[i][(b.y)/25] = index;
        		}
        	}        	
        	return 4;
        	
        }
        
        public void releaseReservedSpace() {
        	//moving to the right
        	if(x <= destX) {
        		int rbound = Math.min(SCREEN_WIDTH/100 - 2,x/100);
        		for(int i=0;i<rbound-1;i++) {
        			if(field[i][y/25] == blocks.indexOf(this)) {
        				field[i][y/25] = -1;
        			}
        		}
        	} else {
        		for(int i=0;i<x/100-1;i++) {
        			if(field[i][y/25] == blocks.indexOf(this)) {
        				field[i][y/25] = -1;
        			}
        		}
        	}
        }
        
        public boolean reserveOneLineDown() {
        	int index = blocks.indexOf(this);
        	for(int i=x/100;i<=(x+width)/100;i++) {
        		if(field[i][(y/25) + 1] == -1 || field[i][(y/25) +1] == index) {
        			field[i][(y/25)+1] = index;
        		} else {
        			return false;
        		}
        	}
        	return true;
        }
        
        public void releaseReservationsPreviousLane() {
        	int index = blocks.indexOf(this);
        	for(int i = 0;i< SCREEN_WIDTH/100;i++) {
        		if(field[i][(y/25)-1] == index) {
        			field[i][(y/25)-1] = -1;
        		}
        	}
        }
        
        public void releaseFinal() {
        	int index = blocks.indexOf(this); 
        	for(int i=0;i<SCREEN_WIDTH/100;i++) {
        		if(field[i][9] == index) {
        			field[i][9] = -1;
        		}
        	}
        }
        
        public void move(Container container) {     	
        	if(x == destX) {
        		if(y != destY) {
        			if(reserveOneLineDown()) {
        				y += speedY;
        				releaseReservationsPreviousLane();
        			}
        		} else {
        			if(stay > 0) {
        				stay--;
        				x = x -1;
        			} else if (destX == 1700) {
        				finished = true;
        				releaseFinal();
        			} else {
        				destX = 1700;
        			}
        		}
        	} else {
	        	releaseReservedSpace();
	        	speedX = reserveXSpace(this);
	        	if(((MaxSpeedX/4) + x) > destX) {
	        		x = destX;
	        		speedX = 0;
	        		System.out.println("Block "+blocks.indexOf(this) +" reached destX.");
	        	} else {
	        		speedX = Math.min(speedX, 1+((4*(destX-x))/MaxSpeedX));
	                x += (speedX*MaxSpeedX)/4;
	        	}
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