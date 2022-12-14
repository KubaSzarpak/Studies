/**
 *
 *  @author Szarpak Jakub S25511
 *
 */

package src.utp8_1;


public class Main {

  public static void main(String[] args) {
    ThreadA A = new ThreadA();

    ThreadB B = new ThreadB(A);

    A.start();
    B.start();
  }
}
