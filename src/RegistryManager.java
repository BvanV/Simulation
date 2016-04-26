import java.rmi.RemoteException;

public class RegistryManager {

	public static void main(String[] args) throws Exception {
				
    	try {
    		System.out.println("Try to set up Registry Manager");
    		java.rmi.registry.LocateRegistry.createRegistry(1099);
   		} catch (RemoteException e) {
    		System.out.println("Error starting registry, you might have one running already");
   		}
    	while (true) {
    		Thread.sleep(1000);
    	}
    	
//    	Setup setup = new Setup();
//    	Thread simThread = new Thread(setup);
//    	simThread.start();
//    	
//    	ControlGUI control = new ControlGUI();
//    	Thread controlThread = new Thread(control);
//    	controlThread.start();
    	
//    	BSS_Main_Server server = new BSS_Main_Server();
    	
//        Thread serverThread = new Thread(server);
//        serverThread.start();

//        BSS_Main_Client client = new BSS_Main_Client();
             
//        Thread clientThread = new Thread(client);

//        client.setServer(server);

        // wait 1 second
//        Thread.sleep(1000);
        // start client
//        clientThread.start();
    }

	
	public static String formatTimeStamp(int[] ts) {
        if (ts == null) return "null";
		String s = "[ ";
		for(int i=0;i<ts.length;i++) {
			s = s + ts[i] + " ";
		}
		return s + "]";
	}
}
