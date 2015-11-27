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
 * v1.07 Collision avoidance by claiming fields. Ships move to destination
 * v1.08 Build a crane 
 * 
 * v2.00 Move the crane to the 'first' container of a Ship
 */
public class MovingBlocksv1_08 extends JPanel implements MouseListener {

	/**
	 * 
	 */
	private static String TITLE = "Moving blocks v1.08";
	private static int SCREEN_WIDTH = 1800;
	private static int SCREEN_HEIGHT = 1000;
	private static int WATER_HEIGHT = 300;
	private static int NUMBER_OF_WATER_LANES = WATER_HEIGHT / 25; // Not used?
	private static int MAX_NUMBER_OF_BLOCKS = 20;
	private static int CONTAINER_LENGTH = 20;
	private static int CONTAINER_WIDTH = (CONTAINER_LENGTH * 2438)/6096;
	private static int CONTAINER_FIELD_WIDTH = 4 * CONTAINER_LENGTH + 4 * CONTAINER_WIDTH;
	private static int CONTAINER_FIELD_HEIGTH = ((SCREEN_HEIGHT - WATER_HEIGHT) * 4) / 5  ;
	private static int CONTAINER_FIELD_OFFSET = SCREEN_WIDTH - CONTAINER_FIELD_WIDTH - (20 * CONTAINER_LENGTH);
	private static int FIELD_WIDTH = CONTAINER_LENGTH * 20;
	private static int[][][] field = new int[SCREEN_WIDTH/FIELD_WIDTH + 1][1][2];		// reserve fields to avoid collisions. --Currently only one lane
	private static final long serialVersionUID = 1L;
    protected static Ship[] blocks = new Ship[MAX_NUMBER_OF_BLOCKS];
    private static Block water;
    private static Block container_field;
    private static Crane[] cranes = new Crane[1];
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

