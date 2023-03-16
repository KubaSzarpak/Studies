package test1;

import java.util.concurrent.*;


class Task implements Runnable {
  
  private String name;
  
  public Task(String name) {
    this.name = name;
  }
  
  public void run() {
    for (int i=1; i <= 4; i++) {
      System.out.println(name + " " + i);
      Thread.yield();
    }
  }
}
  

public class Wykonawca {
 
  public static void main(String[] args) {
    Executor exec = Executors.newFixedThreadPool(2);
    for (int i=1; i<=4; i++) {
      exec.execute(new Task("Task " + i));
    }
    // Dlaczego program siê nie koñczy?
    // - poniewa¿ w tle nadal dzia³a Wykonawca 
    // i jest gotowy do przyjmowania nowych zadañ
  }
}