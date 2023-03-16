package datyczas;
import java.util.*;
import java.text.*;
 
public class DateFormatPol {

  public static String polskaData(Date data) {
    String[] mies = { "stycznia", "lutego", "marca", "kwietnia",
                      "maja", "czerwca", "lipca", "sierpnia",
                      "wrzeúnia", "paüdziernika", "listopada",
                      "grudnia"
                    };
    DateFormatSymbols dfs = new DateFormatSymbols();
    dfs.setMonths(mies);
    SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy", dfs);
    return df.format(data); 
  }
 


  public static void main(String[] args) {
    System.out.println( polskaData( new Date() ) );
  } 
 
}
