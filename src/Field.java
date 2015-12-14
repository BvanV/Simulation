
public class Field extends Block {
	private TFE[][] blocks;
	//position of the next free place
	private int nextFreeX = 0;
	private int nextFreeY = 0;
	private int xsize;
	private int ysize;
    	
    	
	public Field(int x, int y) {
		super();
		xsize = x;
		ysize = y;
		blocks = new TFE[x][y];
	}
 
    public Field(int px, int py, int pwidth, int pheigth, int pred, int pgreen, int pblue, int sizeOfX, int sizeOfY) {
    	super();
		xsize = sizeOfX;
		ysize = sizeOfY;
		blocks = new TFE[xsize][ysize];        	
    	x 		= px;
        y 		= py;
        width 	= pwidth;
        heigth 	= pheigth;      	
        red 	= pred;
        green 	= pgreen;
        blue 	= pblue;
    }
    	
	public void addBlock(TFE b) {
		blocks[nextFreeX][nextFreeY] = b;
		boolean done = false;
		int i = 0;
		while(!done && i < ysize) {
			int j = 0;
			while(!done && j < xsize) {
				if(blocks[j][i] == null) {
					nextFreeX = j;
					nextFreeY = i;
					done = true;
				}
				j++;
			}
			i++;
		}
	}

	public TFE[][] getBlocks() {
		return blocks;
	}

	public void setBlocks(TFE[][] blocks) {
		this.blocks = blocks;
	}

	public int getNextFreeX() {
		return nextFreeX;
	}

	public void setNextFreeX(int nextFreeX) {
		this.nextFreeX = nextFreeX;
	}

	public int getNextFreeY() {
		return nextFreeY;
	}

	public void setNextFreeY(int nextFreeY) {
		this.nextFreeY = nextFreeY;
	}

	public int getXsize() {
		return xsize;
	}

	public void setXsize(int xsize) {
		this.xsize = xsize;
	}

	public int getYsize() {
		return ysize;
	}

	public void setYsize(int ysize) {
		this.ysize = ysize;
	}
}