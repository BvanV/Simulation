package game;

public class Man {

	final private int destination;
	final private long since;
	
	public Man(int dest) {
		destination = dest;
		since = System.currentTimeMillis();
	}

	public int getDestination() {
		return destination;
	}

	public long getSince() {
		return since;
	}
	
	
}
