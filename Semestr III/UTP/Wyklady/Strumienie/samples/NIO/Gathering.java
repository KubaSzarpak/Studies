import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;

class GatheringTest { 

  public static void main(String[] args) throws Exception {
    
    // To b�d� sta�e cz�ci ka�dego pliku
    String sHeader = "To jest nag��wek. Mo�e by� du�y";
    String sFooter = "To jest zako�czenie. Mo�e by� du�e";

    // To b�d� dane, kt�re si� zmieniaj� od pliku do pliku
    byte[][] dane = { { 1, 2, 3},
                     { 9, 10, 11, 12 },
                     { 100, 101}
                   };
    
    Charset charset = Charset.forName("windows-1250");
    ByteBuffer header = charset.encode(CharBuffer.wrap(sHeader)),
               footer = charset.encode(CharBuffer.wrap(sFooter));
    
    // Drugi element tablicy bufor�w b�dzie dynamicznie si� zmianial
    // na razie = null  
    ByteBuffer[] contents = { header, null, footer }; 
    for (int i = 0; i<dane.length; i++) {
      FileChannel fc = new FileOutputStream("plik"+i).getChannel();
      contents[1] = ByteBuffer.wrap(dane[i]);  // podstawienie zmiennych danych
      fc.write(contents);
      fc.close();
      header.rewind();
      footer.rewind();      
    }
  }
      
        
}  
