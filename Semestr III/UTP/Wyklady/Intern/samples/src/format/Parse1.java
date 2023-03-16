package format;
import javax.swing.*;
import java.text.*;
 
public class Parse1 {
 
  public static void main(String[] args) {
 //   NumberFormat format = NumberFormat.getInstance();
 //   NumberFormat format = NumberFormat.getIntegerInstance();
 //    NumberFormat format = NumberFormat.getCurrencyInstance();
    NumberFormat format = new DecimalFormat("[ #.0000 ]");
    System.out.println(format.format(23));
 //   NumberFormat format = NumberFormat.getPercentInstance();
    String in, 
           msg = "Podaj liczbê";
    Number num = null;
    while ((in = JOptionPane.showInputDialog(msg)) != null) {
      System.out.println("Wejscie: " + in);
      try {
        num = format.parse(in);
      } catch (ParseException exc) {
          System.out.println("Wadliwe dane: " + in);
          System.out.println(exc);
          System.out.println("Wadliwa pozycja: " + exc.getErrorOffset()); 
          continue;
      }  
      System.out.println("Parse daje: " + 
                          num.getClass().getName()+ " = " + num);
    }  
    System.exit(0);
  } 
 
}
