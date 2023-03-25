import java.util.Date;
import javax.management.*;

public class ClientListener 
   implements NotificationListener {
    
   public void handleNotification(Notification notification, 
                                  Object handback) {
      System.out.println("Odebrano sygna�:");
      System.out.println("Wiadomo��:         " 
                         + notification.getMessage());
      System.out.println("Numer:             " 
                         + notification.getSequenceNumber());
      System.out.println("Wys�ano:           " 
                         + new Date(
                            notification.getTimeStamp()));
      System.out.println("Dane u�ytkownika:  "
                         + notification.getUserData());

      AttributeChangeNotification attrNotif = 
         (AttributeChangeNotification)notification;
      System.out.println("Stara warto��:     " 
                         + attrNotif.getOldValue());
      System.out.println("Nowa warto��:      " 
                         + attrNotif.getNewValue());
   }
}
