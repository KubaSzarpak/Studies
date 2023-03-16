package datyczas;
import java.text.*;
import java.util.*;
import javax.swing.*;
 
public class Daty1 {
 
  public static void main(String[] args) {

    Calendar c = Calendar.getInstance();
    Date teraz = c.getTime();

    SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance();
    
    String[] pattern = {"dd-MM-yyyy",
                        "MMMM, 'dzieñ 'dd ( EE ), 'roku 'yyyy GGGG",
                        "EEEE, dd MMM yyyy 'r.'" 
                       };
    for (int i=0; i<pattern.length; i++) {
      df.applyPattern(pattern[i]);
      System.out.println(df.format(teraz));
    }  

        
    for (int i=0; i<pattern.length; i++) {

      String in=JOptionPane.
                showInputDialog("WprowadŸ datê wg wzorca " + pattern[i]);
      df.applyPattern(pattern[i]);
      Date data = df.parse(in, new ParsePosition(0)); 
      System.out.println(data);
    }  
  } 
 
}
