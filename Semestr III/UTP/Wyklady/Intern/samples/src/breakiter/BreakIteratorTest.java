package breakiter;
import java.text.BreakIterator;

public class BreakIteratorTest {
 
  // Metoda wypisuj�ca na konsoli
  //  - zdania - je�li przekazano jako argument typ warto�� 0 
  //  - lub s�owa - je�li przekazano jako argument typ warto�� 1 
  // wyodr�bnione z tekstu txt

  private static void show(int typ, String txt) {
    String[] head = { "Zdania", "S�owa" }; // Nag��wek: dla typ==0 "Zdania"
                                           //           dla typ==1 "S�owa"

    // Uzyskanie odpowiedniego breakiteratora
    // dla typ == 0 b�dzie to iterator "po zdaniach"
    // dla typ == 1 b�dzie to iterator "po s�owach"
    BreakIterator bri = (typ == 0 ? BreakIterator.getSentenceInstance()
                                  : BreakIterator.getWordInstance() 
                        ); 
    System.out.println(head[typ]); // Wypisanie nag��wka

    // Po stworzeniu iteratora a przed jego u�yciem
    // nale�y ustali� tekst, kt�ry b�dzie przetwarzany 

    bri.setText(txt);

    // Iterowanie

    int start = bri.first();  // pozycja pierwszego podzia�u na elementy
    int end   = bri.next();   // pozycja nast�pnego podzia�u na elementy

    while (end != BreakIterator.DONE) { // dop�ki mo�na dzieli� tekst

      // Wyodr�bniamy element
      String elt = txt.substring( start, end);

      // Wypisujemy element je�eli to jest iterator dla zda�
      // albo je�eli to jest iterator dla s��w, a elementem jest s�owo
      // Czy to jest s�owo stwierdzamy za pomoc� metody 
      // isIteratorWord(...) - zob. dalej

      if (typ == 0 | (typ == 1 && isIteratorWord(elt)))
         System.out.println(start + ": " +  elt);


      start = end;        // pocz�tek nast�pnego (po wypisanym) elementu
      end = bri.next();   // pozycja  nast�pnego podzia�u
    }
  }
  
  // Metoda stwierdza czy element wyodr�bniony przez iterator s��w
  // jest s�owem. B�dzie nim ka�dy element, kt�ry zawiera cho� jedn�
  // liter� lub cyfr�. Czy dany znak jest liter� lub cyfr� 
  // stwierdzamy za pomoc� statycznej metody z klasy Character 
  // Character.isLetterOrDigit(znak)

  private static boolean isIteratorWord(String elt) {
    int l = elt.length();
    for (int i=0; i < l; i++) 
      if (Character.isLetterOrDigit(elt.charAt(i))) return true;
    return false;
  } 
    
 
  public static void main(String[] args) {

    String s = "Ala Kot-Kotowska ma kota. A mleko? "+ 
               "Pies go wych�epta� (swobodnie) - 0.1 litra. " +
               "Tak? Nie! Chyba nie... A mo�e? By�a�by to \"ha�ba\"?!!!";

    show(0, s); // poka� zdania
    show(1, s); // poka� s�owa
  } 
 
}
