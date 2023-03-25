import java.rmi.*;
import javax.rmi.*;

// Interfejs

public interface AddressInfoInterface extends Remote {

   public String getAddress(String name) throws RemoteException;

}
