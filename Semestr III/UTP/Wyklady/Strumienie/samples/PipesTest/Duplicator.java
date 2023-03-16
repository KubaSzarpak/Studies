import java.io.*;

public class Duplicator extends Thread {
  
  PipedReader fromAuthor;    // potok od autora
  PipedWriter[] toWriters;   // potoki do przepisywaczy
  
  public Duplicator(PipedReader pr,       // potok od autora
                    SpaceToWrite[] space  // na czym pisz¹ pzrepisywacze?
                    ) throws IOException {
    fromAuthor = pr;
    
    int numOfWriters = space.length;      // tylu jest przepisywaczy
                                          // ile miejsc na których pisz¹  
    
    // Tworzymy tablicê potoków do przepisywaczy
    toWriters = new PipedWriter[numOfWriters];
    
    for (int i = 0; i < numOfWriters; i++) { // dla ka¿dego przepisywacza
      
      // tworzymy potok do niego
      toWriters[i] = new PipedWriter();     
      
      // tworzymy przepisywacza
      // podaj¹c: nazwê, z jakiego potoku ma czytaæ, miejsce gdzie ma pisaæ
      TxtWriter tw = new TxtWriter("TxtWriter " + (i+1),
                                   new PipedReader( toWriters[i]), // po³¹czenie! 
                                   space[i]);
      
      // uruchamiamy w¹tek przepisywacza
      tw.start();  
    }
  }
  
  // Kod wykonywany w w¹tku Duplikatora
  public void run() {
    try {
      // Buforowanie potoku od autora
      BufferedReader in = new BufferedReader(fromAuthor);
      
      // czytanie wierszy z potoku od autora 
      // i zapisywanie ich do potoków, czytanych przez przepisywaczy
      while (true) {
        String line = in.readLine();
        for (int i = 0; i < toWriters.length; i++) {
          toWriters[i].write(line);
          toWriters[i].write('\n');
        }
        if (line.equals("Koniec pracy")) break;
      }
    } catch (IOException exc) { return; }
    System.out.println("Duplikator zakoñczy³ dzia³anie");
  }  
        
} 
      
    
    
  