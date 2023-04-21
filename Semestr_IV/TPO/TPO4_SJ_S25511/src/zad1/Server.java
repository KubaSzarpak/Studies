/**
 * @author Szarpak Jakub S25511
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class Server {
    private ServerSocketChannel server;
    private Selector selector;
    private ServerThread serverThread;

    public Server(String host, int port) {
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.socket().bind(new InetSocketAddress(host, port));

            selector = Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }


        serverThread = new ServerThread(server, selector);
    }

    public void startServer() {
        serverThread.start();
    }

    public void stopServer() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        serverThread.running = false;
        serverThread.interrupt();
    }

    public String getServerLog() {
        return serverThread.getServerLog();
    }
}
