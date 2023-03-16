package datyczas;
import java.util.*;
import java.text.*;
import java.awt.*;
import javax.swing.*;
 
public class DateFormatSymbolsShow {

  String[] lang = { "fr", "es", "de", "ru" };

  String out = "";

  public DateFormatSymbolsShow() {
    for (int i=0; i<lang.length; i++) {
      Locale  loc = new Locale(lang[i]);
      DateFormatSymbols dfs = new DateFormatSymbols(loc);
      out += '\n' + loc.getDisplayLanguage();
      // nazwy er
      addToOut("Ery: ", dfs.getEras());
      // nazwy miesiêcy
      addToOut("Miesi¹ce: ", dfs.getMonths());
      // skróty miesiêcy
      addToOut("Miesi¹ce - skróty: ", dfs.getShortMonths());
      // nazwy dni tygodnia
      addToOut("Dni tygodnia: ", dfs.getWeekdays()); 
      // skróty nazw dni tygodnia
      addToOut("Dni tygodnia - skróty: ", dfs.getShortWeekdays()); 
    }  
    JTextArea ta = new JTextArea(out);
    ta.setFont(new Font("Dialog", Font.BOLD, 14));
    JFrame f = new JFrame();
    f.getContentPane().add(ta);
    f.pack();
    f.show();      
  }

  void addToOut(String msg, String[] s) {
    out += "\n" + msg;
    for (int i=0; i<s.length; i++) {
      out += ' ' + s[i];
    }
  }

  public static void main(String[] args) {
    new DateFormatSymbolsShow();
  }
 
}
