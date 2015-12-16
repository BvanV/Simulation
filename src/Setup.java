import javax.swing.JFrame;

public class Setup {
	private static final String TITLE 		= "Havensimulatie"; 

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	        	JFrame GUI = new JFrame(TITLE);
	            GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	            MainPanel m = new MainPanel();
	            Controller c = new Controller(m);
	            c.start();
	            GUI.setContentPane(m);
	            GUI.pack();
	            GUI.setVisible(true);
	        }
	    });
    }
}
