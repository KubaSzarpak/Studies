package kolator;
import java.util.*;
import java.text.*;
 
public class Kolator2 {

  public static void main(String[] args) {

    // Napisy do posortowania
    String[] txt = { "bela", "Ala", "�", "�", "�", "ala" , "Be", "Ala",
                     "alabama", "be", "Be", "1", "�wik�a", "�wik�a", 
                     "�wikla", "Polska", 
                     "My", "my", "Myk", "myk"  };

    // Domy�lny polski kolator
    Collator col = Collator.getInstance();

    // Lista kluczy
    List keys = new ArrayList();

    // Uzyskanie kluczy dla napis�w
    // warto�ci kluczy uzyskujemy od kolatora
    for (int i=0; i<txt.length; i++) {
      CollationKey key = col.getCollationKey( txt[i] );
      keys.add(key);
    }

    // Sortowanie
    // por�wnywane mog� by� tylko klucze uzyskane od tego samego kolatora!
    
    Collections.sort(keys);
    
    // Pokazanie wyniku
    // mamy klucze u�o�one w okre�lonym porz�dku napis�w, kt�re reprezentuj�
    // musimy pobra� napis skojarzony z kluczem
    
    for (Iterator it = keys.iterator(); it.hasNext(); ) {
      CollationKey key = (CollationKey) it.next();
      String napis = key.getSourceString();
      System.out.println(napis);
    }
  } 
 
}
