package datyczas;
import com.ibm.icu.util.*;
import com.ibm.icu.text.*;
 
public class MiscCal {
 
  public static void main(String[] args) {
    Calendar[] kal = {
               Calendar.getInstance(),   // domy�lny kalendarz - gregoria�ski
               new GregorianCalendar(),  // jeszcze raz - ale inaczej tworzony
               new BuddhistCalendar(),   // buddyjski
               new ChineseCalendar(),    // chi�ski
               new JapaneseCalendar(),   // japo�ski
               new IslamicCalendar(),    // islamski
               new HebrewCalendar(),     // hebrajski
               };
    java.util.Date teraz = new java.util.Date(); // aktualny czas
    System.out.println("Teraz jest: " + teraz);  // po angielsku 

    // przebiegamy po klaendarzach
    // ustawiamy je na bie��cy czas
    // i pokazujemy warto�ci takich p�l jak rok, miesi�c itp.

    for (int i=0; i<kal.length; i++) { 
      kal[i].setTime(teraz);
      String className = kal[i].getClass().getName();
      String name = className.substring(className.lastIndexOf(".") + 1);
      System.out.println(name + " - " +
            "era " + kal[i].get(Calendar.ERA) + 
            "; rok " + kal[i].get(Calendar.YEAR) + 
            (name.equals("ChineseCalendar") ? 
                  " czyli " + kal[i].get(Calendar.EXTENDED_YEAR)  : "") + 
            "; mies " + kal[i].get(Calendar.MONTH) + 
            "; dzie� mies. " + kal[i].get(Calendar.DAY_OF_MONTH) +
            "; dzie� tyg. " + kal[i].get(Calendar.DAY_OF_WEEK)
            );
    }  
  }

}