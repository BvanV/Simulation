
public class MoveJob extends Job {
	private int ship;
	private int destX;

	public MoveJob(Condition[] c, int s, int dx) {
		super();
		conditions = c;
		ship = s;
		destX = dx;		// the Losslocation
	}

	public int getShip() {
		return ship;
	}

	public void setShip(int ship) {
		this.ship = ship;
	}

	public int getDestX() {
		return destX;
	}

	public void setDestX(int destX) {
		this.destX = destX;
	}
}
