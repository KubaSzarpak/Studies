package demo1;
import javax.swing.*;

interface MsgDisplay {
   void show(String msg);
}

// Mamy dwie rózne implementacje

class ConsoleDisplay implements MsgDisplay {
   public void show(String s) {
     System.out.println(s);
   }
}

class DialogDisplay implements MsgDisplay {
  public void show(String s) {
    JOptionPane.showMessageDialog(null, s );
  }
}


public class FactoryTest {

  public FactoryTest() {
    badClient();
    goodClient();
  }

  public void badClient() {
    MsgDisplay msg = new ConsoleDisplay(); // w wielu miejscach w kodzie!
    msg.show("Bad");                        // wiele zmian
  }

  public void goodClient() {
    MsgDisplay msg = MsgDisplayFactory.getInstance(); // tu nie ma zmian
    msg.show("Good");
  }

  public static void main(String[] args) {
     new FactoryTest();
  }

}

class MsgDisplayFactory {

  public static MsgDisplay getInstance() {
    return new DialogDisplay();
  }

}