package Old;
import java.awt.Color;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JPanel;


class field extends JPanel {
	final int STANDARD_SIZE = 5;
	row[] rows;
	int size;
	int used = 0;
	
	public field(int s) {
		if(size > 0) {
			rows = new row[s];
			size = s;
		} else {
			rows = new row[STANDARD_SIZE];
			size = STANDARD_SIZE;
		}
		System.out.println("point 1");
		this.setSize(100,100);
		this.setLocation(50, 50);
		this.setBackground(Color.LIGHT_GRAY);
		System.out.println("point 2");
		System.out.println("point 3");
	}
	
	public void addRow() {
		if (used < size) {
			rows[used] = new row();
			used++;
		}
	}
	
	public row getRow(int index) {
		if(index >= 0 && index < used) {
			return rows[index];
		} else {
			System.out.println("Could not return row "+index);
			return null;
		}
	}
}