public class SomeValue
   implements SomeValueMBean {


   public int getValue() {
      System.out.println(
         "Wywo�ano metod� getValue()," + 
         " kt�ra zwr�ci�a wynik: " + 
         value
         );
      return value;
   }

   public void setValue(int val) {
      this.value = val;  
      System.out.println(
         "Wywo�ano metod� setValue()" + 
         " z argumentem: " + 
         val
         );
   }

   /**
    * Atrybut, kt�ry udost�pniamy 
    */
   private int value = 0; 
}
