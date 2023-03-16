package datyczas;
import java.util.*;

public class Kal1 {

  public static void say(String s) { System.out.println(s+'\n'); }

  public static void main(String[] args) {

    // uzyskanie kalendarza domy�lnego
    // (obowi�zuj�cgo dla domy�lnej lokalizacji - tu dla Polski) 
    // ustawionego na bie��c� dat� i czas

    Calendar cal = Calendar.getInstance();
    cal.set(2003, 4, 6, 18, 5, 0);  

    say("ERA.............. " + cal.get(Calendar.ERA) +   
        " (tu: 0=pne, 1=AD)");

    say("ROK.............. " + cal.get(Calendar.YEAR));
    say("MIESI�C.......... " + cal.get(Calendar.MONTH) +
        " (0-stycze�, 2-luty, ..., 11-grudzie�)");

    say("LICZBA DNI\n" +
        "W MIESI�CU....... " + cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        
    say("DZIE� MIESI�CA... " + cal.get(Calendar.DAY_OF_MONTH));
    say("DZIE� MIESI�CA... " + cal.get(Calendar.DATE));
    say("TYDZIE� ROKU..... " + cal.get(Calendar.WEEK_OF_YEAR));
    say("TYDZIE� MIESI�CA. " + cal.get(Calendar.WEEK_OF_MONTH));
    say("DZIE� W ROKU..... " + cal.get(Calendar.DAY_OF_YEAR));

    say("PIERWSZY DZIE�\n" + 
        "TYGODNIA......... " + cal.getFirstDayOfWeek() +
        " (1-niedziela, 2-poniedzia�ek, ..., 7 sobota)"); 

    say("DZIE� TYGODNIA... " + cal.get(Calendar.DAY_OF_WEEK) +
        " (1-niedziela, 2-poniedzia�ek, ..., 7-sobota)");

    say("GODZINA.......... " + cal.get(Calendar.HOUR) +
        " (12 godzinna skala; nast�pne odwolanie czy AM czy PM)");

    say("AM/PM............ " + cal.get(Calendar.AM_PM) +
        " (AM=0, PM=1)"); 
   
    say("GODZINA.......... " + cal.get(Calendar.HOUR_OF_DAY) +
        " (24 godzinna skala)");

    say("MINUTA........... " + cal.get(Calendar.MINUTE));
    say("SEKUNDA.........  " + cal.get(Calendar.SECOND));
    say("MILISEKUNDA:      " + cal.get(Calendar.MILLISECOND));

    int msh = 3600*1000; // liczba milisekund w godzinie 

    say("RӯNICA CZASU\n" +
        "WOBEC GMT........ " + cal.get(Calendar.ZONE_OFFSET)/msh);

    say("PRZESUNI�CIE\n" + 
        "CZASU............ " + cal.get(Calendar.DST_OFFSET)/msh +
        " (w Polsce obowi�zuje w lecie)");

  }

}
