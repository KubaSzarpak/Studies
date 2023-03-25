import java.io.*;

class Obs {
  String name;
  double[] data;
  
  public Obs() {}
  
  public Obs(String nam, double[] dat) {
    name = nam;
    data = dat;
  }
  
  public void writeTo(DataOutputStream dout) 
         throws IOException  {
    dout.writeUTF(name);
    dout.writeInt(data.length);
    for (int i=0; i<data.length; i++) dout.writeDouble(data[i]);
  }
  
  public Obs readFrom(DataInputStream din) 
         throws IOException {
    name = din.readUTF();
    int n = din.readInt();
    data = new double[n];
    for (int i=0; i<n; i++) data[i] = din.readDouble();
    return this;
  }
  
  public void show() {
    System.out.println(name);
    for (int i=0; i<data.length; i++) System.out.print(data[i] + " ");
    System.out.println("");
  }
}
  


class BinDat {
	
  public static void main(String args[]) {
    double[] a = { 1, 2, 3, 4 };
    double[] b = { 7, 8, 9, 10 }; 
    
    Obs obsA = new Obs("Dane A", a);
    Obs obsB = new Obs("Dane B", b);
    
    obsA.show();
    obsB.show();
    
    try {
      DataOutputStream out = new DataOutputStream(
                                 new FileOutputStream("dane")
                                 );
      obsA.writeTo(out);
      obsB.writeTo(out);
      out.close();
      
      DataInputStream in = new DataInputStream(
                                 new FileInputStream("dane")
                                 );
      new Obs().readFrom(in).show();
      new Obs().readFrom(in).show();                                 
      in.close();
      } catch (IOException exc) { 
        exc.printStackTrace(); 
        System.exit(1); 
        }                                   
    
    
  }
}
