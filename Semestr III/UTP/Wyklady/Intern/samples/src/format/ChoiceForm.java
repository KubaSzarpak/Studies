package format;
import javax.swing.*;
import java.text.*;
 
public class ChoiceForm {
 
  public static void main(String[] args) {
    double[] vals = {  -1, 0, 1, 10 };
    String[] msg  = { "x mniejsze od zera",
                      "0 <= x < 1",
                      "1 <= x < 10",
                      "x >= 10" 
                    };
    ChoiceFormat cf = new ChoiceFormat(vals, msg);
    String in;
    while ((in = JOptionPane.showInputDialog("Podaj x")) != null) {
      double x = Double.parseDouble(in);
      String out = cf.format(x);
      System.out.println(x + " : " + out);
    }
  } 
 
}
