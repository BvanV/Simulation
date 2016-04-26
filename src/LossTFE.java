
public class LossTFE {
	int xPos;
	int yPos;
	LossTFE next;
	
	public LossTFE(int x, int y) {
		xPos = x;
		yPos = y;
	}

	public int getxPos() {
		return xPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

	public LossTFE getNext() {
		return next;
	}

	public void setNext(LossTFE next) {
		this.next = next;
	}
}
