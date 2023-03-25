import java.io.*;

class Lines {
	
  public static void main(String args[]) {
    try {
      FileReader fr = new FileReader(args[0]);
      LineNumberReader lr = new LineNumberReader(fr);      
      BufferedWriter bw = new BufferedWriter(
                              new FileWriter(args[1]));
                                    
      String line;
      while  ((line = lr.readLine()) != null) {
        bw.write( lr.getLineNumber() + " " + line);
        bw.newLine();
      }
      lr.close();
      bw.close();
    } catch(IOException exc) {
        System.out.println(exc.toString());
        System.exit(1);
   }   
    
  }
}
