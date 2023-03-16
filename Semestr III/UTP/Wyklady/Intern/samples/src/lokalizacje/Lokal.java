package lokalizacje;
import java.util.*;
import java.text.*;
 
public class Lokal {
 
  public static void main(String[] args) {
    // Domy�lna lokalizacja (w naszym przypadku polska)
    Locale def = Locale.getDefault();

    // Tablica dost�pnych lokalizacji
    Locale[] loc = Locale.getAvailableLocales();

    char[] zera = { '0', '0', '0', '0', '0', '0' }; // do pokazu walut
    // dni tygodnia po polsku
    String[] dtp = new DateFormatSymbols(def).getWeekdays();

    for (int i=0; i<loc.length; i++) {
      String countryCode = loc[i].getCountry();  // kod kraju
      String langCode = loc[i].getLanguage();    // kod j�zyka
      if (countryCode.equals("")) continue;

      // lokalizacja opisana w j�zyku domy�lnej lokalizacji (polskim)
      String kraj =  loc[i].getDisplayCountry(def);
      String jezyk = loc[i].getDisplayLanguage(def);
      
      // Symbole dla wyprowadzania liczb, walut itp.
      DecimalFormatSymbols dfs = new DecimalFormatSymbols(loc[i]);
      
      // Separatory
      char decsep = dfs.getDecimalSeparator();  // miejsc dziesi�tnych
      char thsep  = dfs.getGroupingSeparator(); // tysi�cy
      
      // Waluta
      Currency c = dfs.getCurrency();
      String sym = c.getSymbol();        // symbol w domy�lnej lokalizacji (pl) 
      String nsym = c.getSymbol(loc[i]); // symbol w danym kraju
      String icode = c.getCurrencyCode();     // mi�dzynarod.kod waluty 
      int cdig = c.getDefaultFractionDigits(); // ile mo�e by� miejsc dzies.
      
      // Kalendarz
      Calendar cal = Calendar.getInstance(loc[i]); // kalendarz dla lokalizacji
      int year = cal.get(Calendar.YEAR);           // bie��cy rok
      int fdow = cal.getFirstDayOfWeek();          // pierwszy dzi� tygodnia 
      
      // Symbole u�ywane przy formatowaniu dat
      DateFormatSymbols dafs = new DateFormatSymbols(loc[i]);
      String[] wd = dafs.getWeekdays();  // dni tygodnia wg danej lokalizacji
      String dniTyg = "";
      for (int k=0; k<wd.length; k++) dniTyg += wd[k] + " ";

      // Info
      System.out.println(loc[i]+" kraj: "+kraj+" j�zyk: "+jezyk + 
        "\nseparatory [ miejsc dzies.: '"+decsep+"' tysi�cy '"+thsep+"' ]" +
        "\nwaluta: "+sym+" "+nsym+" "+icode+" grosz=1/1"+new String(zera,0,cdig)+" "+sym +
        "\nrok: "+year + 
        "\ndni: " +dniTyg + 
        "\npierwszy dzie� tygodnia: "+fdow+" czyli "+ wd[fdow] + " - " + dtp[fdow] +
        "\n============================================================"
        );
     }
  } 
 
}
