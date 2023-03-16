import java.io.*;

class Convert {

  public static void main(String[] args) {

    if (args.length != 4) {
      System.out.println("Syntax: in in_enc out out_enc");
      System.exit(1);
    }
  
    String infile  = args[0],     // plik wejœciowy
           in_enc  = args[1],     // wejœciowa strona kodowa
           outfile = args[2],     // plik wyjœciowy
           out_enc = args[3];     // wyjœciowa strona kodowa
  
    try {
       FileInputStream fis = new FileInputStream(infile);
       BufferedReader in = new BufferedReader(new InputStreamReader(fis, in_enc));
       FileOutputStream fos = new FileOutputStream(outfile);
       BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos, out_enc));
       String line;
       while ((line = in.readLine()) != null) {
         out.write(line);
         out.newLine();
       }
       in.close();
       out.close();
    } catch (IOException e) {
        System.err.println(e);
        System.exit(1);
    }
  
  }
}
