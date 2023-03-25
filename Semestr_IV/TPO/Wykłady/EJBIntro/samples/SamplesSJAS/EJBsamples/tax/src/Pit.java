package pit;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.math.*;

public interface Pit extends EJBObject {
    public BigDecimal taxToPay(BigDecimal income) throws RemoteException;
}