    public MovingBlocksv1_08(int width, int height) {

        canvasWidth = width;
        canvasHeight = height;

        container = new Container();

        canvas = new DrawCanvas();
        this.setLayout(new BorderLayout());
        this.add(canvas, BorderLayout.CENTER);
        this.addMouseListener(this);
        for(int i=0;i<(SCREEN_WIDTH/(CONTAINER_LENGTH * 20) + 1);i++) {
        	for(int j=0; j<2;j++) {
        		field[i][0][j] = -1; 
        	}
        }
        water = new Block(0,0,1800,300,51,153,255);
        cranes[0] = new Crane(0);
        container_field = new Block(CONTAINER_FIELD_OFFSET, WATER_HEIGHT,CONTAINER_FIELD_WIDTH, CONTAINER_FIELD_HEIGTH, 255, 204, 153);
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
    				blocks[i].releaseAll();
    			} else {
    				blocks[i] = null;
    			}
    		}
    	}
   		cranes[0].move(container);
 
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
            cranes[0].draw(g);
            for(int i=0;i<=0;i++) {
            	for(int j=0;j<cranes[i].parts.length;j++) {
            		cranes[i].parts[j].draw(g);
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
                f.setContentPane(new MovingBlocksv1_08(SCREEN_WIDTH, SCREEN_HEIGHT));
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
        	x 		= 0;
            y 		= 25*random(NUMBER_OF_WATER_LANES - 1);
            MaxSpeedX = 1 + (CONTAINER_LENGTH/10) + random(CONTAINER_LENGTH / 2);
            speedX 	= 0;
            speedY 	= 0;
            radius 	= random(20);
            red 	= 255*random(1);
            green 	= 255*random(1);
            blue 	= 255*random(1);
            i 		= 0;
            width 	= CONTAINER_LENGTH;
            heigth 	= CONTAINER_WIDTH; 
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
        	y 		= random(WATER_HEIGHT - heigth);
            MaxSpeedX = 1 + (CONTAINER_LENGTH/10) + random(CONTAINER_LENGTH / 2);
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
    	int xsize;
    	int ysize;
    	int destX;
    	boolean done;
    	int tempCounter;

		public Ship(int ind) {
			super();
        	ysize	= 2 + random(3);
        	xsize	= 2*ysize + random(2);
    		size	= xsize * ysize;
            speedX 	= 0;
            speedY 	= 0;
            red 	= 160;
            green 	= 160;
            blue 	= 160;
            index	= ind;
            done	= false;
            tempCounter = 0;
            width 	= 2 * (CONTAINER_LENGTH / 2) + (xsize * CONTAINER_LENGTH);
            heigth 	= 2 * (CONTAINER_WIDTH / 2) + (ysize * CONTAINER_WIDTH);
        	x 		= 0 - width;
        	y 		= WATER_HEIGHT - heigth;
        	destX	= CONTAINER_FIELD_OFFSET + 2 * CONTAINER_WIDTH + ((9 * CONTAINER_LENGTH) / 2) - width;
            MaxSpeedX = 1 + (CONTAINER_LENGTH/10) + random(CONTAINER_LENGTH / 2);
            blokjes = new Block[xsize*ysize];
            for(int i=0;i<ysize;i++) {
            	for(int j=0;j<xsize;j++) {
            		blokjes[i*xsize + j] = new Block(i*xsize + j);
            		blokjes[i*xsize + j].x = x + (CONTAINER_LENGTH / 2) + j * CONTAINER_LENGTH;
            		blokjes[i*xsize + j].y = y + (CONTAINER_WIDTH / 2) + i * CONTAINER_WIDTH;
            	}
            }
        }
		
		@Override
        public void move(Container container) {
			if(!done) {
				reserveSpace();
				int next = getNextField();
				if(x >= destX) {
					if(xsize > 4 && x < CONTAINER_FIELD_OFFSET){
						destX += CONTAINER_LENGTH * 4;
						done = true;
						cranes[0].done = false;
						cranes[0].ShipToHandle = index;
					} else {
						destX = SCREEN_WIDTH;
						done = true;
						cranes[0].done = false;
						cranes[0].ShipToHandle = index;
					}
				} else if(next >= field.length || field[next][0][0] == index) {
					x += MaxSpeedX;
					int correction = 0;
					if(x > destX) {
						correction = x - destX;
						x = destX;
					}
		        	for(int i=0;i<size;i++) {
		        		blokjes[i].x += (MaxSpeedX - correction);
		        	}
				}
				releaseSpace();
			}
		}
		
		public void releaseAll() {
			for(int i=0; i< field.length;i++) {
				if(field[i][0][0] == index) {
					field[i][0][0] = -1;
				}
			}
		}
				
		/**
		 * 
		 * @return the index of the next field to enter. Returns -1 if there is no next field to enter
		 */
		public int getNextField() {
			int nextfield = (x + width) / FIELD_WIDTH;
			nextfield++;
			return nextfield; 
		}
		
		/**
		 * 
		 * @return true if it succeeded to reserve the next field, false otherwise.
		 */
		public void reserveSpace() {
			int next = getNextField();
			if(next != -1 && next < field.length) {
				int goal = field[next][0][0];
				if(goal == -1 || goal == index) {
					field[next][0][0] = index;
				}
				
			}
			
		}

		/**
		 * 
		 * @return the index of the last field passed
		 */
		public int getPreviousField() {
			int prevfield = x / FIELD_WIDTH;
			prevfield--;
			if(prevfield < 0) {
				return -1;
			} else if(prevfield >= field.length) {
				return -1;
			}
			return prevfield; 
		}


		public void releaseSpace() {
			int prev = getPreviousField();
			if(prev != -1 && field[prev][0][0] == index) {
				field[prev][0][0] = -1;
			}
		}
    }
    
    public static class Crane extends Block {
    	Block[] parts;
    	boolean done = true;
    	boolean busy = false;
    	boolean startedThisShip = false;
    	int ShipToHandle;
    	int leftRow;		// index of left most row currently in scope
    	int ysize;
    	int xsize;
    	Block currentContainer;
    	boolean hasContainer = false;
    	int indexCurrentContainer = -1;		// TEMP VALUE
    	int containerOriginXpos;		// 'Row' on the ship
    	int containerOriginYpos;		// 'Column' on the ship
    	int containerDestinationX;
    	int containerDestinationY;
    	boolean containerIsDone = true;
    	
    	
    	public Crane(int ind) {
    		super();
            speedX 	= 0;
            speedY 	= 0;
            red 	= 255;
            green 	= 51;
            blue 	= 51;
            index	= ind;
            width 	= 4 * CONTAINER_LENGTH + 2 * CONTAINER_WIDTH + 2 * (1 + (CONTAINER_WIDTH / 5));
            heigth 	= 2 * CONTAINER_LENGTH;
        	x 		= CONTAINER_FIELD_OFFSET + CONTAINER_WIDTH - (1 + (CONTAINER_WIDTH / 5));
        	y 		= WATER_HEIGHT + 8 * CONTAINER_WIDTH;
            MaxSpeedX = 0;
            parts = new Block[7];
   		
            //Initialize left arm
    		parts[0] 		= new Block();
    		parts[0].heigth = 7 * CONTAINER_WIDTH;
    		parts[0].width 	= CONTAINER_WIDTH;
    		parts[0].x		= this.x;
    		parts[0].y		= this.y - parts[0].heigth;
    		parts[0].red	= 255;
    		parts[0].green	= 51;
    		parts[0].blue	= 51;

            //Initialize right arm
    		parts[1] 		= new Block();
    		parts[1].heigth = 7 * CONTAINER_WIDTH;
    		parts[1].width 	= CONTAINER_WIDTH;
    		parts[1].x		= this.x + 4 * CONTAINER_LENGTH + CONTAINER_WIDTH + 2 * (1 + (CONTAINER_WIDTH / 5));
    		parts[1].y		= this.y - parts[1].heigth;
    		parts[1].red	= 255;
    		parts[1].green	= 51;
    		parts[1].blue	= 51;
    		
    		//initialize Rod
    		parts[2] 		= new Block();
    		parts[2].heigth = CONTAINER_WIDTH / 2;
    		parts[2].width 	= 4 * CONTAINER_LENGTH + 2 * (1 + (CONTAINER_WIDTH / 5));
    		parts[2].x		= this.x + CONTAINER_WIDTH;
    		parts[2].y		= this.y - CONTAINER_WIDTH - (CONTAINER_WIDTH / 2);
    		parts[2].red	= 255;
    		parts[2].green	= 51;
    		parts[2].blue	= 51;   		
    		
    		//initialize Clipleft
    		parts[3] 		= new Block();
    		parts[3].heigth = CONTAINER_WIDTH;
    		parts[3].width 	= 1 + (CONTAINER_WIDTH / 5);
    		parts[3].x		= parts[2].x;
    		parts[3].y		= parts[2].y - CONTAINER_WIDTH;
    		parts[3].red	= 255;
    		parts[3].green	= 51;
    		parts[3].blue	= 51;
    		
    		//initialize Clipright
    		parts[4] 		= new Block();
    		parts[4].heigth = CONTAINER_WIDTH;
    		parts[4].width 	= 1 + (CONTAINER_WIDTH / 5);
    		parts[4].x		= parts[3].x + parts[3].width + CONTAINER_LENGTH;
    		parts[4].y		= parts[3].y;
    		parts[4].red	= 255;
    		parts[4].green	= 51;
    		parts[4].blue	= 51;
    		
    		//initialize Clip top leftt
    		parts[5] 		= new Block();
    		parts[5].heigth = 1 + (CONTAINER_WIDTH / 5);
    		parts[5].width 	= 1 + (CONTAINER_WIDTH / 5) + (CONTAINER_LENGTH / 5);
    		parts[5].x		= parts[3].x;
    		parts[5].y		= parts[3].y - parts[5].heigth;
    		parts[5].red	= 255;
    		parts[5].green	= 51;
    		parts[5].blue	= 51;
    		
    		//initialize Clip top right
    		parts[6] 		= new Block();
    		parts[6].heigth = parts[5].heigth;
    		parts[6].width 	= parts[5].width;
    		parts[6].x		= parts[4].x + parts[4].width - parts[6].width;
    		parts[6].y		= parts[5].y;
    		parts[6].red	= 255;
    		parts[6].green	= 51;
    		parts[6].blue	= 51;
    	}
    	
		@Override
        public void move(Container container) {
			if(!done) {
				if(!startedThisShip && !busy) {	// start with this ship
					startedThisShip = true;
					busy = true;
	    			ysize = blocks[ShipToHandle].ysize;
	    			xsize = blocks[ShipToHandle].xsize;
	    			leftRow = xsize - 4;
				} else if(startedThisShip && !busy) {	//restart after ship movement
					busy = true;
					leftRow -= 4;
				} else if(startedThisShip && busy) {	// repeated step
					if(containerIsDone) {
						indexCurrentContainer = nextContainerToHandle();
						if(indexCurrentContainer == -1) { // No container to handle
							busy = false;
							done = true;
							blocks[ShipToHandle].done = false;
							if(leftRow < 1) {
								startedThisShip = false;
							}
						} else { // a new container is found
							containerIsDone = false;
							containerOriginXpos = indexCurrentContainer % xsize;
							containerOriginYpos = indexCurrentContainer / xsize;
						}
					} else {		// do stuff multiple times
						if(!hasContainer) {
							if(y > WATER_HEIGHT + CONTAINER_WIDTH) { 		//move crane to the right position
								moveVert(-1);
							} else if(parts[2].y > WATER_HEIGHT - (CONTAINER_WIDTH / 2) - ((ysize - 1 - containerOriginYpos) * CONTAINER_WIDTH)) {	// move Rod to right position
								moveRodVert(-1);
							} else if(parts[3].x< parts[0].x + parts[0].width + ((containerOriginYpos - leftRow) * CONTAINER_LENGTH)) {					//move clips to the right
								moveClipsHoriz(1);
							}
						}
					}
				}
			}
    	}
		
		/**
		 * move the crane in vertical direction
		 * @param dist move distance, positive is downwards.
		 */
		public void moveVert(int dist) {
			y += dist;
			for(int i=0;i<parts.length;i++) {
				parts[i].y += dist;
			}
		}

		/**
		 * move the Rod in vertical direction
		 * @param dist move distance, positive is downwards
		 */
		public void moveRodVert(int dist) {
			for(int i=2;i<7;i++) {
				parts[i].y += dist;
			}
		}
		
		/**
		 * move the clips in horizontal direction
		 * @param dist move distance, positive is to the right
		 */
		public void moveClipsHoriz(int dist) {
			for(int i=3;i<7;i++) {
				parts[i].x += dist;
			}
		}
		
		/**
		 * 
		 * @return the index of the next container to take from the ship. Returns -1 if there are no containers left on the ship to handle
		 */
		public int nextContainerToHandle() {
			
			// TEMPORARY
			if(indexCurrentContainer != 0 && leftRow < 1) {
				return 0;
			} else {
				return -1;
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