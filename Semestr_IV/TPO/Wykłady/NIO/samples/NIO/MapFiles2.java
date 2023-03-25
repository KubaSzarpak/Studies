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
    // z bufora bajtowego (mapuj�ce plik). Konwersja: win1250->unicode
    CharBuffer cbuf = inCharset.decode(mbb);
    
    // Okazuje si�, �e ten nowo utworzony bufor opakowuje tablic�
    // zatem mo�emy j� uzyska� i dzia�a� na jej elementach
    // to dzialanie oznacza dzialanie na elementach bufora
    char[] chArr = cbuf.array();
    for (int i=0; i < chArr.length; i++) 
      chArr[i] = Character.toUpperCase(chArr[i]);
    
    // Po dekodowaniu bufor bajtowy musi by� przewini�ty do pocz�tku
    // aby koder (zob. dalej) m�g� w nim zapisywa� kodowane dane
    mbb.rewind();
    
    // Utworzenie kodera, zamieniaj�cego Unicode na wyj�ciow� stron� kodow�
    CharsetEncoder encoder = outCharset.newEncoder();
    
    // Koder zapisuje istniej�cy bufor mbb (ten kt�ry mapuje plik)
    // ostatni argument - true oznacza zako�czenie pracy kodera na tym wywo�aniu
    encoder.encode(cbuf, mbb, true);
    fc.close();
  }
}
    
