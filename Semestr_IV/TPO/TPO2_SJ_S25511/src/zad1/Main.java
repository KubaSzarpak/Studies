/**
 *
 *  @author Szarpak Jakub S25511
 *
 */

package zad1;


import javax.swing.*;

public class Main {
  public static void main(String[] args) {
    Service s = new Service("Poland");
    String weatherJson = s.getWeather("Warsaw");
    Double rate1 = s.getRateFor("USD");
    Double rate2 = s.getNBPRate();
    // ...
    // część uruchamiająca GUI
    SwingUtilities.invokeLater(Frame::new);
    
  }
}
