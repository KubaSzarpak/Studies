// Klasa, okreœlaj¹ca przestrzenie 
// na których pisz¹ przepisywacze
// oraz grupuj¹ca te przestrzenie w oknie.
// Ka¿dy przepisywacz wypisuje tekst 
// do wielowierszowego pola edycyjnego (TextArea z pakietu AWT)
// do czego s³u¿y mu metoda writeLine.
// Wszystkie przestrzenie grupowane s¹ w oknie frame.

import java.awt.*;
import java.awt.event.*;

public class SpaceToWrite extends TextArea {
  
  private static Frame frame = new Frame("Write space");
  
  // Konstruktor: tworzy now¹ przetrzeñ pisania dla jednego przepisywacza
  public SpaceToWrite(int rows, int cols) {
    super(rows, cols);  // utworzenie TextArea  - z podan¹ liczb¹ wierszy, kolumn 
    frame.add(this);    // dodanie TextArea do okna
  }
  
  // Metoda dopisuj¹ca nowy wiersz do textarea
  public void writeLine(String s) {
    this.append(s + '\n');
  }

  // Metoda ustalaj¹ca u³o¿enie pól edycyjnych w oknie
  // rozmiar okna (pack daje rozmiar taki jak akurat potrzreba)
  // i pokazuj¹ca okno    
  public static void show(int numWriters) {
    frame.setLayout(new GridLayout(0, numWriters));  
    frame.pack();
    frame.show();
    
    // Umo¿liwienie zakoñczenia aplikacji poprzez zamkniêcie okna
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        frame.dispose();
        System.exit(1);
      }
    });
  }  

  
}