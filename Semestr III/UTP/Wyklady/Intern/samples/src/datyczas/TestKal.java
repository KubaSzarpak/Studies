package datyczas;
import java.util.*; 
import javax.swing.*;

public class TestKal {
 
  public static void main(String[] args) {
    String in;
    int d = 0;
    while ((in = JOptionPane.showInputDialog("DATE:")) != null) {
      d = Integer.parseInt(in);
      show("set",  "DATE", Calendar.DATE, d);
      show("add",  "DATE", Calendar.DATE, d);
      show("roll", "DATE", Calendar.DATE, d);
    }
    System.exit(0);
  } 

  static void say(String s) { System.out.println(s); } 


  static void show(String oper, String what, int field, int value) {
    Calendar c = Calendar.getInstance();
    say("Teraz jest: " + c.getTime());
    say("Operacja: " + oper + "(Calendar." + what + ", " + value + ")");
    if (oper.equals("set")) c.set(field, value);
    else if (oper.equals("add")) c.add(field, value);
         else if (oper.equals("roll")) c.roll(field,value);
    
    say("Aktualne ustawienia kalendarza: " + c.get(Calendar.YEAR) + '/'
                               + (c.get(Calendar.MONTH) + 1) + '/' +
                               + c.get(Calendar.DATE));
     say("-----------------------------------------------------------");
  } 
 
}
