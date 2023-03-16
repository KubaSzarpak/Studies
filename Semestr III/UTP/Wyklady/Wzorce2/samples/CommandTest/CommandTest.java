import java.util.*;
import javax.swing.*;

public class CommandTest {

  public CommandTest() {
    String req = JOptionPane.showInputDialog("Podaj zlecenie do wykonania");
    List resList = serviceRequest(req);
    for (Object o : resList) {
      if (o.getClass().isArray()) {
        Object[] arr = (Object[]) o;
        for (int i=0; i<arr.length; i++) System.out.println(arr[i]);
      } else System.out.println(o);
    }

  }

  // Metoda obs³ugi zeleceñ
  // niezale¿na od konkretnych zeleceñ

  public List serviceRequest(String req) {
    Command cmd = null;
    try {
      Class klasa = Class.forName(req);
      cmd = (Command) klasa.newInstance();
      String data = JOptionPane.showInputDialog(
                    "-------------------  Podaj parametry -----------------");
      StringTokenizer st = new StringTokenizer(data, "()");
      while (st.hasMoreTokens()) {
        String para = st.nextToken();
        String[] parm = para.split("#");
        cmd.setParameter(parm[0], parm[1]);
      }
      cmd.execute();
    } catch (Exception exc) { exc.printStackTrace(); }
    return cmd.getResults();
  }





  public static void main(String[] args) {
     new CommandTest();
  }


}