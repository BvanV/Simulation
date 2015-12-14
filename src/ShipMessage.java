
public class ShipMessage {
	int shipIndex;
	boolean removeTFE;
	int rmTFEX;
	int rmTFEY;
	
	public ShipMessage(int index, boolean rmTFE, int x, int y) {
		shipIndex 	= index;
		removeTFE 	= rmTFE;
		rmTFEX 		= x;
		rmTFEY		= y;
	}

	public boolean isRemoveTFE() {
		return removeTFE;
	}

	public void setRemoveTFE(boolean removeTFE) {
		this.removeTFE = removeTFE;
	}

	public int getRmTFEX() {
		return rmTFEX;
	}

	public void setRmTFEX(int rmTFEX) {
		this.rmTFEX = rmTFEX;
	}

	public int getRmTFEY() {
		return rmTFEY;
	}

	public void setRmTFEY(int rmTFEY) {
		this.rmTFEY = rmTFEY;
	}

	public int getShipIndex() {
		return shipIndex;
	}

	public void setShipIndex(int shipIndex) {
		this.shipIndex = shipIndex;
	}
	
}
