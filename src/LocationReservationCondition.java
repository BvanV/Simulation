
public class LocationReservationCondition extends Condition {
	private int reservedLocation;
	private int shipIndex;
	
	public LocationReservationCondition(int loc, int s) {
		super();
		reservedLocation = loc;
		shipIndex = s;
	}

	public int getReservedLocation() {
		return reservedLocation;
	}

	public void setReservedLocation(int reservedLocation) {
		this.reservedLocation = reservedLocation;
	}

	public int getShipIndex() {
		return shipIndex;
	}

	public void setShipIndex(int shipIndex) {
		this.shipIndex = shipIndex;
	}
	
}
