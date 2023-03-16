package locksIntro;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

class StringTab {
  
  private String[] txt;
  private Lock lock = new ReentrantLock();
  

  public StringTab(String[] txt) {
    this.txt = txt;
  }
  
  public void set(int i, String s) {
    lock.lock();
    txt[i] = s;
    lock.unlock();
  }
  
  public String get(int i) {
    String t = null;
    lock.lock();
    t = txt[i];
    lock.unlock();
    return t;
  }
  
  public String get1(int i) {
    lock.lock();
    try {
      return txt[i];
    } finally {
        lock.unlock();
    }
  }
 
}


public class Test1 {
  public static void main(String[] args) {
    
    final StringTab st = new StringTab(new String[] { "ala", "kot", "pies" });
    System.out.println(st.get1(3));
    new Thread( new Runnable() {
      public void run() {
        st.set(3, "tygrys");
      }
    }). start();

    new Thread( new Runnable() {
      public void run() {
        try {
          TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println("W¹tek 2 dzia³a");
        System.out.println(st.get(0));
        System.out.println("W¹tek 2 siê koñczy");
      }
    }). start();
  }
}




