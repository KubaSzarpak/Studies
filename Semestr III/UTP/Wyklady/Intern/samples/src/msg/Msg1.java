package msg;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.*;
import java.util.*;
import java.beans.*;

@SuppressWarnings("serial")
public class Msg1 extends JFrame  {

  private JPanel panel = new JPanel();
  private JTextArea log = new JTextArea(20, 40);
  private String parseErrorPattern;
  private String reportPattern;
  private String exceedPattern;
  private double limit = 1000;
  private double sum;

  public Msg1() {
    panel.add(new JLabel());
    final JTextField tf = new JTextField(10);
    panel.add(tf);
    JButton b = new JButton();
    b.setActionCommand("pl_PL");
    b.addActionListener(locChanger);
    panel.add(b);
    b = new JButton();
    b.setActionCommand("en_GB");
    b.addActionListener(locChanger);
    panel.add(b);
    add(panel, "North");

    localize(Locale.getDefault());

    
    add(log);

    tf.addActionListener( new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            ParsePosition p = new ParsePosition(0);
            String txtNum = tf.getText().trim();
            Number val = NumberFormat.getInstance().parse(txtNum, p);
            String out;
            if (p.getIndex() != txtNum.length())
              out = MessageFormat.format(parseErrorPattern, txtNum);
            else if (sum + val.doubleValue() > limit)
              out = MessageFormat.format(exceedPattern, limit);
            else {
              sum += val.doubleValue();
              Date date = Calendar.getInstance().getTime();
              out = MessageFormat.format(reportPattern, date, val);
            }
            log.append(out+'\n');
          }
       });
    
    // Inicjacja fontów
    Font f = new Font("Dialog", Font.PLAIN, 18);
    log.setFont(f);
    for (Component c : panel.getComponents()) c.setFont(f);

    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private void localize(Locale loc) {
    limit = 1000;
    sum = 0;
    log.setText("");
    Locale.setDefault(loc);
    ResourceBundle info =
                   ResourceBundle.getBundle("msg.Info");
    String[] txt = {
                       info.getString("enterData"),
                       "",
                       info.getString("polish"),
                       info.getString("english"),
                   };
    Component[] c = panel.getComponents();
    for (int i=0; i<c.length; i++) {
      Statement stmt = new Statement(c[i], "setText",
                            new Object[] { txt[i] } );
      try {
        stmt.execute();
      } catch(Exception exc) { exc.printStackTrace(); }
    }
    panel.invalidate();
    parseErrorPattern = info.getString("parseError");
    reportPattern = info.getString("report");
    exceedPattern = info.getString("exceedError");
  }


  ActionListener locChanger =  new ActionListener() {
    public void actionPerformed(ActionEvent e) {
      String symloc = e.getActionCommand();
      String[] locArg = symloc.split("_");
      localize(new Locale(locArg[0], locArg[1]));
    }
  };

  public static void main(String[] args) {
     SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new Msg1();
      }
    });
  }

}