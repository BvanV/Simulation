import java.awt.Color;
import java.awt.Graphics;



public class Crane extends Block {
	private static final long serialVersionUID = -9076651078827732361L;
	private int CONTAINER_LENGTH 		= MainPanel.CONTAINER_LENGTH;
	private int CONTAINER_WIDTH 		= MainPanel.CONTAINER_WIDTH;
	private int WATER_HEIGHT 			= MainPanel.WATER_HEIGHT;
	private int CONTAINER_FIELD_OFFSET	= MainPanel.CONTAINER_FIELD_OFFSET;

	
	Block[] parts;
	boolean done = true;
	boolean busy = false;
	boolean startedThisShip = false;
	CraneJob currentJob;
	int craneStatus;
	int ShipToHandle;
	int leftRow;		// index of left most row currently in scope
	int ysize;
	int xsize;
	TFE currentContainer;
	boolean hasContainer = false;
	int indexCurrentContainer = -1;		// TEMP VALUE
	int containerOriginXpos;		// 'Row' on the ship
	int containerOriginYpos;		// 'Column' on the ship
	int containerDestinationX;		// 'Row' on field
	int containerDestinationY;		// 'Column' on field
	boolean containerIsDone = true;
	
	int destYCrane;
	int destXCrane;
	int destYRod;
	int destXClip;
	
	
	final int CR_WAITING 					= 0;
	final int CR_MOVE_TO_COAST 				= 1;
	final int CR_MOVE_ALONG_COAST_TO_TFE	= 2;
	final int CR_MOVE_ROD_TO_TFE_ON_SHIP	= 3;
	
	final int CR_REMOVE_TFE_FROM_SHIP		= 4;
	final int CR_MOVE_ALONG_COAST_TO_FIELD	= 5;
	final int CR_MOVE_TFE_TO_FIELD_POS		= 6;
	final int CR_MOVING 				= 99;
	
	
	public Crane(int ind, int x, int y) {
		super();
        speedX 	= 0;
        speedY 	= 0;
        red 	= 255;
        green 	= 51;
        blue 	= 51;
        index	= ind;
        width 	= 4 * CONTAINER_LENGTH + 2 * CONTAINER_WIDTH + 2 * (1 + (CONTAINER_WIDTH / 5));
        heigth 	= 2 * CONTAINER_LENGTH;
    	this.x 	= x;
    	this.y 	= y;
        MaxSpeedX = 0;
        parts = new Block[7];
        craneStatus = CR_WAITING;
	
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
	
	public void addJob(CraneJob cj) {
		if(currentJob == null) {
			currentJob = cj;
		} else {
			CraneJob pointer = currentJob;
			CraneJob next = pointer.getNextJob();
			while(next != null) {
				pointer = next;
				next = pointer.getNextJob();
			}
			pointer.setNextJob(cj);
		}
	}
	
	/**
	 * Prepare the move and call the moveCrane method
	 * @param container
	 */
    public void move(CustomContainer container) {
    	//Decide what to do;
    	if(craneStatus == CR_WAITING) {
    		if(currentJob != null) {
    			applyJob();
    		}
    	}
    	if(craneStatus == CR_MOVE_TO_COAST) {
    		if(y != destYCrane) {
    			moveCrane(container);
    		} else {
    			craneStatus = CR_MOVE_ALONG_COAST_TO_TFE;
    		}
    	} 
    	if(craneStatus == CR_MOVE_ALONG_COAST_TO_TFE) {
    		if(x != destXCrane) {
    			moveCrane(container);
    		} else {
    			craneStatus = CR_MOVE_ROD_TO_TFE_ON_SHIP;
    		}
    	}
    	if(craneStatus == CR_MOVE_ROD_TO_TFE_ON_SHIP) {
    		if(parts[2].y > destYRod) {
    			moveCrane(container);
    		}
    	}
    		    	
    }
      
    /**
     * execute the actual move
     * @param container
     */
    private void moveCrane(CustomContainer container) {
    	if(craneStatus == CR_MOVE_TO_COAST) {
    		if(y > destYCrane) {
    			moveVert(-1);
    		} 
    	} 
    	if(craneStatus == CR_MOVE_ALONG_COAST_TO_TFE) {
    		if(x > destXCrane) {
    			moveHoriz(-1);
    		} else if(x < destXCrane) {
    			moveHoriz(1);
    		}
    	} 
    	if(craneStatus == CR_MOVE_ROD_TO_TFE_ON_SHIP) {
    		if(parts[2].y > destYRod) {
    			moveRodVert(-1);
    		}
    	}
	}

    /**
     * start with a new job
     */
    public void applyJob() {
    	destYCrane 	= WATER_HEIGHT + CONTAINER_WIDTH;
    	destXCrane 	= CONTAINER_FIELD_OFFSET + (currentJob.getTFEOnBoardX() * CONTAINER_LENGTH);
    	ysize		= currentJob.getShipYSize();
    	destYRod	= WATER_HEIGHT - ysize * CONTAINER_WIDTH + CONTAINER_WIDTH/2 + currentJob.getTFEOnBoardY() * CONTAINER_WIDTH;
		craneStatus = CR_MOVE_TO_COAST;
		if(currentJob != null) {
			CraneJob next = currentJob.getNextJob();
			currentJob = next;
		}
    }
    
	/**
	 * Takes the container with index from ShipToHandle
	 */
	public void takeContainer(int index) {
		hasContainer = true;
	}
	
	/**
	 * Releases the current container
	 */
	public void releaseContainer() {
		currentContainer 		= null;
		containerIsDone	= true;
		hasContainer 	= false;				
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
		if(hasContainer) {
			currentContainer.setY(currentContainer.getY() + dist);
		}
	}

	/**
	 * move the crane in horizontal direction
	 * @param dist move distance, positive is to the right.
	 */
	public void moveHoriz(int dist) {
		x += dist;
		for(int i=0;i<parts.length;i++) {
			parts[i].x += dist;
		}
		if(hasContainer) {
			currentContainer.setX(currentContainer.getX() + dist);
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
		if(hasContainer) {
			currentContainer.setY(currentContainer.getY() + dist);
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
		if(hasContainer) {
			currentContainer.setX(currentContainer.getX() + dist);
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
	
	@Override
    public void draw(Graphics g) {
        g.setColor(new Color(red, green, blue));
        g.fillRect(x, y, width, heigth);
      	for(int j=0;j<parts.length;j++) {
      		parts[j].draw(g);
      		if(hasContainer) {
      			currentContainer.draw(g);
      		}
      	}
        
    }

	
}