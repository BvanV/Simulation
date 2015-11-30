import javax.swing.JFrame;

public class Controller {
	private static final String TITLE 		= "Havensimulatie"; 

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	        	JFrame GUI = new JFrame(TITLE);
	            GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	            GUI.setContentPane(new MainPanel());
	            GUI.pack();
	            GUI.setVisible(true);
	        }
	    });
    }
}
