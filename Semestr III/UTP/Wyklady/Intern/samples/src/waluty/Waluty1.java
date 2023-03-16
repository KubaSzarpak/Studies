package waluty;
import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import com.ibm.icu.util.Currency;

 
public class Waluty1 {
 
  public static void main(String[] args) {

    Locale def = Locale.getDefault();
    Locale en =  new Locale("en", "US");


    // Tablica dostêpnych lokalizacji
    Locale[] loc = Currency.getAvailableLocales();

    
    boolean[] b  = { false }; // dla getName() - oznacza normalny wynik
                              // w przeciwieñstwie do wyniku dla ChoiceFormat

    String out = "";

    for (int i=0; i<loc.length; i++) {
      String kraj =  loc[i].getDisplayCountry(def);
      if (kraj.equals("")) continue;
      String lang = loc[i].getDisplayLanguage(def);
      String variant = loc[i].getVariant();

      // Waluta dla lokalizacji loc[i]
      Currency c = Currency.getInstance(loc[i]);

      String icode = c.getCurrencyCode();     // miêdzynarod. kod waluty 

      // symbol w danym kraju
      String sym  =  c.getName(loc[i], Currency.SYMBOL_NAME, b);
      // nazwa w danym kraju
      String name = c.getName(loc[i], Currency.LONG_NAME, b);
      // nazwa po angielsku
      String enName = c.getName(en, Currency.LONG_NAME, b);
  

      out += kraj + " " + lang + " " + variant + 
        "\nwaluta: "+icode+" "+sym+
        "\n"+ name + '\n' + enName + 
        "\n------------------------------------------\n";
     }

     JFrame f = new JFrame();
     JTextArea ta = new JTextArea();
     ta.setFont(new Font("Dialog", Font.BOLD, 14));
     ta.setText(out);
     f.getContentPane().add(new JScrollPane(ta));
     f.pack();
     f.show();
  } 
 
}
