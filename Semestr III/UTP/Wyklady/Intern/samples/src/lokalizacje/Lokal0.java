package lokalizacje;
import java.lang.reflect.*;
import java.util.*;
import java.text.*;
 
public class Lokal0 {
 
  // Lista wszystkich dostêpnych lokalizacji
  // z p.w. klasy Locale
  static List locLoc = Arrays.asList(Locale.getAvailableLocales());

  static void test(String klasa) {
    List avLoc = null;
    try {
      Class c = Class.forName(klasa);
      Class[] argTypes = {}; 
      Method getLocMet =  c.getDeclaredMethod("getAvailableLocales", argTypes);
      Object[] args  = {};
      avLoc = Arrays.asList( (Object[]) getLocMet.invoke(c, args));
    } catch (Exception exc) {
        System.out.println(klasa + "\n" + exc);
        return;
    }
    msg(klasa, avLoc, "Locale", locLoc);
    msg("Locale", locLoc, klasa, avLoc);
  } 
      

  static void msg(String klasa1, List l1,  String klasa2, List l2) {
     String msg = "Dostêpne lokalizacje w klasie " + klasa1;
     if (!l1.containsAll(l2)) msg += " NIE";
     System.out.println(msg + " s¹ takie same jak w klasie " + klasa2);
  }

 
  public static void main(String[] args) {

    test("java.text.NumberFormat");
    test("java.util.Calendar");
    test("java.text.DateFormat");
    test("java.text.Collator");
    test("java.text.BreakIterator");

  } 
 
}
