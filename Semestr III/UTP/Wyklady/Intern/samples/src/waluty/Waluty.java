package waluty;
import java.util.*;
//import com.ibm.icu.util.Currency;

 
public class Waluty {
 
  public static void main(String[] args) {
    // Domy�lna lokalizacja (w naszym przypadku polska)
    Locale def = Locale.getDefault();

    // Tablica dost�pnych lokalizacji
    Locale[] loc = Locale.getAvailableLocales();

    char[] zera = { '0', '0', '0', '0', '0', '0' }; // do pokazu walut

    for (int i=0; i<loc.length; i++) {
      String countryCode = loc[i].getCountry();  // kod kraju
      String langCode = loc[i].getLanguage();    // kod j�zyka
      if (countryCode.equals("")) continue;

      // lokalizacja opisana w j�zyku domy�lnej lokalizacji (polskim)
      String kraj =  loc[i].getDisplayCountry(def);
      String jezyk = loc[i].getDisplayLanguage(def);
      
//      boolean[] b  = { false };

      // Waluta dla lokalizacji loc[i]
      Currency c = Currency.getInstance(loc[i]);
      String sym  =  c.getSymbol();      // symbol w domy�lnej lokalizacji (pl) 
      String nsym = c.getSymbol(loc[i]); // symbol w danym kraju
      String icode = c.getCurrencyCode();     // mi�dzynarod. kod waluty 
      int cdig = c.getDefaultFractionDigits(); // ile mo�e by� miejsc dzies.
      
      System.out.println(loc[i]+" kraj: "+kraj+" j�zyk: "+jezyk + 
        "\nwaluta: "+sym+" "+nsym+" "+icode + 
                   " grosz = 1/1"+new String(zera,0,cdig)+" "+sym +
        "\n============================================================"
        );
     }
  } 
 
}
