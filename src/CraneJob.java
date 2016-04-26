
public class CraneJob {
	private CraneJob nextJob;
	private int shipIndex;
	private int shipYSize;
	private int TFEOnBoardX;
	private int TFEOnBoardY;
	private int TFEOnFieldX;
	private int TFEOnFieldY;
	private Ship ship;
	
	public CraneJob(Ship sh,int shipind, int ysize, int onBoardX, int onBoardY, int onFieldX, int onFieldY) {
		ship		= sh;
		shipIndex 	= shipind;
		shipYSize	= ysize;
		TFEOnBoardX = onBoardX;
		TFEOnBoardY = onBoardY;
		TFEOnFieldX = onFieldX;
		TFEOnFieldY = onFieldY;		
	}

	public CraneJob getNextJob() {
		return nextJob;
	}

	public void setNextJob(CraneJob nextJob) {
		this.nextJob = nextJob;
	}

	public int getShipIndex() {
		return shipIndex;
	}

	public void setShipIndex(int shipIndex) {
		this.shipIndex = shipIndex;
	}

	public int getTFEOnBoardX() {
		return TFEOnBoardX;
	}

	public void setTFEOnBoardX(int tFEOnBoardX) {
		TFEOnBoardX = tFEOnBoardX;
	}

	public int getTFEOnBoardY() {
		return TFEOnBoardY;
	}

	public void setTFEOnBoardY(int tFEOnBoardY) {
		TFEOnBoardY = tFEOnBoardY;
	}

	public int getTFEOnFieldX() {
		return TFEOnFieldX;
	}

	public void setTFEOnFieldX(int tFEOnFieldX) {
		TFEOnFieldX = tFEOnFieldX;
	}

	public int getTFEOnFieldY() {
		return TFEOnFieldY;
	}

	public void setTFEOnFieldY(int tFEOnFieldY) {
		TFEOnFieldY = tFEOnFieldY;
	}

	public int getShipYSize() {
		return shipYSize;
	}

	public void setShipYSize(int shipYSize) {
		this.shipYSize = shipYSize;
	}

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}
}
