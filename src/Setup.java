import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JFrame;

public class Setup implements Setup_RMI, Runnable, Serializable {

	private static final long serialVersionUID = -4124987945400854254L;
	private final JFrame GUI = new JFrame(TITLE);
	private final Controller cont = new Controller();
	private String logText = "<>";
	
	public Setup() throws RemoteException {
		super();
	}

	@Override
	public synchronized void contact(String msg) {
		System.out.println("Remote msg called! msg: "+msg);
		cont.getMp().addNewLogText(msg);
		System.out.println("Tried to add msg to log");
		logText = msg;
	}
	
	private static final String TITLE 		= "Havensimulatie"; 
	final static int SCREEN_WIDTH 			= 1800;
	final static int SCREEN_HEIGHT 			= 1000;		

	@Override
	public void run() {
        GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        cont.init();
        GUI.setContentPane(cont.getMp());
        GUI.pack();
        GUI.setVisible(true);	
		try {
			Registry registry = LocateRegistry.getRegistry("localhost");
			registry.bind("Visualizer", this);
			System.out.println("Currently bound:");
			for(String name : registry.list()) {
				System.out.println(" V - "+name);
			}
			GUI.setTitle(GUI.getTitle() + " - Connected");
		} catch (RemoteException e) {
			System.out.println("failed to contact other window");
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			System.out.println("There exists already a visualizer");
		}
		while(true) {
			cont.runAgain();
			System.out.println("contact: " + logText);
	        try {
	            Thread.sleep(1000);
	        } catch (InterruptedException e) { }
		}
	
	}

}
