package Old;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;



public class block extends JPanel implements ActionListener {
	private Timer t;
	private ArrayList<Double> x;
	private ArrayList<Double> y;
	private ArrayList<Double> velX;
	private ArrayList<Double> velY;
	private ArrayList<Rectangle2D> blocks;
	
	public block(double startX, double startY, double speedX, double speedY) {
		t 		= new Timer(5, this);
		x 		= new ArrayList<Double>();
		y 		= new ArrayList<Double>();
		velX 	= new ArrayList<Double>();
		velY 	= new ArrayList<Double>();
		blocks 	= new ArrayList<Rectangle2D>();
		x.add(startX);
		y.add(startY);
		velX.add(speedX);
		velY.add(speedY);
	}
	
	public void addBlock(double startX, double startY, double speedX, double speedY) {
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		for(int i=0; i<blocks.size();i++) {
			Rectangle2D rect = new Rectangle2D.Double(x.get(i), y.get(i), 50, 20);
			g2.fill(rect);			
		}
		t.start();
	}
	
	public void actionPerformed(ActionEvent e) {
//		for(int i=0;i<blocks.size();i++){
//			if(x.get(i) < 1750) {
//				x.set(i, x.get(i) + velX.get(i));
//			}
//		}
		repaint();
	}
}
