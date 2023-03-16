package datyczas;
import java.util.*;
 
public class Strefy0 {
 
  public static void main(String[] args) {
    TimeZone tz = TimeZone.getDefault();

    // --- Informacje o strefie czasowej

    // identyfikator strefy
    String id = tz.getID();

    // ró¿nica wzglêdem czasu standardowego (UCT)
    int diff = tz.getRawOffset();    

    // czy strefa u¿ywa czasu letniego
    boolean useDST = tz.useDaylightTime();

    // ile czasu trzeba dodaæ do lokalnego zegara
    // aby uzyskaæ czas bez ew. przesuniêcia letniego
    int dstSav = tz.getDSTSavings();  
    
    // Nazwa strefy
    String defName = tz.getDisplayName();

    // Krótka nazwa strefy 
    String shortName = tz.getDisplayName(useDST, TimeZone.SHORT);

    // Nazwa strefy w podanym jêzyku
    String locName = tz.getDisplayName(new Locale("fr")); 

    // Dluga nazwa strefy 
    String fullName = tz.getDisplayName(useDST, TimeZone.LONG);

    // D³uga nazwa strefy w podanym jêzyku
    String locFullName = tz.getDisplayName(useDST, TimeZone.LONG,
                         new Locale("es"));

    // Jaka jest aktualna ró¿nica czasu TERAZ wobec UCT, 
    // z uwzglêdnieniem czasu letniego 
    Date teraz = new Date();
    long ms = teraz.getTime();
    int offset = tz.getOffset(ms);
    
     
    System.out.println("ID = " + id);
    System.out.println("RawOffset = " + diff); 
    System.out.println("useDaylightTime = " + useDST);
    System.out.println("DSTSavings = " + dstSav);
    System.out.println("DisplayName = " + defName);
    System.out.println("DisplayName short = " + shortName);
    System.out.println("DisplayName full = " + fullName);
    System.out.println("DisplayName locale(\"fr\") = " + locName);
    System.out.println("DisplayName full locale(\"es\") = " + locFullName);
    System.out.println("Offset = " + offset);
  } 
 
}
