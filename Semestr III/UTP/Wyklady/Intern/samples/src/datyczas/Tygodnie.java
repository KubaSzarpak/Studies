package datyczas;
import java.util.*;
 
public class Tygodnie  {
   
  public static void main(String[] args) {
   Calendar calendar = Calendar.getInstance();

 System.out.println("Pi¹tki: ");
 printWeeks(15, 2003, 2, 28);
 System.out.println("Wtorki: ");
 printWeeks(15, 2003, 3, 4);


 } 
 static Calendar[] swieta = { new GregorianCalendar(2003, 4-1, 18),
                          new GregorianCalendar(2003, 4-1, 19),
                          new GregorianCalendar(2003, 4-1, 20),
                          new GregorianCalendar(2003, 4-1, 21),
                          new GregorianCalendar(2003, 4-1, 22),
                          new GregorianCalendar(2003, 5-1, 1),
                          new GregorianCalendar(2003, 5-1, 2),
                          new GregorianCalendar(2003, 5-1, 3),
                          new GregorianCalendar(2003, 6-1, 19),
                        };


  static void printWeeks(int n, int rok, int mies, int dzien) {

    int indSwiat = 0; 
    Calendar c = new GregorianCalendar(rok, mies-1, dzien);
    nextWeek:
    for (int i = 1; i <= n; i++) {
       for (int j=0; j< swieta.length; j++)
          if (c.equals(swieta[j])) {
            c.add(Calendar.DAY_OF_MONTH, 7);
            i--;
            continue nextWeek;
          }
       System.out.println(i + " : " + c.getTime());
       c.add(Calendar.DAY_OF_MONTH, 7);
    }
  } 
         

 
}
