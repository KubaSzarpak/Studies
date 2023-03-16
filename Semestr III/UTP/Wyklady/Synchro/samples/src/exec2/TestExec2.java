package exec2;
import java.awt.event.*;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.*;

import javax.swing.*;



class ResultHandler<V> {
  
  int x = 400, y = 50;
  public void handleResult(V result, Exception exc) {
    String msg;
    if (exc != null) msg = exc.toString();
    else msg = "Wynik = " + result;
    JFrame f = new JFrame("Task results");
    JLabel lab = new JLabel("        " + msg);
    f.add(lab);
    f.setBounds(x, y, 300, 100);
    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    x+=50;
    y+=50;
    f.setVisible(true);
    
  }
}  
  


public class TestExec2 extends JFrame implements ActionListener {
  
  int k = 0;
  int n = 15;
  JTextArea ta = new JTextArea(40,20);
  
  private void append(String txt) {
    ta.append(txt);
    ta.setCaretPosition(ta.getDocument().getLength());
  }
  
  TestExec2() {
    add(new JScrollPane(ta));
    JPanel p = new JPanel();
    JButton b = new JButton("Start");
    b.addActionListener(this);
    p.add(b);
    b = new JButton("Cancel last submitted");
    b.setActionCommand("Stop");
    b.addActionListener(this);
    p.add(b);    
    b = new JButton("Last submitted result");
    b.setActionCommand("Result");
    b.addActionListener(this);
    p.add(b);    
    b = new JButton("Shutdown");
    b.addActionListener(this);
    p.add(b);
    b = new JButton("ShutdownNow");
    b.addActionListener(this);
    p.add(b);
    add(p, "South");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setVisible(true);
  }
  
    
  public void actionPerformed(ActionEvent e)  {
    String cmd = e.getActionCommand();
    try {
      Method m = this.getClass().getDeclaredMethod("task"+cmd);
      m.invoke(this);
    } catch(Exception exc) { exc.printStackTrace(); }
  }

  class SumTaskA extends AbstractTask<Integer> {
    
    public SumTaskA(String taskName, ResultHandler h, String handlerMethod ) throws Exception {
      super(taskName, h, handlerMethod);
    }
    
    public Integer call() throws Exception {
      Future<Integer> task = getTask();
      int sum = 0;
      if (task.isCancelled()) return null;
      for (int i = 1; i <= 10; i++) {
        if (task.isCancelled()) break;         
        sum+=i;
        append(getName() + " part result = " + sum + '\n');
        Thread.sleep(1000);
        //throw new Exception("a tak siê przerwa³o...");
      }                                    
      return sum;
    }
  };
  
  FutureTask<Integer> task;
  
  //ExecutorService exec = Executors.newSingleThreadExecutor();
  ExecutorService exec = Executors.newFixedThreadPool(3);
  
  ResultHandler<Integer> handler = new ResultHandler<Integer>();
  
  public void taskStart() {
    try {
      k++;
      SumTaskA stask = new SumTaskA("Task " + k, handler, "handleResult");
      task = stask.getTask();
      exec.execute(task);
    } catch(RejectedExecutionException exc) {
        append("Execution rejected\n");
        return;
    }
    catch(Exception exc) {
      append(exc.getMessage()+"\n");
    }
    append("Task " + k + " submitted\n");
  }
  
  public void taskResult() {
    String msg = "";
    if (task.isCancelled()) msg = "Task cancelled.";
    else if (task.isDone()) {
      try {
        msg = "Ready. Result = " + task.get();
      } catch(Exception exc) {
          msg = exc.getMessage();
      }
    }
    else msg = "Task is running or awaiting.";
    JOptionPane.showMessageDialog(null, msg);
  }
  
  public void taskStop() {
    task.cancel(true);
  }
  
  public void taskShutdown() {
    exec.shutdown();
    append("Executor shutdown\n");
  }
  
  public void taskShutdownNow() {
    List<Runnable> awaiting = exec.shutdownNow();
    append("Eeecutor shutdown now - awaiting tasks\n");
    for (Runnable r : awaiting) { 
      append(r.getClass().getName() +'\n');
    }
      
 }
    
  
  public static void main(String[] args) {
     new TestExec2();
  }

}
  
  