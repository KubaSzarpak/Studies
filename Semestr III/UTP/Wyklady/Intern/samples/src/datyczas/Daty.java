package datyczas;
import java.util.*;
import java.text.*;
import javax.swing.*;
 
public class Daty {
 
  public static void main(String[] args) {
    Date teraz = new Date();

    int[] styles = { DateFormat.SHORT, DateFormat.MEDIUM,
                     DateFormat.LONG, DateFormat.FULL }; 

    String outMsg = "";

    for (int i=0; i < styles.length; i++) {

      DateFormat df = DateFormat.getDateTimeInstance(
                                    styles[i],          // styl daty
                                    DateFormat.FULL     // styl czasu
                      );

      outMsg += df.format(teraz) + '\n' + "-----------------\n" ;
    }
    System.out.println(outMsg); 
      

    Locale[] llist = { new Locale("de"),
                       new Locale("fr"),
                       new Locale("es"),
                       new Locale("ar"),
                       new Locale("ru"),
                       new Locale("th")
                     };
    outMsg = "";
    for (int i=0; i < llist.length; i++) {
      DateFormat df = DateFormat.getDateTimeInstance(
                                    DateFormat.FULL,    // styl daty
                                    DateFormat.FULL,    // styl czasu              
                                    llist[i]            // lokalizacja
                      );
      outMsg += df.format(teraz) + '\n' + "-----------------\n" ;
    }
    JOptionPane.showMessageDialog(null, outMsg);
    System.exit(0);
  } 
 
}
