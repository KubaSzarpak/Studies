import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class ScatteringTest { 

  static final String fname = "scatter.tst";  // nazwa pliku testowego

  public static void main(String[] args) throws Exception {
      
    // Zapisywanie danych testowych
    
    DataOutputStream out = new DataOutputStream(
    	                       new FileOutputStream(fname) );
    short[]  dat1 = { 1, 2, 3, };
    double[] dat2 = { 10.1, 10.2, 10.3 };
    for (int i=0; i < dat1.length; i++) out.writeShort(dat1[i]);
    for (int i=0; i < dat2.length; i++) out.writeDouble(dat2[i]);  
    out.close();

    //-----------------------------------------------------------+
    // Odczytywanie danych testowych                             |
    //-----------------------------------------------------------+
    
    FileInputStream in = new FileInputStream(fname);
    
    // Uzyskanie kana�u
    FileChannel channel = in.getChannel();
    
    // Tablica bajt-bufor�w
    final int SHORT_SIZE  = 2,  // ile bajt�w ma short
              DOUBLE_SIZE = 8;  // ........... i double
    
    ByteBuffer[] buffers = { ByteBuffer.allocate(dat1.length*SHORT_SIZE),
                             ByteBuffer.allocate(dat2.length*DOUBLE_SIZE)   
                           };
    // jedno czytanie z kana�u zapisuje kilka bufor�w !                   
    long r = channel.read(buffers);  

    System.out.println("Liczba bajt�w przeczytanych do obu bufor�w: " + r);
    
    // Przed uzyskiwaniem danych z bufor�w - trzeba je przestawi�!
    buffers[0].flip();
    buffers[1].flip();
    
    // Pierwssy bufor
    // Widok na bufor jako na zawieraj�cy liczby short
    ShortBuffer buf1 = buffers[0].asShortBuffer();  
    System.out.println("Dane 1"); 
    while ( buf1.hasRemaining()) {  
      short elt = buf1.get();       
      System.out.println(elt);      
    }  
    
    // Drugi bufor
    // Widok na bufor jako na zawieraj�cy liczby double 
    DoubleBuffer buf2 = buffers[1].asDoubleBuffer();   
    System.out.println("Dane 2");
    while ( buf2.hasRemaining()) System.out.println(buf2.get());
  }
}