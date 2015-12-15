
public class WaterSpace {
	private WaterSpace left;
	private WaterSpace right;
	private int xLeft;
	private int xRight;
	private Ship ship;
	
	public WaterSpace(Ship s, WaterSpace r, int xr) {
		left 	= null;
		right 	= r;
		xLeft 	= 0;
		xRight 	= xr;
		ship 	= s;
	}

	public WaterSpace getLeft() {
		return left;
	}

	public void setLeft(WaterSpace left) {
		this.left = left;
	}

	public WaterSpace getRight() {
		return right;
	}

	public void setRight(WaterSpace right) {
		this.right = right;
	}

	public int getxLeft() {
		return xLeft;
	}

	public void setxLeft(int xLeft) {
		this.xLeft = xLeft;
	}

	public int getxRight() {
		return xRight;
	}

	public void setxRight(int xRight) {
		this.xRight = xRight;
	}

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}
}
