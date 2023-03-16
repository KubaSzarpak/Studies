import java.io.*;
import java.nio.*;
import java.nio.channels.*;

class MapFiles1 {
  
  String fname = "test";
    
  public MapFiles1() throws Exception {
    init();         // inicjacja pliku testowego
    mapAndChange(); // mapowanie i zmiana danych pliku
    checkResult();  // sprawdzenie wyników
  }
  
  void init() throws IOException {
    int[] data = { 10, 11, 12, 13 }; 
    DataOutputStream out = new DataOutputStream(
                               new FileOutputStream(fname)
                            );
    for (int i=0; i<data.length; i++) out.writeInt(data[i]);
    out.close();
  }  
  
  void mapAndChange() throws IOException {
    
    // Aby dokonywaæ zmian musmi przy³¹czyæ kanal 
    // do pliku otwartego w trybie "read-write"
    RandomAccessFile file =  new RandomAccessFile(fname, "rw");
    FileChannel channel = file.getChannel();

    // Mapowanie pliku
    MappedByteBuffer buf;
    buf  = channel.map( 
                FileChannel.MapMode.READ_WRITE,  // tryb "odczyt-zapis"
                                             0,  // od pocz¹tku pliku 
                            (int)channel.size()  // ca³y plik       
            );   
    
    // Uzyskujemy widok na bufor = zmapowany plik
    IntBuffer ibuf = buf.asIntBuffer();
    
    // Dla ciekawoœci: jakie charakterystyki widoku
    System.out.println(ibuf + " --- Direct: " +  ibuf.isDirect());
    
    int i = 0;
    while (ibuf.hasRemaining()) {
      int num = ibuf.get();       // pobieramy kolejny element  
      ibuf.put(i++, num * 10);    // zapisujemy jego wartoœæ*10 na jego pozycji    }  
    }
    
    // Zapewnia, ¿e zmiany na pewno zostan¹ odzwierciedlone w pliku
    buf.force();
    
    channel.close();
  } 
  
  void checkResult() throws IOException {
    DataInputStream in = null;
    try {
      in = new DataInputStream(new FileInputStream(fname));
      while(true) System.out.println(in.readInt());
    } catch(EOFException exc) { 
        return; 
    } finally { 
        in.close(); 
    }     
  } 

  public static void main(String[] args) throws Exception  {
    new MapFiles1();
  }
}    

      
