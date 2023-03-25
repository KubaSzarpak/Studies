import java.nio.*;

class Buffers {
  
  static void say(String s) { System.out.println(s); }
  
  static void showParms(String msg, Buffer b) {
    say("Charakterystyki bufora - " + msg); 
    say("capacity: " + b.capacity());
    say("limit: " + b.limit());
    say("position: " + b.position());
    say("remaining: " + b.remaining());
  }

  public static void main(String args[]) {
    
    // alokacja bufora 10 bajtowego (inicjalnie waro�ci element�w = 0)
    ByteBuffer b = ByteBuffer.allocate(10);     
    showParms("Po utworzeniu", b);
    
    // Zapis dw�ch bajt�w do bufora
    b.put((byte) 7).put((byte) 9);
    showParms("Po dodaniu dw�ch element�w", b);
    
    // Przestawienie bufora
    b.flip();
    showParms("Po przestawieniu", b);
    
    // Teraz mo�emy czyta� wpisane dane 
    say("Czytamy pierwszy element: " + b.get());
    showParms("Po pobraniu pierwszego elementu", b);
    say("Czytamy drugi element: " + b.get());
    showParms("Po pobraniu drugiego elementu", b);
    
    say("Czy mo�emy jeszcze czyta�?");
    try {
      byte x = b.get();
    } catch (BufferUnderflowException exc) {
       say("No, nie - prosz� spojrze� na ostatni limit!");
    }
    
    // Jeszcze raz odczytajmy dane z bufora
    // w tym celu musimy go przewin��
    b.rewind();
    showParms("Po przewini�ciu", b);
    
    say("Czytanie wszystkiego, co wpisali�my");
    while (b.hasRemaining()) 
      say("Jest: " + b.get());
  }
}
