package game;

import java.util.LinkedList;
import java.util.Queue;

public class WaitSquare {

	private final int xPos;
	private final int yPos;
	private final int capacity;
	private final Queue<Man> travellers;
	
	public WaitSquare(int x, int y, int capacity) {
		xPos = x;
		yPos = y;
		this.capacity = capacity;
		travellers = new LinkedList<Man>();
	}

	public int getxPos() {
		return xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public int getCapacity() {
		return capacity;
	}

	public Queue<Man> getTravellers() {
		return travellers;
	}

	
}
