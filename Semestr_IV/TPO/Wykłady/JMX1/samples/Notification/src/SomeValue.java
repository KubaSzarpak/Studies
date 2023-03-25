import javax.management.*;

public class SomeValue
   extends NotificationBroadcasterSupport
   implements SomeValueMBean {


   public int getValue() {
      return value;
   }

   public void setValue(int val) {

      Notification notify =             // tworzymy sygna�
         new AttributeChangeNotification(
            this,                       // �r�d�o sygna�u
            sequenceNumber++,           // numer sygna�u
            System.currentTimeMillis(), // czas wys�ania
            "Warto�� zmieniona",        // wiadomo��    
            "Value",                    // nazwa atrybutu
            "int",                      // typ atrybutu
            value,                      // stara warto��
            val                         // nowa warto��              
            );

      this.value = val;  
      sendNotification(notify);         // rozsy�amy sygna�    

      System.out.println("Nowa warto�� atrybutu \"Value\": " 
                         + value);
   }

   public int reset() {
      if (value == 0)
         return 0;
      int oldval = value;
      setValue(0); 
      return oldval;
   }

   /**
    * Numer sygna�u
    */
   private long sequenceNumber = 1;      // dodatkowy pole
    
   private int value = 0; 
}
