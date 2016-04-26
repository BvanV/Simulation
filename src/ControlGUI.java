import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JButton;
import javax.swing.JFrame;


public class ControlGUI implements Control_RMI, Runnable, ActionListener, Serializable {
	
	private static final long serialVersionUID = -7780907775812957568L;
	private Registry registry;
	private final JFrame GUI = new JFrame("Control Center");
	
	public ControlGUI() throws RemoteException {
		super();
		registry = null;
		try {
			registry = LocateRegistry.getRegistry("localhost");
			registry.bind("Controller", this);
			System.out.println("Currently bound(Controller):");
			for(String name : registry.list()) {
				System.out.println(" C - "+name);
			}
			GUI.setTitle(GUI.getTitle() + " - Connected");
		} catch (RemoteException e) {
			System.out.println("failed to contact other window");
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			GUI.setTitle(GUI.getTitle() + " - Connection failed");
		}
	}

	private final JButton addShipButton = new JButton("Voeg schip toe");
	
	@Override
	public void run() {
        GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        Controller cont = new Controller();
//        cont.start();
//        GUI.setContentPane(cont.getMp());
        GUI.setSize(getPreferredSize());
        GUI.setVisible(true);
        
        GUI.add(addShipButton);
        addShipButton.addActionListener(this);
	}
	
    public Dimension getPreferredSize() {
        return (new Dimension(500, 150));
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		addShipButton.setText("Button is clicked");
		try {
			Setup_RMI visualizer = (Setup_RMI) registry.lookup("Visualizer");
			System.out.println("Currently bound:");
			for(String name : registry.list()) {
				System.out.println(" C2 - "+name);
			}
			visualizer.contact("Button pressed in the other window!");
		} catch (AccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.out.println("Visualizer does not exist");
		}
	}
	
	public void contactApp() {

		
	}
	
}
