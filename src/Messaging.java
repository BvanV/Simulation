import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Messaging extends Remote {

	void sendMessage(String s) throws RemoteException;
	
}
