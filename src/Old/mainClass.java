package Old;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;



class mainClass {	
	public static void main(String[] args) {
		block b = new block(0,10,2,0);
		JFrame f = new JFrame();
		f.add(b);
		f.setVisible(true);
		f.setSize(1800, 1000);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setTitle("Container Simulatie");
	}
}