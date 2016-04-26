
public class CraneMessage {
	int CraneIndex = 0;
	int lossLocation = 3;
	boolean removedFromShip;
	int xOnShip;
	int yOnShip;
	
	public CraneMessage(boolean rm, int x, int y) {
		removedFromShip = rm;
		xOnShip = x;
		yOnShip = y;
	}

	public int getCraneIndex() {
		return CraneIndex;
	}

	public void setCraneIndex(int craneIndex) {
		CraneIndex = craneIndex;
	}

	public int getLossLocation() {
		return lossLocation;
	}

	public void setLossLocation(int lossLocation) {
		this.lossLocation = lossLocation;
	}

	public boolean isRemovedFromShip() {
		return removedFromShip;
	}

	public void setRemovedFromShip(boolean removedFromShip) {
		this.removedFromShip = removedFromShip;
	}

	public int getxOnShip() {
		return xOnShip;
	}

	public void setxOnShip(int xOnShip) {
		this.xOnShip = xOnShip;
	}

	public int getyOnShip() {
		return yOnShip;
	}

	public void setyOnShip(int yOnShip) {
		this.yOnShip = yOnShip;
	}

	
}
