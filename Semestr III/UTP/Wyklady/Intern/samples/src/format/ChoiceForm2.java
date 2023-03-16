package format;
import javax.swing.*;
import java.text.*;
 
public class ChoiceForm2 {
 
  public static void main(String[] args) {
    String pattern = 
     "0#brak jablek |0<niecala polowa jablka |"+
     "0.5#rowno pol jablka | 0.5<ponad polowa jablka |" +
     "1#jedno jablko |1<niecale dwa jablka |" + 
     "2#dwa jablka |2<wiecej ni¿ dwa jablka";

    ChoiceFormat cf = new ChoiceFormat(pattern);
    String in;
    while ((in = JOptionPane.showInputDialog("Ile jest jab³ek?")) != null) {
      double x = Double.parseDouble(in);
      String out = cf.format(x);
      System.out.println("Podano: " + x + " Wynik: " +  out);
    }
  } 
 
}
