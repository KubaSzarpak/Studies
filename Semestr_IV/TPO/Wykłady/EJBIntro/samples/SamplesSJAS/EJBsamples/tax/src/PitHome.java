package pit;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface PitHome extends EJBHome {
    Pit create() throws RemoteException, CreateException;
}
