import java.rmi.*;
import javax.rmi.*;

// Implementacja

import java.util.*;


public class AddressInfo extends PortableRemoteObject 
                         implements AddressInfoInterface {

   private Map<String, String> map = new HashMap<String, String>();

   public AddressInfo()  throws RemoteException {
     super();  
     map.put("Kowalski Jan", "Ogrodowa 6");

   } 

   public String getAddress(String name) throws RemoteException {
     return map.get(name);
   }
}
