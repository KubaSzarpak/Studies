package exec1;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;
import java.util.concurrent.*;
import java.lang.reflect.*;

public class Exec1 extends JFrame implements ActionListener {
  
  int k = 0;
  int n = 15;
  JTextArea ta = new JTextArea(40,20);
  
  Exec1() {
    add(new JScrollPane(ta));
    JPanel p = new JPanel();
    JButton b = new JButton("Start");
    b.addActionListener(this);
    p.add(b);
    b = new JButton("Stop current");
    b.setActionCommand("Stop");
    b.addActionListener(this);
    p.add(b);    
    b = new JButton("Curent result");
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
  
  private void append(String txt) {
    ta.append(txt);
    ta.setCaretPosition(ta.getDocument().getLength());
  }
  
    
  public void actionPerformed(ActionEvent e)  {
    String cmd = e.getActionCommand();
    try {
      Method m = this.getClass().getDeclaredMethod("task"+cmd);
      m.invoke(this);
    } catch(Exception exc) { exc.printStackTrace(); }
  }

  class SumTask implements Callable<Integer> {
  
    private int taskNum, 
                limit;
    
    public SumTask(int taskNum, int limit) {
      this.taskNum = taskNum;
      this.limit = limit;
    }
    
    public Integer call() throws Exception {
      int sum = 0;
      for (int i = 1; i <= limit; i++) {
        if (Thread.currentThread().isInterrupted()) return null;        
        sum+=i;
        append("Task " + taskNum + " part result = " + sum + '\n');
        Thread.sleep(1000);
      }                                    
      return sum;
    }
  };
  
  Future<Integer> task;
  
  //ExecutorService exec = Executors.newSingleThreadExecutor();
  ExecutorService exec = Executors.newFixedThreadPool(3);
  
  public void taskStart() {
    try {
      task = exec.submit(new SumTask(++k, 15));
    } catch(RejectedExecutionException exc) {
        append("Execution rejected\n");
        return;
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
    else msg = "Task is running or waiting for execution";
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
    append("Eeecutor shutdown now - awaiting tasks:\n");
    for (Runnable r : awaiting) { 
      append(r.getClass().getName()+'\n');
    }
      
 }
    
  
  public static void main(String[] args) {
     new Exec1();
  }

}
