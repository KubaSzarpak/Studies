package kolator;
import java.util.*;
import java.text.*;
 
public class Kolator2 {

  public static void main(String[] args) {

    // Napisy do posortowania
    String[] txt = { "bela", "Ala", "¹", "¥", "¹", "ala" , "Be", "Ala",
                     "alabama", "be", "Be", "1", "Æwik³a", "æwik³a", 
                     "æwikla", "Polska", 
                     "My", "my", "Myk", "myk"  };

    // Domyœlny polski kolator
    Collator col = Collator.getInstance();

    // Lista kluczy
    List keys = new ArrayList();

    // Uzyskanie kluczy dla napisów
    // wartoœci kluczy uzyskujemy od kolatora
    for (int i=0; i<txt.length; i++) {
      CollationKey key = col.getCollationKey( txt[i] );
      keys.add(key);
    }

    // Sortowanie
    // porównywane mog¹ byæ tylko klucze uzyskane od tego samego kolatora!
    
    Collections.sort(keys);
    
    // Pokazanie wyniku
    // mamy klucze u³o¿one w okreœlonym porz¹dku napisów, które reprezentuj¹
    // musimy pobraæ napis skojarzony z kluczem
    
    for (Iterator it = keys.iterator(); it.hasNext(); ) {
      CollationKey key = (CollationKey) it.next();
      String napis = key.getSourceString();
      System.out.println(napis);
    }
  } 
 
}
