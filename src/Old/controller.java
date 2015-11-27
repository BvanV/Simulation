package Old;

class controller {
	gui g;
	
	public controller(gui a) {
		g = a;
		g.setVisible(true);
		g.getComponent(0).setVisible(true);
		g.repaint();
	}
	
	
	public void update() {
		
	}
}