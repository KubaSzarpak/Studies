import java.nio.*;

class BufView2 {

  static void say(String s) { System.out.println(s); }
  static void showParms(String msg, Buffer b) {
    say("\nCharakterystyki bufora - " + msg); 
    say("capacity: " + b.capacity());
    say("limit: " + b.limit());
    say("position: " + b.position());
    say("remaining: " + b.remaining());
  }
  
  public static void main(String args[]) {
    
    final int SHORT_SIZE = 2;
    ByteBuffer bb = ByteBuffer.allocate(10*SHORT_SIZE);
    ShortBuffer sb = bb.asShortBuffer();
    short a = 1, b = 2, c = 3;
    sb.put(a).put(b).put(c);
    //showParms("bufor short - po dodaniu liczb", sb);
    //showParms("bufor bajtowy - po dodaniu liczb", bb);    
    
    sb.flip();
    bb.limit(sb.limit()*SHORT_SIZE);
    //showParms("bufor short - po przestawieniu", sb);
    //showParms("bufor bajtowy - po flip bufora short", bb);    
    System.out.print("Bajty wpisanych danych");
    while (bb.hasRemaining()) System.out.print(" " + bb.get());  
  }
}