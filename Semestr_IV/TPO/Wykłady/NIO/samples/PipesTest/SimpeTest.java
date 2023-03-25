import java.io.*;
class DataPutter extends Thread {
  
  OutputStream out;
  
  public DataPutter(OutputStream o) {
    out = o;
  }
  
  public void run() {
    try {
      for (char c = 'a'; c <= 'z'; c++) out.write(c);
      out.close();
    } catch(IOException exc) { return; }
    
  }
}

class DataGetter extends Thread {
  
  InputStream in;
  
  public DataGetter(InputStream i) {
    in = i;
  }
  
  public void run() {
    try {
      int c;
      while ((c = in.read()) != -1) System.out.println((char) c);
    } catch(IOException exc) { return; }
  }
}


class Main {
  public static void main(String[] args) throws IOException {
    PipedOutputStream pout = new PipedOutputStream();
    PipedInputStream pin = new PipedInputStream(pout);
    new DataPutter(pout).start();
    new DataGetter(pin).start();
  }
}