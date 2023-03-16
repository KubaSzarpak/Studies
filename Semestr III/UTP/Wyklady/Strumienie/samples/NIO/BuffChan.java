import java.io.*;
import java.nio.*;
import java.nio.channels.*;

class BuffChan {
  
  String fname = "testfile.tmp";
  
  // inicjalizacja danych testowych
  void init() throws Exception {
    double[] data = { 0.1, 0.2, 0.3 };
    DataOutputStream out = new DataOutputStream(
                             new FileOutputStream(fname)
                            );
    for (int i=0; i < data.length; i++) out.writeDouble(data[i]);
    out.close();
    
  }
                             
  
  BuffChan() throws Exception {
    
    // inicjalizavja danych testowych
    init();
    
    // utworzenie bufora
    ByteBuffer buf = ByteBuffer.allocate(1000); // nie wiemy ile, maks 100B
    
    // uzyskanie kana³u
    FileChannel fcin = new FileInputStream(fname).getChannel();
    
    // czytanie z kana³u do bufora
    fcin.read(buf);
    fcin.close();
    
    // przestawienie bufora bajtowego
    buf.flip();
    
    // Utworzenie widoku bufora
    DoubleBuffer dbuf = buf.asDoubleBuffer();
    
    // Wypisanie odczytanych danych
    while (dbuf.hasRemaining()) System.out.println(dbuf.get());
  }
    
  public static void main(String args[]) throws Exception {
    new BuffChan();
  }
}    
    
    
    
    
      