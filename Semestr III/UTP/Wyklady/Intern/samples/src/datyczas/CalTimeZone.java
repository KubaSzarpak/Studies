package datyczas;
import java.util.*;

 
public class CalTimeZone {
 
  public static void main(String[] args) {
    TimeZone tz = TimeZone.getTimeZone("Asia/Jakarta");
    Calendar c = Calendar.getInstance(tz);
    System.out.println("Current time: " + c.getTime());
    System.out.println("Java time: " +
         c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE)); 
  } 

}