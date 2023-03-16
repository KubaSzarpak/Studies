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
    String[] txt = { "bela", "Ala", "�", "�", "�", "ala" , "Be", "Ala",
                     "alabama", "be", "Be", "1", "�wik�a", "�wik�a", 
                     "�wikla", "Polska", 
                     "My", "my", "Myk", "myk"  };

    // Domy�lny polski kolator
    Collator col = Collator.getInstance();
    sortShow("Default sort", txt, col);

    // Nowe regu�y
    String rules = " < Polska < A < � < B < C < � < D < E < � < F < G < H" +
                  " < I < J < K < L < � < M < N < � < O < P < Q < R < S < �" +
                  " < T < U < V < W < X < Y < Z < � " +
                  " < a < � < b < c < � < d < e < � < f < g < h" +
                  " < i < j < k < l < � < m < n < � < o < p < q < r < s < �" +
                  " < t < u < v < w < x < y < z < �";

    try {
      col = new RuleBasedCollator(rules);
    } catch (ParseException exc) {
        System.out.println("Wadliwa regu�a na pozycji " + exc.getErrorOffset());
        System.out.println(exc);
        System.exit(1);
    }
    
    sortShow("My new rules sort", txt, col);

  } 
 
}
