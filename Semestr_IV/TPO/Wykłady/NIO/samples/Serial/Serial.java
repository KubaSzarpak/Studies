import java.io.*;
import java.util.*;

class Serial {
	
  public static void main(String args[]) {
    
    Date data = new Date();
    int[] temperatura = { 19, 25 , 22};
    String[] opis = { "dzieñ", "noc", "woda" };

    // Zapis    
    try {

      ObjectOutputStream out = new ObjectOutputStream(
                                 new FileOutputStream("test.ser")
                                 );
      out.writeObject(data);
      out.writeObject(opis);
      out.writeObject(temperatura);
      out.close();
    } catch(IOException exc) {
        exc.printStackTrace();
        System.exit(1);
    }

    // Odtworzenie (zazwyczaj w innym programie)
    try {
      ObjectInputStream in = new ObjectInputStream(
                                 new FileInputStream("test.ser")
                                );
      Date odczytData = (Date) in.readObject();
      String[] odczytOpis = (String[]) in.readObject();
      int[] odczytTemp = (int[]) in.readObject();
      in.close();
      System.out.println(String.valueOf(odczytData));
      for (int i=0; i<odczytOpis.length; i++) 
          System.out.println(odczytOpis[i] + " " + odczytTemp[i]);       

    } catch(IOException exc) {
        exc.printStackTrace();
        System.exit(1);
    } catch(ClassNotFoundException exc) {
        System.out.println("Nie mo¿na odnaleŸæ klasy obiektu"); 
        System.exit(1);
    } 
    
  }
 
} 
