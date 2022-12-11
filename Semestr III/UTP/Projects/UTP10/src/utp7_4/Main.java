/**
 * @author Szarpak Jakub S25511
 */

package utp7_4;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class Main {
    private static ExecutorService executor;
    public static class MyRunnable implements Runnable {
        Socket client;
        public MyRunnable(Socket socket) {
            this.client = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader read = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter write = new PrintWriter(client.getOutputStream(), true);
                while (client.isConnected()) {
                    write.println(read.readLine());
                }

                System.out.println("Client closed");
            } catch (IOException e) {
                try {
                    client.close();
                    System.out.println("Connection problem");
                    System.out.println("Client closed");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                throw new RuntimeException(e);
            }
        }
    }

    public synchronized void listenSocket(int port) {
        MyCloseThread myClose = new MyCloseThread(executor);
        myClose.start();

        ServerSocket server = null;
        Socket client;

        try {
            server = new ServerSocket(port);
            System.out.println("Server listens on port: " + server.getLocalPort());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        while (!executor.isShutdown()) {
            try {
                client = server.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (!executor.isShutdown()) //checks if executor is shutdown during while loop
                executor.execute(new FutureTask<>(new MyRunnable(client), true));
        }
    }

    public static void main(String[] args) {
        executor = Executors.newFixedThreadPool(4);
        int port = 4445;

        Main server = new Main();

        server.listenSocket(port);
    }
}
