import javax.management.*;

public class SomeValue
   extends NotificationBroadcasterSupport
   implements SomeValueMBean {


   public int getValue() {
      return value;
   }

   public void setValue(int val) {

      Notification notify =             // tworzymy sygna³
         new AttributeChangeNotification(
            this,                       // ¼ród³o sygna³u
            sequenceNumber++,           // numer sygna³u
            System.currentTimeMillis(), // czas wys³ania
            "Warto¶æ zmieniona",        // wiadomo¶æ    
            "Value",                    // nazwa atrybutu
            "int",                      // typ atrybutu
            value,                      // stara warto¶æ
            val                         // nowa warto¶æ              
            );

      this.value = val;  
      sendNotification(notify);         // rozsy³amy sygna³    

      System.out.println("Nowa warto¶æ atrybutu \"Value\": " 
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
    * Numer sygna³u
    */
   private long sequenceNumber = 1;      // dodatkowy pole
    
   private int value = 0; 
}
