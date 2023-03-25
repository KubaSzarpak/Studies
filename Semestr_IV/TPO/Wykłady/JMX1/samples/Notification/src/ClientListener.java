import java.util.Date;
import javax.management.*;

public class ClientListener 
   implements NotificationListener {
    
   public void handleNotification(Notification notification, 
                                  Object handback) {
      System.out.println("Odebrano sygna³:");
      System.out.println("Wiadomo¶æ:         " 
                         + notification.getMessage());
      System.out.println("Numer:             " 
                         + notification.getSequenceNumber());
      System.out.println("Wys³ano:           " 
                         + new Date(
                            notification.getTimeStamp()));
      System.out.println("Dane u¿ytkownika:  "
                         + notification.getUserData());

      AttributeChangeNotification attrNotif = 
         (AttributeChangeNotification)notification;
      System.out.println("Stara warto¶æ:     " 
                         + attrNotif.getOldValue());
      System.out.println("Nowa warto¶æ:      " 
                         + attrNotif.getNewValue());
   }
}
