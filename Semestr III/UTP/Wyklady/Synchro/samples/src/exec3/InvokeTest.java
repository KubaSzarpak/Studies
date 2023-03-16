package exec3;

import java.util.concurrent.*;
import java.util.*;

class Eval implements Callable<Integer> {
  
  Integer num;
  
  public Eval(int n) {
    num = n;
  }
  
  public Integer call() throws Exception {
    Thread.sleep(1000);
    return num;
  }
  
}
    
public class InvokeTest {
   
  public static int sum(ExecutorService exec, List<Callable<Integer>> tasks) throws Exception {
    long start = System.currentTimeMillis();
    System.out.println("Start");
    List<Future<Integer>> results =  exec.invokeAll(tasks);  // InterruptedException
    long elapsed = System.currentTimeMillis() - start;
    System.out.println("End after " + elapsed/1000 + " sec.");
    int sum = 0;
    for (Future<Integer> r : results) sum += r.get();
    return sum;
  }
    
  public static void main(String[] args) {
    List<Callable<Integer>> taskList = new ArrayList<Callable<Integer>>();
    ExecutorService exec = Executors.newFixedThreadPool(10);
    for (int i=1; i <=5; i++) {
      Callable<Integer> task = new Eval(i);
      taskList.add(task);
    }
    try {
      int result = sum(exec, taskList);
      System.out.println("Wynik: " + result);
    } catch(Exception exc) { exc.printStackTrace(); }
  }

}
