package datyczas;
import com.ibm.icu.util.Calendar;
import java.util.Locale; 
import java.util.Map;
import java.util.TreeMap;
import java.util.Iterator;
import java.text.DateFormatSymbols;

public class FirstDoW {
 
  public static void main(String[] args) {
    // Domyœlna lokalizacja (w naszym przypadku polska
    Locale def = Locale.getDefault();

    // Chcemy mieæ dni tygodnia po polsku
    DateFormatSymbols dafs = new DateFormatSymbols(def);
    String[] wdays = dafs.getWeekdays();  // dni tygodnia po polsku


    // Tablica dostêpnych lokalizacji (z pakietu ICU!!!)
    Locale[] loc = Calendar.getAvailableLocales();

    // Mapa: klucz = kraj, wartoœæ = piewrwszy dzieñ tygodnia
    Map fdowMap = new TreeMap();
 
    for (int i=0; i<loc.length; i++) {

      // Uzyskanie kalendarza dla danej lokalizacji 
      // (bêd¹ to kalendzrae gregoriañskie, bo taka jest w³aœciwoœæ pakietu ICU)
      Calendar c = Calendar.getInstance(loc[i]);

      // indeks pierwszego dnia tygodnia w tym kalendarzu
      int fdow = c.getFirstDayOfWeek();
      // je¿eli to poniedzia³ek - nie interesuej nas
      if (fdow == 2) continue;

      // lokalizacja opisana w jêzyku domyœlnej lokalizacji (polskim)
      String country = loc[i].getDisplayCountry(def);
      if (country.equals("")) continue; // pomijamy te bez kraju

      if (!fdowMap.containsKey(country)) fdowMap.put(country, wdays[fdow]);  
    }

    System.out.println("Gdzie pierwszy dzieñ tygodnia nie jest poniedzia³kiem?");
    for (Iterator it = fdowMap.keySet().iterator(); it.hasNext(); ) {
      // Jaki jest pierwszy dzieñ tygodnia
      String country = (String) it.next();
      String fday = (String) fdowMap.get(country);
      System.out.println(country  +  " = " + fday);
    }
  } 

}
