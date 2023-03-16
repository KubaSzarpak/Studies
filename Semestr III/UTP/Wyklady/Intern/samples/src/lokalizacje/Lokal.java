package lokalizacje;
import java.util.*;
import java.text.*;
 
public class Lokal {
 
  public static void main(String[] args) {
    // Domyœlna lokalizacja (w naszym przypadku polska)
    Locale def = Locale.getDefault();

    // Tablica dostêpnych lokalizacji
    Locale[] loc = Locale.getAvailableLocales();

    char[] zera = { '0', '0', '0', '0', '0', '0' }; // do pokazu walut
    // dni tygodnia po polsku
    String[] dtp = new DateFormatSymbols(def).getWeekdays();

    for (int i=0; i<loc.length; i++) {
      String countryCode = loc[i].getCountry();  // kod kraju
      String langCode = loc[i].getLanguage();    // kod jêzyka
      if (countryCode.equals("")) continue;

      // lokalizacja opisana w jêzyku domyœlnej lokalizacji (polskim)
      String kraj =  loc[i].getDisplayCountry(def);
      String jezyk = loc[i].getDisplayLanguage(def);
      
      // Symbole dla wyprowadzania liczb, walut itp.
      DecimalFormatSymbols dfs = new DecimalFormatSymbols(loc[i]);
      
      // Separatory
      char decsep = dfs.getDecimalSeparator();  // miejsc dziesiêtnych
      char thsep  = dfs.getGroupingSeparator(); // tysiêcy
      
      // Waluta
      Currency c = dfs.getCurrency();
      String sym = c.getSymbol();        // symbol w domyœlnej lokalizacji (pl) 
      String nsym = c.getSymbol(loc[i]); // symbol w danym kraju
      String icode = c.getCurrencyCode();     // miêdzynarod.kod waluty 
      int cdig = c.getDefaultFractionDigits(); // ile mo¿e byæ miejsc dzies.
      
      // Kalendarz
      Calendar cal = Calendar.getInstance(loc[i]); // kalendarz dla lokalizacji
      int year = cal.get(Calendar.YEAR);           // bie¿¹cy rok
      int fdow = cal.getFirstDayOfWeek();          // pierwszy dziñ tygodnia 
      
      // Symbole u¿ywane przy formatowaniu dat
      DateFormatSymbols dafs = new DateFormatSymbols(loc[i]);
      String[] wd = dafs.getWeekdays();  // dni tygodnia wg danej lokalizacji
      String dniTyg = "";
      for (int k=0; k<wd.length; k++) dniTyg += wd[k] + " ";

      // Info
      System.out.println(loc[i]+" kraj: "+kraj+" jêzyk: "+jezyk + 
        "\nseparatory [ miejsc dzies.: '"+decsep+"' tysiêcy '"+thsep+"' ]" +
        "\nwaluta: "+sym+" "+nsym+" "+icode+" grosz=1/1"+new String(zera,0,cdig)+" "+sym +
        "\nrok: "+year + 
        "\ndni: " +dniTyg + 
        "\npierwszy dzieñ tygodnia: "+fdow+" czyli "+ wd[fdow] + " - " + dtp[fdow] +
        "\n============================================================"
        );
     }
  } 
 
}
