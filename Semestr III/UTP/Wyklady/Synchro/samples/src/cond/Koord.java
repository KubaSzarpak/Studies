package cond;

import java.util.concurrent.locks.*;

class Teksty {
  
  Lock lock = new ReentrantLock();
  Condition txtWritten = lock.newCondition();
  Condition txtSupplied = lock.newCondition();

  String txt = null;
  boolean newTxt = false;

  // Metoda ustalaj¹ca tekst - wywo³uje Autor
  void setTextToWrite(String s) {
    lock.lock();
    try {
      if (txt != null) {
        while (newTxt == true) 
          txtWritten.await();
      }
      txt = s;
      newTxt = true;
      txtSupplied.signal();
    } catch (InterruptedException exc) {  
    } finally {
         lock.unlock();
    }
  }
    

  // Metoda pobrania tekstu - wywo³uje Writer
   String getTextToWrite() {
     lock.lock();  
     try {
       while (newTxt == false) 
         txtSupplied.await(); // mo¿e byæ Interrupted
       newTxt = false;
       txtWritten.signal();
       return txt;
     } catch (InterruptedException exc) {
         return null;
     } finally {
         lock.unlock();
     }
   }
     

}

// Klasa "wypisywacza"
class Writer extends Thread {

  Teksty txtArea;

  Writer(Teksty t) {
    txtArea=t;
  }

  public void run() {
    String txt = txtArea.getTextToWrite();
    while(txt != null) {
      System.out.println("-> " + txt);
      txt = txtArea.getTextToWrite();
      }
  }

}

// Klasa autora
class Author extends Thread {

  Teksty txtArea;

  Author(Teksty t)  {
    txtArea=t;
  }

  public void run() {

    String[] s = { "Pies", "Kot", "Zebra", "Lew", "Owca", "S³oñ", null };
    for (int i=0; i<s.length; i++) {
      try { // autor zastanawia siê chwilê co napisaæ
        sleep((int)(Math.random() * 1000));
      } catch(InterruptedException exc) { }
      txtArea.setTextToWrite(s[i]);
    }
  }

}

// Klasa testuj¹ca
public class Koord {

   public static void main(String[] args) {
     Teksty t = new Teksty();
     Thread t1 = new Author(t);
     Thread t2 = new Writer(t);
     t1.start();
     try { Thread.sleep(3000); } catch(Exception exc) {}
     t2.start();
   }

}

