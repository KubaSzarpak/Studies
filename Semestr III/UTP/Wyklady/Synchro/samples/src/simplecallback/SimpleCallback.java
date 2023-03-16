package simplecallback;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.*;


class  FutureTaskCallback<V> extends  FutureTask<V> {
  
  public FutureTaskCallback(Callable<V> callable) {
    super(callable);
  }
  
  public void done() {
    String result = "Wynik: ";
    if (isCancelled()) result += "Cancelled.";
    else try {
      result += get();
    } catch(Exception exc) {
        result += exc.toString();
    }
    JOptionPane.showMessageDialog(null, result);
  }
  
}


public class SimpleCallback extends JFrame {
  
  JTextField input = new JTextField(40),  
             progress = new JTextField(40);
  
  String toReverse;
  
  Callable<String> reverseTask = new Callable<String>() {
    public String call() throws Exception {
      Thread t = Thread.currentThread();
      if (toReverse == null || toReverse.trim().equals("")) 
        throw new IllegalArgumentException("Set string to reverse");
      if (t.isInterrupted()) return null;
      char[] org = toReverse.toCharArray();
      StringBuffer out = new StringBuffer();
      if (t.isInterrupted()) return null;
      for (int i = org.length-1; i>=0; i--) {
        Thread.sleep(500);
        out.append(org[i]);
        if (t.isInterrupted()) return null;
        progress.setText(out.toString());
        if (t.isInterrupted()) return null;
      }
      return out.toString();
    }
  };

  ExecutorService exec = Executors.newSingleThreadExecutor();
  FutureTaskCallback<String> ft;
    
  
  public SimpleCallback() {
    Font f = new Font("Dialog", Font.PLAIN, 16);
    input.setFont(f);
    input.setBorder(BorderFactory.createTitledBorder("Tu wpisz tekst do odwrócenia"));
    progress.setFont(f);
    progress.setBorder(BorderFactory.createTitledBorder("Tu bêd¹ widoczne wyniki odwracania"));
    JPanel p = new JPanel();
    JButton b = new JButton("Start");
    b.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ft = new FutureTaskCallback<String>(reverseTask);
        toReverse = input.getText();
        exec.execute(ft);
      }
    });
    p.add(b);
    b = new JButton("Stop");
    b.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (ft != null) ft.cancel(true);
      }
    });
    p.add(b);
    Container cp = getContentPane();
    cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
    add(input);
    add(p);
    add(progress);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }
    
    
    
    

  public static void main(String[] args) {
     SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new SimpleCallback();
      }
    });
  }

}
