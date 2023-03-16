import java.io.*;

public class TxtWriter extends Thread {  // Klasa przepisywacza

  private LineNumberReader in;   // strumieñ sk¹d czyta
  private SpaceToWrite spw;      // miejsce gdzie pisze
    
  public TxtWriter(String name,      // nazwa przepisywacza
                   Reader in_,       // z jakiego strumeinia czyta 
                   SpaceToWrite spw_ // gdzie pisze 
                   ) 
  {
    super(name);
    in = new LineNumberReader(in_);  // filtrowanie strumienia
                                     // by mieæ numery wierszy 
    spw = spw_;                                     
  }
  
  // Kod w¹tku przepisywacza
  // czyta wiersze ze strumienia wejœciowego
  // i zapisuje je w miejscu oznaczanym spw (SpaceToWrite) 
  // dopóki nie nadszedl sygna³ o koñcu pracy (tekst "Koniec pracy")
  public void run() {
    spw.writeLine(" *** " + getName() + " rozpocz¹³ pracê" + " ***");
    spw.writeLine("---> czekam na teksty !");
    String txt;
    try {
      txt = in.readLine();
      while(!txt.equals("Koniec pracy")) { 
        spw.writeLine(in.getLineNumber() + " " + txt);
        txt = in.readLine();
      }
      in.close();
      spw.writeLine("**** " + getName() + " skoñczy³ pracê"); 
    } catch(IOException exc) {
        spw.writeLine("****" + getName() + " - zakonczenie na skutek bledu");
        exc.printStackTrace();
        return;
    }
  }
}        
    
        