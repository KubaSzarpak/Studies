public class SomeValue
   implements SomeValueMBean {


   public int getValue() {
      System.out.println(
         "Wywo³ano metodê getValue()," + 
         " która zwróci³a wynik: " + 
         value
         );
      return value;
   }

   public void setValue(int val) {
      this.value = val;  
      System.out.println(
         "Wywo³ano metodê setValue()" + 
         " z argumentem: " + 
         val
         );
   }

   /**
    * Atrybut, który udostêpniamy 
    */
   private int value = 0; 
}
