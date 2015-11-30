
public class newField extends newBlock {
	newTFE[][] blocks;
	//position of the next free place
	int nextFreeX = 0;
	int nextFreeY = 0;
	int xsize;
	int ysize;
    	
    	
	public newField(int x, int y) {
		super();
		xsize = x;
		ysize = y;
		blocks = new newTFE[x][y];
	}
 
    public newField(int px, int py, int pwidth, int pheigth, int pred, int pgreen, int pblue, int sizeOfX, int sizeOfY) {
    	super();
		xsize = sizeOfX;
		ysize = sizeOfY;
		blocks = new newTFE[xsize][ysize];        	
    	x 		= px;
        y 		= py;
        width 	= pwidth;
        heigth 	= pheigth;      	
        red 	= pred;
        green 	= pgreen;
        blue 	= pblue;
    }
    	
	public void addBlock(newTFE b) {
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
}