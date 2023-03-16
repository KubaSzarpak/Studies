package lokalizacje;
import java.text.*;
import java.util.*;
 
public class DefLok {

  static public void report() {
    Locale defLoc = Locale.getDefault();
    System.out.println("Domyœlna lokalizacja : " + defLoc);
    DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
    NumberFormat nf = NumberFormat.getInstance();
    System.out.println(df.format(new Date()));
    System.out.println(nf.format(1234567.1));
  }

  public static void main(String[] args) {
    report();
    Locale.setDefault(new Locale("en"));
    report();
  } 
 
}
