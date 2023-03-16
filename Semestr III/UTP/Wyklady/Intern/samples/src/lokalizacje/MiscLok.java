package lokalizacje;
import java.text.*;
import java.util.*;
 
public class MiscLok {

  public static void main(String[] args) {
    System.out.println("Domyœlna lokalizacja : " + Locale.getDefault());
    DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
    System.out.println(df.format(new Date()));
    double num = 123.4;
    NumberFormat nf = NumberFormat.getInstance();
    System.out.println("Liczba " + num +
                       " w lokalizacji domyœlnej: " + nf.format(num));
    nf = NumberFormat.getInstance(new Locale("en"));
    System.out.println("Liczba " + num +
                       " w lokalizacji angielskiej: " + nf.format(num));

  } 
 
}
