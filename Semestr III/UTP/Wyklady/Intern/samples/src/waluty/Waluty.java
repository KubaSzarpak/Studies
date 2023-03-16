package waluty;
import java.util.*;
//import com.ibm.icu.util.Currency;

 
public class Waluty {
 
  public static void main(String[] args) {
    // Domyœlna lokalizacja (w naszym przypadku polska)
    Locale def = Locale.getDefault();

    // Tablica dostêpnych lokalizacji
    Locale[] loc = Locale.getAvailableLocales();

    char[] zera = { '0', '0', '0', '0', '0', '0' }; // do pokazu walut

    for (int i=0; i<loc.length; i++) {
      String countryCode = loc[i].getCountry();  // kod kraju
      String langCode = loc[i].getLanguage();    // kod jêzyka
      if (countryCode.equals("")) continue;

      // lokalizacja opisana w jêzyku domyœlnej lokalizacji (polskim)
      String kraj =  loc[i].getDisplayCountry(def);
      String jezyk = loc[i].getDisplayLanguage(def);
      
//      boolean[] b  = { false };

      // Waluta dla lokalizacji loc[i]
      Currency c = Currency.getInstance(loc[i]);
      String sym  =  c.getSymbol();      // symbol w domyœlnej lokalizacji (pl) 
      String nsym = c.getSymbol(loc[i]); // symbol w danym kraju
      String icode = c.getCurrencyCode();     // miêdzynarod. kod waluty 
      int cdig = c.getDefaultFractionDigits(); // ile mo¿e byæ miejsc dzies.
      
      System.out.println(loc[i]+" kraj: "+kraj+" jêzyk: "+jezyk + 
        "\nwaluta: "+sym+" "+nsym+" "+icode + 
                   " grosz = 1/1"+new String(zera,0,cdig)+" "+sym +
        "\n============================================================"
        );
     }
  } 
 
}
