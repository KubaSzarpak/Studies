package locksIntro;

import java.util.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.locks.ReentrantReadWriteLock.*;

import javax.swing.*;

class Balance {
  
  private int number = 0;
  private Lock lock = new ReentrantLock();
  
  
  private boolean useLock;
  
  public Balance(boolean ul) {
    useLock = ul;
  }
  
  public int balance(boolean print) {
    if (!useLock) return balanceSynchro(print);
    else return balanceLocked(print);
  }
  
  public int balanceLocked(boolean print) {
    try {
      lock.lock();
      number++;
      if (print) System.out.print("*");
      int w = number/0;
      number--;
 
      return number;  
  } finally {
    
    lock.unlock();      
    } 
  }
  
  public synchronized int  balanceSynchro(boolean print) {
    number++;
    if (print) System.out.print("*");
    number--;
    return number;
  }
  
  
  public int balance1() {
    lock.lock();
    boolean balanced = true;
    try {
      number++;
      balanced = false;
      int jakisWynik = number/0;
      number--;
      balanced = true;
      return number;
    } finally {
        if (!balanced) number--;
        lock.unlock();
    }
    
  }
  
  public String toString() { return ""+number; }
    
  
}  

class BalanceThread extends Thread {
  
  private Balance b;  // referencja do obiektu klasy Balance
  private int count;  // liczba pwotórzeñ pêtli w metodzie run
  private static Lock lock = new ReentrantLock();
    
  public BalanceThread(String name, Balance b, int count) {
    super(name); 
    this.b = b;
    this.count = count;
    start();
  }  

  public void run() {
    int wynik = 0; 
      for (int i = 0; i < count; i++) {
        boolean print;
        if (i%20 == 0) print = true;
        else print = false;
        try {
          wynik = b.balance(print);
        } catch(Exception exc) { }  
        if (wynik != 0) break;
      }
    System.out.println("\n"+ Thread.currentThread().getName() + 
                       " konczy z wynikiem  " + wynik);
    System.out.println("Stan b: " + b);
  }
}  

class BalanceTest {
  
  static ArrayList<String> czasy = new ArrayList<String>();
  
  public static void test(int tnum, boolean lock) {
    
    Balance b = new Balance(lock);
    String wynik = lock ? "Lock " : "Synchro ";
    String id = lock ? "L" : "S"; 
    wynik += tnum;
    // Tworzymy i uruchamiamy w¹tki
    Thread[] thread = new Thread[tnum];
    
    long start = System.currentTimeMillis();
    
    for (int i = 0; i < tnum; i++) 
      thread[i] = new BalanceThread(id +(i+1), b, 100);

    // czekaj na zako½czenie wszystkich w?tk¡w
    try {
      for (int i = 0; i < tnum; i++) thread[i].join(); 
    } catch (InterruptedException exc) { 
      System.exit(1); 
    } 
    wynik += " Czas: " + (System.currentTimeMillis() - start);
    System.out.println(wynik);
    czasy.add(wynik);
    for (int i = 0; i < thread.length; i++) { thread[i] = null; }
    System.gc();
  }
  
  public static void main(String[] args) {
    //Test synchro
    /*for (int i=100; i<=1000; i+=100) {
      test(i, false);
    }*/
    // Test locked
    for (int i=10; i<=10; i+=100) {
      test(i, true);
    }

    
    for (String msg : czasy) { System.out.println(msg);
      
    }
    
    
  }
  

}       