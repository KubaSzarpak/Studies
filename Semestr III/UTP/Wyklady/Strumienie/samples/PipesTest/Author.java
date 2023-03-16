import java.io.*;
import java.util.*;

public class Author {
  
  private int linesToWrite;    // ile wierszy ma napisac autor
  String[] words;              // z jakich slów siê bêd¹ sk³adaæ
  private Writer out;          // strumieñ do którego zapisuje teksty
  static final int N = 5;      // maksymalna liczba s³ów w wierszu 
  
  public Author(int l, String[] words, Writer w) {
    linesToWrite = l;
    this.words = words;
    out = w;
    try {    
      write();                  // wywo³anie pisania
    } catch(IOException exc) { 
        System.out.println(exc.toString());
    } catch(InterruptedException exc) {}      
  }

  // Metoda pisania przez autora
  public void write() throws IOException, 
                             InterruptedException {
    Random rand = new Random();
    for (int i=0; i < linesToWrite; i++) {
      
      // Ka¿dy wiersz sk³ada siê z losowo wybranej nw liczby s³ów
      int nw = rand.nextInt(N) + 1; 
      String line = "";

      for (int k=0; k<nw; k++) {   // s³owa s¹ losowane z tablicy words
        int wordNum = rand.nextInt(words.length);
        line += words[wordNum] + " ";
      }
      out.write(line);
      out.write('\n');
      Thread.sleep((rand.nextInt(3) + 1) * 1000);  // autor myœi nad 
    }                                              // nastêpnym wierszem :-)
    out.write("Koniec pracy\n");
    out.close();
    System.out.println("Autor skoñczy³ pisaæ");
  }
}
        
      
    