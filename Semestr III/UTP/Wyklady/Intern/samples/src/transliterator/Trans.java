package transliterator;
import com.ibm.icu.text.*;
import java.util.Enumeration;
import javax.swing.*;
 
public class Trans {

  static void show(String msg, Enumeration e) {
    System.out.println(msg);
    while (e.hasMoreElements()) {
      System.out.println(e.nextElement());
    }
  }

 
  public static void main(String[] args) {
    show("IDs", Transliterator.getAvailableIDs());
    Transliterator tg = Transliterator.getInstance("Latin-Greek");
    ReplaceableString text = new ReplaceableString("yia maz!");
    tg.transliterate(text);
    JOptionPane.showMessageDialog(null, text);
    Transliterator tr = Transliterator.getInstance("Latin-Cyrillic");
    text = new ReplaceableString("Vsego horoshego !");
    tr.transliterate(text);
    JOptionPane.showMessageDialog(null, text);
  } 
 
}
