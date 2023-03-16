package datyczas;
import java.util.*;
 
public class Strefy0 {
 
  public static void main(String[] args) {
    TimeZone tz = TimeZone.getDefault();

    // --- Informacje o strefie czasowej

    // identyfikator strefy
    String id = tz.getID();

    // r�nica wzgl�dem czasu standardowego (UCT)
    int diff = tz.getRawOffset();    

    // czy strefa u�ywa czasu letniego
    boolean useDST = tz.useDaylightTime();

    // ile czasu trzeba doda� do lokalnego zegara
    // aby uzyska� czas bez ew. przesuni�cia letniego
    int dstSav = tz.getDSTSavings();  
    
    // Nazwa strefy
    String defName = tz.getDisplayName();

    // Kr�tka nazwa strefy 
    String shortName = tz.getDisplayName(useDST, TimeZone.SHORT);

    // Nazwa strefy w podanym j�zyku
    String locName = tz.getDisplayName(new Locale("fr")); 

    // Dluga nazwa strefy 
    String fullName = tz.getDisplayName(useDST, TimeZone.LONG);

    // D�uga nazwa strefy w podanym j�zyku
    String locFullName = tz.getDisplayName(useDST, TimeZone.LONG,
                         new Locale("es"));

    // Jaka jest aktualna r�nica czasu TERAZ wobec UCT, 
    // z uwzgl�dnieniem czasu letniego 
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
