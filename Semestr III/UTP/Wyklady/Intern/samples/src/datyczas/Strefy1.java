package datyczas;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
 
public class Strefy1 {
 
  public static void main(String[] args) {

    // Konstruowanie stref czasowych
    TimeZone myTz = TimeZone.getTimeZone("Europe/Warsaw");
    TimeZone java = TimeZone.getTimeZone("Asia/Jakarta");
    TimeZone cuba = TimeZone.getTimeZone("America/Havana"); 

    // za pomoc� pokazanej dalej metody getDiffMsg
    // wyliczamy i pokazujemy aktualn� r�nic� czasu
    // pomi�dzy sterfami czasowymi

    System.out.println(getDiffMsg(myTz, java));
    System.out.println("--------------------------------------------------");
    System.out.println(getDiffMsg(myTz, cuba));
    System.out.println("--------------------------------------------------");
    System.out.println(getDiffMsg(cuba, java));
    System.out.println("--------------------------------------------------");

    // Jakie strefy czasowe maj� podan� r�nic� czasu wobec GMT
    
   for (int k = 12; k <= 14; k++) { 
     String[] ids = TimeZone.getAvailableIDs(k*3600000);
     Arrays.sort(ids);
     System.out.println(
      "Strefy czasowe maj�ce r�nice +" + k + " godzin wobec GMT" );
     for (int i=0; i < ids.length; i++) {
       System.out.println(ids[i]);
     }
     System.out.println("--------------------------------------------------");
   }
  }  

  static String getDiffMsg(TimeZone z1, TimeZone z2) {
    Date data = new Date();
    long teraz = data.getTime();
    double offset1 = z1.getOffset(teraz)/3600000.0; 
    double offset2 = z2.getOffset(teraz)/3600000.0; 
    double diff;
    if (offset1 > offset2)  diff = -(offset1 - offset2);
    else diff = offset2 - offset1;
    String out =  "R�nica czasu pomi�dzy" + '\n' +
                  z1.getID() + " i " + z2.getID()  + '\n' +
                  "wynosi teraz : " + diff + " godz."  + '\n' +
                  "W strefie " + z1.getID() +  
                       (z1.inDaylightTime(data) ? " " : " nie ") +
                       "dzia�a czas letni"  + '\n' + 
                  "W strefie " + z2.getID() + 
                       (z2.inDaylightTime(data) ? " " : " nie ") +
                       "dzia�a czas letni";
                       
    return out;
  }     
 
}
