package kolator;
import java.util.*;
import java.text.*;
 
public class Kolator1 {

  static void sortShow(String msg, String[] txt, final Collator col) {
    String[] copyTxt = (String[]) txt.clone();
    Arrays.sort(copyTxt, col);
    System.out.println(msg);
    for (int i=0; i < copyTxt.length; i++) {
      System.out.println(copyTxt[i]);
    }
  }      

 
  public static void main(String[] args) {

    // Napisy do posortowania
    String[] txt = { "bela", "Ala", "¹", "¥", "¹", "ala" , "Be", "Ala",
                     "alabama", "be", "Be", "1", "Æwik³a", "æwik³a", 
                     "æwikla", "Polska", 
                     "My", "my", "Myk", "myk"  };

    // Domyœlny polski kolator
    Collator col = Collator.getInstance();
    sortShow("Default sort", txt, col);

    // Nowe regu³y
    String rules = " < Polska < A < ¥ < B < C < Æ < D < E < Ê < F < G < H" +
                  " < I < J < K < L < £ < M < N < Ñ < O < P < Q < R < S < Œ" +
                  " < T < U < V < W < X < Y < Z <  " +
                  " < a < ¹ < b < c < æ < d < e < ê < f < g < h" +
                  " < i < j < k < l < ³ < m < n < ñ < o < p < q < r < s < œ" +
                  " < t < u < v < w < x < y < z < Ÿ";

    try {
      col = new RuleBasedCollator(rules);
    } catch (ParseException exc) {
        System.out.println("Wadliwa regu³a na pozycji " + exc.getErrorOffset());
        System.out.println(exc);
        System.exit(1);
    }
    
    sortShow("My new rules sort", txt, col);

  } 
 
}
