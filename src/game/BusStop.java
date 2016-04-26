package game;

public class BusStop {
	
	private final int index;
	private final int x;
	private final int y;
	private final int squareDir;
	private final WaitSquare waitSquare;
	private boolean active = false;
	
	public BusStop(int index, int x, int y, int squareDir, int capacity) {
		this.index = index;
		this.x = x;
		this.y = y;
		this.squareDir = squareDir;
		waitSquare = new WaitSquare(x, y, capacity);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getSquareDir() {
		return squareDir;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public WaitSquare getWaitSquare() {
		return waitSquare;
	}

	public int getIndex() {
		return index;
	}
	
}
