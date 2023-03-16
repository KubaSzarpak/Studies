import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;

class MapFiles2 {

  public static void main(String[] args) throws Exception  {
    Charset inCharset = Charset.forName("windows-1250"),
           outCharset = Charset.forName("ISO-8859-2");
    
    RandomAccessFile file = new RandomAccessFile(args[0], "rw");
    FileChannel fc = file.getChannel();
    
    // Mapowanie pliku
    MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE,
                                  0, (int) fc.size());
    
    // Utworzenia bufora znakowego ze zdekodowanymi znakami
    // z bufora bajtowego (mapuj¹ce plik). Konwersja: win1250->unicode
    CharBuffer cbuf = inCharset.decode(mbb);
    
    // Okazuje siê, ¿e ten nowo utworzony bufor opakowuje tablicê
    // zatem mo¿emy j¹ uzyskaæ i dzia³aæ na jej elementach
    // to dzialanie oznacza dzialanie na elementach bufora
    char[] chArr = cbuf.array();
    for (int i=0; i < chArr.length; i++) 
      chArr[i] = Character.toUpperCase(chArr[i]);
    
    // Po dekodowaniu bufor bajtowy musi byæ przewiniêty do pocz¹tku
    // aby koder (zob. dalej) móg³ w nim zapisywaæ kodowane dane
    mbb.rewind();
    
    // Utworzenie kodera, zamieniaj¹cego Unicode na wyjœciow¹ stronê kodow¹
    CharsetEncoder encoder = outCharset.newEncoder();
    
    // Koder zapisuje istniej¹cy bufor mbb (ten który mapuje plik)
    // ostatni argument - true oznacza zakoñczenie pracy kodera na tym wywo³aniu
    encoder.encode(cbuf, mbb, true);
    fc.close();
  }
}
    
