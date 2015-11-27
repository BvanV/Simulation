import javax.swing.JFrame;

public class Controller {
	private static final String TITLE 		= "Havensimulatie"; 
	private static final int SCREEN_WIDTH 	= 1800;
	private static final int SCREEN_HEIGHT 	= 1000;	

    public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	        	JFrame GUI = new JFrame(TITLE);
	            GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	            GUI.setContentPane(new MainPanel(SCREEN_WIDTH, SCREEN_HEIGHT));
	            GUI.pack();
	            GUI.setVisible(true);
	        }
	    });
    }
}
