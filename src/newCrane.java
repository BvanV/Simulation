import java.awt.Color;
import java.awt.Graphics;



public class newCrane extends newBlock {
	private int CONTAINER_LENGTH = 20;
	private int CONTAINER_WIDTH = 5;
	private int CONTAINER_FIELD_OFFSET = 300;
	private int WATER_HEIGHT = 400;

	
	newBlock[] parts;
	boolean done = true;
	boolean busy = false;
	boolean startedThisShip = false;
	int ShipToHandle;
	int leftRow;		// index of left most row currently in scope
	int ysize;
	int xsize;
	newTFE currentContainer;
	boolean hasContainer = false;
	int indexCurrentContainer = -1;		// TEMP VALUE
	int containerOriginXpos;		// 'Row' on the ship
	int containerOriginYpos;		// 'Column' on the ship
	int containerDestinationX;
	int containerDestinationY;
	boolean containerIsDone = true;
	
	int destYCrane;
	int destYRod;
	int destXClip;
	
	public newCrane(int ind, int x, int y) {
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
        parts = new newBlock[7];
	
        //Initialize left arm
		parts[0] 		= new newBlock();
		parts[0].heigth = 7 * CONTAINER_WIDTH;
		parts[0].width 	= CONTAINER_WIDTH;
		parts[0].x		= this.x;
		parts[0].y		= this.y - parts[0].heigth;
		parts[0].red	= 255;
		parts[0].green	= 51;
		parts[0].blue	= 51;

        //Initialize right arm
		parts[1] 		= new newBlock();
		parts[1].heigth = 7 * CONTAINER_WIDTH;
		parts[1].width 	= CONTAINER_WIDTH;
		parts[1].x		= this.x + 4 * CONTAINER_LENGTH + CONTAINER_WIDTH + 2 * (1 + (CONTAINER_WIDTH / 5));
		parts[1].y		= this.y - parts[1].heigth;
		parts[1].red	= 255;
		parts[1].green	= 51;
		parts[1].blue	= 51;
		
		//initialize Rod
		parts[2] 		= new newBlock();
		parts[2].heigth = CONTAINER_WIDTH / 2;
		parts[2].width 	= 4 * CONTAINER_LENGTH + 2 * (1 + (CONTAINER_WIDTH / 5));
		parts[2].x		= this.x + CONTAINER_WIDTH;
		parts[2].y		= this.y - CONTAINER_WIDTH - (CONTAINER_WIDTH / 2);
		parts[2].red	= 255;
		parts[2].green	= 51;
		parts[2].blue	= 51;   		
		
		//initialize Clipleft
		parts[3] 		= new newBlock();
		parts[3].heigth = CONTAINER_WIDTH;
		parts[3].width 	= 1 + (CONTAINER_WIDTH / 5);
		parts[3].x		= parts[2].x;
		parts[3].y		= parts[2].y - CONTAINER_WIDTH;
		parts[3].red	= 255;
		parts[3].green	= 51;
		parts[3].blue	= 51;
		
		//initialize Clipright
		parts[4] 		= new newBlock();
		parts[4].heigth = CONTAINER_WIDTH;
		parts[4].width 	= 1 + (CONTAINER_WIDTH / 5);
		parts[4].x		= parts[3].x + parts[3].width + CONTAINER_LENGTH;
		parts[4].y		= parts[3].y;
		parts[4].red	= 255;
		parts[4].green	= 51;
		parts[4].blue	= 51;
		
		//initialize Clip top leftt
		parts[5] 		= new newBlock();
		parts[5].heigth = 1 + (CONTAINER_WIDTH / 5);
		parts[5].width 	= 1 + (CONTAINER_WIDTH / 5) + (CONTAINER_LENGTH / 5);
		parts[5].x		= parts[3].x;
		parts[5].y		= parts[3].y - parts[5].heigth;
		parts[5].red	= 255;
		parts[5].green	= 51;
		parts[5].blue	= 51;
		
		//initialize Clip top right
		parts[6] 		= new newBlock();
		parts[6].heigth = parts[5].heigth;
		parts[6].width 	= parts[5].width;
		parts[6].x		= parts[4].x + parts[4].width - parts[6].width;
		parts[6].y		= parts[5].y;
		parts[6].red	= 255;
		parts[6].green	= 51;
		parts[6].blue	= 51;
	}
	
    public void move(CustomContainer container) {
		if(!done) {
			if(!startedThisShip && !busy) {	// start with this ship
				startedThisShip = true;
				busy = true;
//    			ysize = blocks[ShipToHandle].ysize;
//    			xsize = blocks[ShipToHandle].xsize;
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
//						blocks[ShipToHandle].done = false;
						if(leftRow < 1) {
							startedThisShip = false;
						}
					} else { // a new container is found
						containerIsDone = false;
						containerOriginXpos = indexCurrentContainer % xsize;
						containerOriginYpos = indexCurrentContainer / xsize;
						destYCrane = WATER_HEIGHT + CONTAINER_WIDTH;
						destYRod = WATER_HEIGHT - (CONTAINER_WIDTH / 2) - ((ysize - 1 - containerOriginYpos) * CONTAINER_WIDTH);
						destXClip = parts[0].x + parts[0].width + ((containerOriginYpos - leftRow) * CONTAINER_LENGTH);
					}
				} else {		// do stuff multiple times
					if(!hasContainer && y > destYCrane) { 		//move crane to the right position
						moveVert(-1);
					} else if(!hasContainer && parts[2].y > destYRod) {	// move Rod to right position
						moveRodVert(-1);
					} else if(!hasContainer && parts[3].x < destXClip) {					//move clips to the right
						moveClipsHoriz(1);
					} else if(!hasContainer && parts[3].x > destXClip) {					//move clips to the right
						moveClipsHoriz(-1);						
					} else if(!hasContainer) {		// Take the container
						takeContainer(indexCurrentContainer);
//						int ypos = container_field.nextFreeY;
//						destYCrane = WATER_HEIGHT + ((ypos + 8) * CONTAINER_WIDTH);
					} else if(y < destYCrane) {		// Move container to the left
						moveVert(1);
					} else if(parts[2].y < this.y - CONTAINER_WIDTH - (CONTAINER_WIDTH / 2)) {		// move rod downwards
						moveRodVert(1);			
//					} else if(parts[3].x > parts[2].x+ (container_field.nextFreeX * CONTAINER_LENGTH)) {	// move clip to the left
						moveClipsHoriz(-1);
//					} else if(parts[3].x < parts[2].x+ (container_field.nextFreeX * CONTAINER_LENGTH)) {	// move clip to the right
						moveClipsHoriz(1);
					} else {		// container arrived at destination
						releaseContainer();
					}
					
				}
			}
		}
	}
	
	/**
	 * Takes the container with index from ShipToHandle
	 */
	public void takeContainer(int index) {
//		currentContainer = blocks[ShipToHandle].blokjes[index];
//		blocks[ShipToHandle].blokjes[index] = null;
		hasContainer = true;
	}
	
	/**
	 * Releases the current container
	 */
	public void releaseContainer() {
//		container_field.addBlock(currentContainer);
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