
public class Condition {
	private int status;
	private final static int CD_NOT_STARTED	= 0;
	private final static int CD_IN_PROGRESS	= 1;

	public Condition() {
		
	}
	
	public boolean isMet() {
		return false;
	}
}
