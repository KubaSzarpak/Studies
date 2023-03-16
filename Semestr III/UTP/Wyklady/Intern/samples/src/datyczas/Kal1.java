package datyczas;
import java.util.*;

public class Kal1 {

  public static void say(String s) { System.out.println(s+'\n'); }

  public static void main(String[] args) {

    // uzyskanie kalendarza domyœlnego
    // (obowi¹zuj¹cgo dla domyœlnej lokalizacji - tu dla Polski) 
    // ustawionego na bie¿¹c¹ datê i czas

    Calendar cal = Calendar.getInstance();
    cal.set(2003, 4, 6, 18, 5, 0);  

    say("ERA.............. " + cal.get(Calendar.ERA) +   
        " (tu: 0=pne, 1=AD)");

    say("ROK.............. " + cal.get(Calendar.YEAR));
    say("MIESI¥C.......... " + cal.get(Calendar.MONTH) +
        " (0-styczeñ, 2-luty, ..., 11-grudzieñ)");

    say("LICZBA DNI\n" +
        "W MIESI¥CU....... " + cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        
    say("DZIEÑ MIESI¥CA... " + cal.get(Calendar.DAY_OF_MONTH));
    say("DZIEÑ MIESI¥CA... " + cal.get(Calendar.DATE));
    say("TYDZIEÑ ROKU..... " + cal.get(Calendar.WEEK_OF_YEAR));
    say("TYDZIEÑ MIESI¥CA. " + cal.get(Calendar.WEEK_OF_MONTH));
    say("DZIEÑ W ROKU..... " + cal.get(Calendar.DAY_OF_YEAR));

    say("PIERWSZY DZIEÑ\n" + 
        "TYGODNIA......... " + cal.getFirstDayOfWeek() +
        " (1-niedziela, 2-poniedzia³ek, ..., 7 sobota)"); 

    say("DZIEÑ TYGODNIA... " + cal.get(Calendar.DAY_OF_WEEK) +
        " (1-niedziela, 2-poniedzia³ek, ..., 7-sobota)");

    say("GODZINA.......... " + cal.get(Calendar.HOUR) +
        " (12 godzinna skala; nastêpne odwolanie czy AM czy PM)");

    say("AM/PM............ " + cal.get(Calendar.AM_PM) +
        " (AM=0, PM=1)"); 
   
    say("GODZINA.......... " + cal.get(Calendar.HOUR_OF_DAY) +
        " (24 godzinna skala)");

    say("MINUTA........... " + cal.get(Calendar.MINUTE));
    say("SEKUNDA.........  " + cal.get(Calendar.SECOND));
    say("MILISEKUNDA:      " + cal.get(Calendar.MILLISECOND));

    int msh = 3600*1000; // liczba milisekund w godzinie 

    say("RÓ¯NICA CZASU\n" +
        "WOBEC GMT........ " + cal.get(Calendar.ZONE_OFFSET)/msh);

    say("PRZESUNIÊCIE\n" + 
        "CZASU............ " + cal.get(Calendar.DST_OFFSET)/msh +
        " (w Polsce obowi¹zuje w lecie)");

  }

}
