import java.nio.*;
import java.nio.channels.*;
import java.io.*;

class DirectTransfer {

  String inFileName;
  String outFileName;
  
  DirectTransfer(String infn, String outfn) throws Exception {
    inFileName  = infn;
    outFileName = outfn;
    directTransfer();
    copyByStream();
  }


  void directTransfer() throws Exception {
    
    FileInputStream in = new FileInputStream(inFileName);
    FileOutputStream out =  new FileOutputStream(outFileName);
    FileChannel fcin = in.getChannel();
    FileChannel fcout = out.getChannel();
    long size = fcin.size(); 
    System.out.println("Copying file " + size + "B."); 
    
    long start =  System.currentTimeMillis(); 
    
    // Bezpoœredni transfer 
    fcout.transferFrom(fcin, 0, size);
    
    long end = System.currentTimeMillis();
    
    System.out.println("Direct transfer time " + (end - start));

   } 
  
  
  final int BUFSIZE = 5000000;  
  void copyByStream() throws Exception  {
    
    FileInputStream fin = new FileInputStream(inFileName);
    BufferedInputStream in = new BufferedInputStream(fin, BUFSIZE );
    FileOutputStream fout = new FileOutputStream(outFileName);
    BufferedOutputStream out = new BufferedOutputStream(fout, BUFSIZE);
   
    byte[] b = new byte[BUFSIZE];
    long start =  System.currentTimeMillis(); 
    while (true) {
      int n = in.read(b);
      if (n == -1) break;
      out.write(b, 0, n);
    }
    in.close();
    out.close();
     
    long end = System.currentTimeMillis();
    System.out.println("Stream time " + (end - start));
  }
  
   
   public static void main(String[] args) throws Exception {
     new DirectTransfer(args[0], args[1]);
   }
}