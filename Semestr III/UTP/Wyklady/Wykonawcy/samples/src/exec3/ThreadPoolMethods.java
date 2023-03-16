package exec3;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.*;
import javax.swing.*;
import java.lang.reflect.*;


class Code implements Callable<Integer> {
  
  String name;
  int num;
  
  public Code(String n, int sleep) {
     name = n;
     num = sleep;
  }
  
  public Integer call() throws Exception {
    Thread.sleep(num*1000);
    return num;
  }
  
  public String getName() { return name; }
}


class Task extends FutureTask<Integer> {
  Code code;
  
  public Task(Code c) {
    super(c);
    code = c;
  }
  
  public String getName() { 
    return code.getName();
  }
}
  
  

public class ThreadPoolMethods extends JFrame implements ActionListener {
  
  ThreadPoolExecutor exec = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
  
  public ThreadPoolMethods() {
    setLayout(new FlowLayout());
    JButton b = new JButton("Start");
    b.addActionListener(this);
    add(b);
    b = new JButton("Show");
    b.addActionListener(this);
    add(b);
    pack();
    setVisible(true);
  }
  
  public void actionPerformed(ActionEvent e) {
    String cmd = e.getActionCommand();
    if (cmd.equals("Start")) {
      int sleep = 5, step = 3;
      for (int k=1; k <= 10; k++) {
        sleep += step;
        exec.execute(new Task(new Code("Task " + k, sleep)));
      }
      System.out.println("All tasks submitted");
    }
    else {
      try {
        printStat();
      } catch(Exception exc) { exc.printStackTrace(); }
      BlockingQueue<Runnable> que = exec.getQueue();
      System.out.println("Que size " + que.size());
      for (Runnable r : que) {
        Task t = (Task) r;
        System.out.println(t.getName());
      }
    }
  }
    
  void printStat() throws Exception {
    Method[] mets = ThreadPoolExecutor.class.getDeclaredMethods();
    for (Method m : mets) {
      String name = m.getName();
      if (name.startsWith("get")) {
        Object res = null;  
        try {
          res = m.invoke(exec);
        } catch(Exception exc) {
          System.out.println("Unable to call " + name);
        }
        System.out.println(name + " = " + res);
      }
    }
  }

  public static void main(String[] args) {
    new ThreadPoolMethods();
  }





 
}
