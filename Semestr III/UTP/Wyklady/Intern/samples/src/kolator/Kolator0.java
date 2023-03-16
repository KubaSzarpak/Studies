package kolator;
import java.util.*;
import java.text.*;
 
public class Kolator0 {

  static void sortShow(String msg, String[] txt, Collator col) {
    String[] copyTxt = (String[]) txt.clone();
    Arrays.sort(copyTxt, col);
    System.out.println(msg);
    for (int i=0; i < copyTxt.length; i++) {
      System.out.println(copyTxt[i]);
    }
  }      

 
  public static void main(String[] args) {
    String[] txt = { "bela", "Ala", "¹", "¥", "¹", "ala" , "Be", "Ala",
                     "alabama", "be", "Be", "1", "æ", "my", "My", "Myk", "myk"  };
    Collator col = Collator.getInstance();
    sortShow("Sort TERTIARY", txt, col);
    col.setStrength(Collator.PRIMARY);
    sortShow("Sort PRIMARY", txt, col);
    
//   Collator col1 = Collator.getInstance(new Locale("en"));
//    sortShow("Sort en", txt, col1); 
  } 
 
}
