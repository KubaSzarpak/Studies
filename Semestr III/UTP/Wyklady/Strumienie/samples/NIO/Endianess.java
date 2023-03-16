import java.nio.*;

class Endianness {
  
   static void show(int n) {
       String s = Integer.toHexString(n);
       int l = s.length();
       for (int i=l; i < 8; i++) s = '0' + s;
       System.out.println("Liczba " + n + " hex -> " + s.toUpperCase());
   } 
      
   public static void main(String args[]) {
     int num = Integer.parseInt(args[0]);
     ByteBuffer buf = ByteBuffer.allocate(4);
     System.out.println(buf.order().toString());
     IntBuffer b1 = buf.asIntBuffer();
     System.out.println("Porz퉐ek b1 " + b1.order());
     b1.put(num);
     b1.flip();
     show(b1.get());
     buf.order(ByteOrder.LITTLE_ENDIAN);
     System.out.println("Porz퉐ek buf " + buf.order());     
     System.out.println("Porz퉐ek b1 " + b1.order());
     b1.rewind(); 
     show(b1.get());
     System.out.println("Porz퉐ek buf " + buf.order());
     System.out.println("Porz퉐ek dziedzizcony " + buf.asIntBuffer().order());
     show(buf.asIntBuffer().get());
  }
}  
     
        
     
     
    
    
