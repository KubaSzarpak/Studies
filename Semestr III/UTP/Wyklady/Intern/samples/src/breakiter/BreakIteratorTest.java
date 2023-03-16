package breakiter;
import java.text.BreakIterator;

public class BreakIteratorTest {
 
  // Metoda wypisuj¹ca na konsoli
  //  - zdania - jeœli przekazano jako argument typ wartoœæ 0 
  //  - lub s³owa - jeœli przekazano jako argument typ wartoœæ 1 
  // wyodrêbnione z tekstu txt

  private static void show(int typ, String txt) {
    String[] head = { "Zdania", "S³owa" }; // Nag³ówek: dla typ==0 "Zdania"
                                           //           dla typ==1 "S³owa"

    // Uzyskanie odpowiedniego breakiteratora
    // dla typ == 0 bêdzie to iterator "po zdaniach"
    // dla typ == 1 bêdzie to iterator "po s³owach"
    BreakIterator bri = (typ == 0 ? BreakIterator.getSentenceInstance()
                                  : BreakIterator.getWordInstance() 
                        ); 
    System.out.println(head[typ]); // Wypisanie nag³ówka

    // Po stworzeniu iteratora a przed jego u¿yciem
    // nale¿y ustaliæ tekst, który bêdzie przetwarzany 

    bri.setText(txt);

    // Iterowanie

    int start = bri.first();  // pozycja pierwszego podzia³u na elementy
    int end   = bri.next();   // pozycja nastêpnego podzia³u na elementy

    while (end != BreakIterator.DONE) { // dopóki mo¿na dzieliæ tekst

      // Wyodrêbniamy element
      String elt = txt.substring( start, end);

      // Wypisujemy element je¿eli to jest iterator dla zdañ
      // albo je¿eli to jest iterator dla s³ów, a elementem jest s³owo
      // Czy to jest s³owo stwierdzamy za pomoc¹ metody 
      // isIteratorWord(...) - zob. dalej

      if (typ == 0 | (typ == 1 && isIteratorWord(elt)))
         System.out.println(start + ": " +  elt);


      start = end;        // pocz¹tek nastêpnego (po wypisanym) elementu
      end = bri.next();   // pozycja  nastêpnego podzia³u
    }
  }
  
  // Metoda stwierdza czy element wyodrêbniony przez iterator s³ów
  // jest s³owem. Bêdzie nim ka¿dy element, który zawiera choæ jedn¹
  // literê lub cyfrê. Czy dany znak jest liter¹ lub cyfr¹ 
  // stwierdzamy za pomoc¹ statycznej metody z klasy Character 
  // Character.isLetterOrDigit(znak)

  private static boolean isIteratorWord(String elt) {
    int l = elt.length();
    for (int i=0; i < l; i++) 
      if (Character.isLetterOrDigit(elt.charAt(i))) return true;
    return false;
  } 
    
 
  public static void main(String[] args) {

    String s = "Ala Kot-Kotowska ma kota. A mleko? "+ 
               "Pies go wych³epta³ (swobodnie) - 0.1 litra. " +
               "Tak? Nie! Chyba nie... A mo¿e? By³a¿by to \"hañba\"?!!!";

    show(0, s); // poka¿ zdania
    show(1, s); // poka¿ s³owa
  } 
 
}
