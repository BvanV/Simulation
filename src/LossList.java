
public class LossList {
	Ship s;
	LossTFE first;
	
	public LossList(Ship s) {
		this.s = s;
		first = null;
	}


	public Ship getS() {
		return s;
	}


	public void setS(Ship s) {
		this.s = s;
	}


	public LossTFE getFirst() {
		return first;
	}


	public void setFirst(LossTFE first) {
		this.first = first;
	}
	
	public void addTFEToLoss(int x, int y) {
		LossTFE temp = first;
		if(first == null) {
			first = new LossTFE(x, y);
		} else {
			LossTFE next = temp.getNext();
			while(next != null) {
				temp = next;
				next = temp.getNext();
			}
			temp.setNext(new LossTFE(x, y));
		}

	}
	
	
}
