import java.nio.*;

class BufView1 {
  
  public static void main(String args[]) {
    
    final int SHORT_SIZE = 2;
    // Alokacja buforu bajtowego, 
    // mog¹cego przechowywaæ do 10 liczb typu short
    ByteBuffer bb = ByteBuffer.allocate(10*SHORT_SIZE);
    
    // Widok na ten bofor jak na short-bufor
    ShortBuffer sb = bb.asShortBuffer();

    // Dodanie trzech liczb typu short
    short a = 1, b = 2, c = 3;
    sb.put(a).put(b).put(c);
    
    // Co wpisano do bufora? Na wydruku: 1, 2, 3
    sb.flip();
    while (sb.hasRemaining()) System.out.println(sb.get());
    
    // Operujemy teraz na nim za pomoc¹ bufora bajtowego
    // zmieniaj¹c bajty na pozycji 1, 3 i 5.
    byte  x = 4, y = 5, z = 6; 
    bb.put(1, x).put(3, y).put(5, z);
    
    // Co poka¿e short-bufor? Na wydruku 4, 5, 6
    sb.rewind();
    while (sb.hasRemaining()) System.out.println(sb.get());
  }
}
     
    
    