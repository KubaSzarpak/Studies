package lokalizacje;
import java.util.*;
import java.text.*;
 
public class Lokal1 {
 
  public static void main(String[] args) {

    // Tablica dost�pnych lokalizacji
    
    Locale[] loc = Locale.getAvailableLocales();

    System.out.println("Kod j�zyka" + "#" + 
                       "Kod kraju" + "#" +
                       "Kod wariantu" +  "#" +
                       "J�zyk" + "#" + 
                       "Kraj" +  "#" + "Wariant"
                      );
     
    for (int i=0; i<loc.length; i++) {
      String countryCode = loc[i].getCountry();  // kod kraju
      String langCode = loc[i].getLanguage();    // kod j�zyka
      String varCode  = loc[i].getVariant();     // wariant

      // lokalizacja opisana w j�zyku domy�lnej lokalizacji (polskim)
      String kraj =  loc[i].getDisplayCountry();
      String jezyk = loc[i].getDisplayLanguage();
      String wariant = loc[i].getDisplayVariant();
      System.out.println(langCode + "#" + 
                         countryCode +  "#" +
                         varCode + "#" +
                         jezyk + "#" + kraj + "#" + wariant);
    
     }
  } 
 
}
