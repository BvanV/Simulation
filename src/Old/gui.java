package Old;
import java.awt.Color;

import javax.swing.JFrame;



class gui extends JFrame {
	private field[] fields;
	private int size;
	private int used = 0; 
	
	public gui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1800, 800);
		setLocation(100,100);
		setBackground(Color.BLUE);
//		fields = new field[3];
//		size = 3;
	}
	
	public void addField() {
		if(used >= size) {
			System.out.println("No more space to add a field!");
		} else {
			field f = new field(3);
			fields[used] = f;
			used++;
			this.add(f);
			f.setVisible(true);
		}
		
	}
	
	public field getField(int index) {
		if(index >= 0 && index < used) {
			return fields[index];
		} else {
			System.out.println("No field with index "+ index);
			return null;
		}
	}
}