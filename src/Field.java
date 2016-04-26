
public class Field extends Block {
	private TFE[][] blocks;
	private boolean[][] reservations;
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
		reservations = new boolean[x][y];
	}
 
    public Field(int px, int py, int pwidth, int pheigth, int pred, int pgreen, int pblue, int sizeOfX, int sizeOfY) {
    	super();
		xsize = sizeOfX;
		ysize = sizeOfY;
		blocks = new TFE[xsize][ysize];
		reservations = new boolean[xsize][ysize];
    	x 		= px;
        y 		= py;
        width 	= pwidth;
        heigth 	= pheigth;      	
        red 	= pred;
        green 	= pgreen;
        blue 	= pblue;
    }
	
	public boolean reserveNext() {
		if(nextFreeX == -1 || nextFreeY == -1) {
			return false;
		}
		try {
			reservations[nextFreeX][nextFreeY] = false;
		} catch(Exception e) {}
		updateNext();
		return true;
	}
	
	public void updateNext() {
		boolean done = false;
		int i = 0;
		while(!done && i < ysize) {
			int j = 0;
			while(!done && j < xsize) {
				if(blocks[j][i] == null && !reservations[j][i]) {
					nextFreeX = j;
					nextFreeY = i;
					done = true;
				}
				j++;
			}
			i++;
		}
	}
	
	public void addTFE(int x, int y, TFE t) {
		blocks[x][y] = t;
		reservations[x][y] = false;
		updateNext();
	}
	
	public TFE getTFE(int x, int y) {
		return blocks[x][y];
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