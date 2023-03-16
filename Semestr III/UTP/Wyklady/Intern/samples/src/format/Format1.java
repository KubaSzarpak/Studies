package format;
import javax.swing.*;
import java.text.*;
import java.math.*;
import java.util.Locale;
 
public class Format1 {

  public static void show(double n1, Double n2, BigDecimal n3,
                          String format) {
    DecimalFormat df = new DecimalFormat(format);
    DecimalFormatSymbols sym = df.getDecimalFormatSymbols();
    sym.setDecimalSeparator('.');
    System.out.println("Format " + format);
    System.out.println("Liczba: " +  n1 + " wygl퉐a tak: " + df.format(n1));
    System.out.println("Liczba: " +  n2 + " wygl퉐a tak: " + df.format(n2));
    System.out.println("Liczba: " +  n3 + " wygl퉐a tak: " + df.format(n3));
  }
  
 
  public static void main(String[] args) {
    double num1  = 1.346;
    Double num2  = new Double(0.765474);
    BigDecimal num3 = new BigDecimal("100.2189091");

    show(num1, num2, num3, "#.##");
    show(num1, num2, num3, "#.## %");
    show(num1, num2, num3, "#.0000");
    show(num1, num2, num3, "#.00 �");
    show(num1, num2, num3, "#.00 ㄴ");
    show(num1, num2, num3, "[ 000.0 ]");

    /*String format; 
    while ((format = JOptionPane.showInputDialog("Podaj format")) != null) {
      DecimalFormat df = new DecimalFormat(format);
      String in;
      while ((in = JOptionPane.showInputDialog(format + " -liczba?")) != null) { 
        double num = Double.parseDouble(in);
        String out = df.format(num);
        System.out.println("Liczba: " + num +
                           " w formacie: " + format +  
                           " wygl퉐a tak: " + out);
      }
    } */
 }   
 
}
