import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;

class GatheringTest { 

  public static void main(String[] args) throws Exception {
    
    // To bêd¹ sta³e czê¹ci ka¿dego pliku
    String sHeader = "To jest nag³ówek. Mo¿e byæ du¿y";
    String sFooter = "To jest zakoñczenie. Mo¿e byæ du¿e";

    // To bêd¹ dane, które siê zmieniaj¹ od pliku do pliku
    byte[][] dane = { { 1, 2, 3},
                     { 9, 10, 11, 12 },
                     { 100, 101}
                   };
    
    Charset charset = Charset.forName("windows-1250");
    ByteBuffer header = charset.encode(CharBuffer.wrap(sHeader)),
               footer = charset.encode(CharBuffer.wrap(sFooter));
    
    // Drugi element tablicy buforów bêdzie dynamicznie siê zmianial
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
