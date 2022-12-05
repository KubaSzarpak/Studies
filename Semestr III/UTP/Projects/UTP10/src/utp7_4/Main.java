/**
 *
 *  @author Szarpak Jakub S25511
 *
 */

package utp7_4;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class Main {

  protected static class newThread extends Thread{

    Socket client;

    public newThread (Socket socket) {
      super();
      this.client = socket;
    }

    @Override
    public void run() {


    }
  }

  protected static class myCallable  implements Callable{

    @Override
    public Object call() throws Exception {
      return null;
    }
  }


  public synchronized newThread listenSocket (int port) {
    ServerSocket socket = null;
    Socket client = null;

    try {
      socket = new ServerSocket(port);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }

    while (true) {
      try {
        client = socket.accept();


      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return new newThread(client);
    }

  }

  public static void main(String[] args) {

    ExecutorService executor = Executors.newFixedThreadPool(4);

//    executor.

    while (true) {
      FutureTask<Integer> f = new FutureTask<>(() -> 0);

    }



  }
}
