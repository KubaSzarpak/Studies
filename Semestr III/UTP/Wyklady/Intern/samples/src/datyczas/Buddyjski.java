package datyczas;
import java.util.*; 
public class Buddyjski {
 
  public static void main(String[] args) {
    // Domyœlna lokalizacja (w naszym przypadku polska
    Locale def = Locale.getDefault();

    // Tablica dostêpnych lokalizacji
    Locale[] loc = Calendar.getAvailableLocales();

    for (int i=0; i<loc.length; i++) {
      String langCode = loc[i].getLanguage();    // kod jêzyka
      String countryCode = loc[i].getCountry();  // kod kraju
      if (countryCode.equals("")) continue;

      // lokalizacja opisana w jêzyku domyœlnej lokalizacji (polskim)
      String dName = loc[i].getDisplayName(def);  

      // Uzyskanie kalendarza dla danej lokalizacji
      Calendar c = Calendar.getInstance(loc[i]);
      
      // Jaki tam jest teraz rok i jaki jest indeks pierwszego dnia tygodnia
      System.out.println(langCode + "_" + countryCode + " czyli " + dName + 
                         " rok: " + c.get(Calendar.YEAR) +  
                         " pierwszy dzieñ tyg.: " + c.getFirstDayOfWeek()  
                         );
    }

  } 


 
}
