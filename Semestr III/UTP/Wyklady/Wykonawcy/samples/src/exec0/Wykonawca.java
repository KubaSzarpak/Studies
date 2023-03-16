package exec0;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.*;
import java.io.*;
import java.nio.channels.*;
import javax.swing.*;


class Task implements Runnable {
  
  private String name;
  
  public Task(String name) {
    this.name = name;
  }
  
  public void run() {
    for (byte i=1; i <= 128 ; i++) {
      System.out.println(name + " " + i);
      Thread.yield();
    }
  }
}
  

public class Wykonawca {
 
  public static void main(String[] args) {
    //new Interruptible();
    //System.exit(1);
    //if (Thread.currentThread().isInterrupted()) return;
    
    ExecutorService exec = Executors.newFixedThreadPool(2);
    for (int i=1; i<=4; i++) {
      exec.execute(new Task("Task " + i));
    }
    try { 
      Thread.sleep(1000); 
    } catch(Exception exc) {}
    
    exec.shutdownNow();
    
    try {
      exec.execute(new Task("Task after shutdown"));
    } catch (RejectedExecutionException  exc) {
        exc.printStackTrace();
    }
    try {
      exec.awaitTermination(5, TimeUnit.SECONDS);
    } catch(InterruptedException exc) { exc.printStackTrace(); }
    System.out.println("Terminated: " + exec.isTerminated());
    
  }
}


class Interruptible  {
  
  Lock lock = new ReentrantLock();
  
  Runnable task1 = new Runnable() {
     public void run() {
       System.out.println("Task 1 begins");
       try {
         lock.tryLock(1000, TimeUnit.SECONDS);
         System.out.println("Task 1 entered");
       } catch(InterruptedException exc) {
           System.out.println("Task 1 interrupted");
//       } finally {
//           lock.unlock(); // uWaga: IllegalMonitorState - proba zwolnienia nieposiadanego locka
       }
       System.out.println("Task 1 stopped");
     }
  };
  
  Runnable task2 = new Runnable() {
    public void run() {
      System.out.println("Task 2 begins");
      for (int i=1; i <= 600; i++) {
        if (Thread.currentThread().isInterrupted()) break;
        // jakieœ obliczenie
        if (Thread.currentThread().isInterrupted()) break;
        try { 
          Thread.sleep(1000);
        } catch (InterruptedException exc) { break; }
      }
      System.out.println("Task 2 stopped");
    }
  };
          
    
  Runnable task3 = new Runnable() {
    Scanner scan = new Scanner(new FileInputStream(FileDescriptor.in).getChannel(), "Cp852");
    public void run() {
      System.out.println("Task 3 begins");
      System.out.print(">>");
      while (scan.hasNextLine()) {
        try {
          String s = scan.nextLine();
          System.out.print('\n'+s + "\n>>");
        } catch (Exception exc) {   // Uwaga: scanner nie zg³asza wyj¹tków, ale przerywa dzialanie
            exc.printStackTrace();
            break;
        }
      }
      System.out.println("Task 3 stopped - " + scan.ioException()); // jaki wyj¹tek go przerwa³?
    }
  };
  
  Interruptible() {
    ExecutorService exec = Executors.newCachedThreadPool();
    
    exec.execute(new Runnable() {        // w¹tek zamyka mutex
                    public void run() {
                      lock.lock();
                    }
                 }
                     );
    exec.execute(task1);
    exec.execute(task2);
    exec.execute(task3);
    JOptionPane.showMessageDialog(null, "Press Ok to stop all tasks");
    exec.shutdownNow();
  }
  
}
    
    
    
      
  
  
       
       
    
       
       
       
       
       
       
    
