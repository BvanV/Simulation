
public class PositionCondition extends Condition {
	private int destX;
	private Ship ship;

	public PositionCondition() {
		super();
		
	}
	
	@Override
	public boolean isMet() {
		if(ship == null) {
			return false;
		}
		return destX == ship.getX();
	}
}
