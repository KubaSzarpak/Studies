import java.awt.*;
import javax.swing.*;

public class KontKomp extends JFrame {

  public KontKomp() {
    setLayout(new FlowLayout());
    JPanel p1 = new JPanel();
    p1.add(new JButton("B1"));
    p1.add(new JButton("B1"));
    JPanel p2 = new JPanel();
    p2.add(new JButton("BUTT3"));
    p2.add(new JButton("BUTT4"));
    add(p1);
    add(p2);
  }

  public void setBackground(JComponent jc, Color color) {
    jc.setBackground(color);
    java.awt.Component[] components = jc.getComponents();
    for (java.awt.Component c : components) setBackground( (JComponent) c, color);
  }



  public static void main(String[] args) {
    KontKomp kont = new KontKomp();
    kont.pack();
    kont.show();
    try { Thread.sleep(2000); } catch(Exception exc) {}
    kont.setBackground((JComponent) kont.getContentPane(), Color.yellow);
  }


}