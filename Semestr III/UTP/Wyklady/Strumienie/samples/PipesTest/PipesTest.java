import java.io.*;

class PipesShow {
  
  PipedWriter authorWrites = new PipedWriter(); // potok, do którego pisze autor 
  PipedReader duplicatorReads;                  // potok, z ktorego czyta duplikator
  
  Duplicator dup;                                
  
  PipesShow(int numLines, int numWriters) {
    
    // ka¿dy przepisywacz na swoj¹ przestrzeñ pisania
    SpaceToWrite[] writeSpace = new SpaceToWrite[numWriters];
    for (int i=0; i < writeSpace.length; i++) 
      writeSpace[i] = new SpaceToWrite(20, 30); // 20 wierszy, 30 kolumn
    
    try {
      // Po³¹czenie potoku do ktorego pisze autor
      // z nowoutworzonym potokiem, z którego bêdzie czyta³ duplikator
      duplicatorReads = new PipedReader(authorWrites);
      
      // utworzenie duplikatora (on z kolei stworzy i uruchomi przepisywaczy)
      dup = new Duplicator(duplicatorReads, // sk¹d bêdzie czyta³
                           writeSpace);     // przetstrzeñ pisania dla przepisywaczy
      
      // start w¹tku duplikatora
      dup.start();    
      
    } catch (IOException exc) {
        System.out.println("Nie mo¿na stworzyæ duplikatora");
        exc.printStackTrace();
        System.exit(1);
    }
    
    SpaceToWrite.show(numWriters); // pokazanie ogólnej przestrzeni pisania
                                   // grupuj¹cej przestrzenie pisania 
                                   // ka¿dego przepisywacza 

    // Teraz autor bêdzie pisa³!
    // Utworzenie obiektu klasy Autor powoduje rozpoczêcie przez niego pisania
    
    String words[] = { "Ala", "ma", "kota", "i", "psa" };
    
    Author autor = new Author(numLines,      // ile wierszy ma napisaæ
                              words,         // z jakich s³ów sk³adaæ teksty
                              authorWrites); // Dok¹d je zapisywaæ 
  }

  public static void main(String args[]) { 
    int numLin = 0; // ile wierszy ma napisaæ autor
    int numWri = 0; // ilu jest przepisywaczy
    try {
      numLin = Integer.parseInt(args[0]);
      numWri = Integer.parseInt(args[1]);
    } catch(Exception exc) {
        System.out.println("Syntax: java  PipesShow numLines numWri");
        System.exit(1);
    }    
    new PipesShow(numLin, numWri);  
  }
}
