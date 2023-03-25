import java.io.*;
import java.nio.*;
import java.nio.channels.*;

class Ende1 {

  public static void main(String[] args) {

    if (args.length != 4) {
      System.out.println("Syntax: in in_enc out out_enc");
      System.exit(1);
    }
  
    String infile  = args[0],     // plik wej�ciowy
           in_enc  = args[1],     // wej�ciowa strona kodowa
           outfile = args[2],     // plik wyj�ciowy
           out_enc = args[3];     // wyj�ciowa strona kodowa
  
    try {
       FileChannel fcin = new FileInputStream(infile).getChannel();
       FileChannel fcout = new FileOutputStream(outfile).getChannel();
       ByteBuffer buf = ByteBuffer.allocate((int)fcin.size());
       
       // czytanie z kana�u
       fcin.read(buf);
       
       // przeniesienie zawarto�ci bufora do tablicy bytes
       buf.flip();
       byte[] bytes = new byte[buf.capacity()];
       buf.get(bytes);
       
       // dekodowanie - za pomoc� konstruktora klasy String
       String txt = new String(bytes, in_enc);
       
       // enkodowanie za pomoc� metody getBytes z klasy String
       // utworzenie nowego bufora dla kana�u wyj�ciowego
       // zapis do pliku poprzez kana�
       bytes = txt.getBytes(out_enc);
       buf = ByteBuffer.wrap(bytes);
       fcout.write(buf);

       fcin.close();
       fcout.close();
    } catch (Exception e) {
        System.err.println(e);
        System.exit(1);
    }
  
  }
}