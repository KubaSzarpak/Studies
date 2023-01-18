import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This class is a thread to handle TCP clients that communicated with node.
 * @author Jakub Szarpak
 */
public class TCPClient extends Thread {
    private final Socket clientSocket;
    private final DatabaseNodeCenter node;
    private final BufferedReader read;
    private final PrintWriter write;

    public TCPClient(Socket socket, DatabaseNodeCenter node, BufferedReader read, PrintWriter write) {
        this.node = node;
        this.clientSocket = socket;
        this.read = read;
        this.write = write;
    }

    /**
     * Run method from Runnable interface.
     * It reads clients request, sends it to operate() method on node field and sends back the result.
     * After all closes connection with client.
     */
    public void run() {
        try {

            String request = read.readLine();
            System.out.println(request);
            String response = node.operate(request);
            write.println(response);


            clientSocket.close();
            System.out.println("client closed");
        } catch (IOException e) {
            // if there is any problem during connection, the socketClient need to be closed
            try {
                clientSocket.close();
                System.out.println("client closed");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
