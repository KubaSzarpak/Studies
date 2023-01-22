import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;

/**
 * This class is a thread which listens for incoming nodes or clients.
 *
 * @author Jakub Szarpak
 */
public class SocketListener extends Thread {
    private final DatabaseNodeCenter node;

    public SocketListener(DatabaseNodeCenter node) {
        this.node = node;
    }

    /**
     * Run method from Runnable interface. In this method TCP server is created which listens for incoming Sockets.
     * Then it sends them to recognizeSocket() method.
     */
    @Override
    public void run() {
        ServerSocket server = null;
        Socket socket = null;

        try {
            server = new ServerSocket(node.PORT);
            System.out.println("Server listens on port: " + server.getLocalPort());
        } catch (IOException e) {
            System.out.println("Could not listen");
            System.exit(-1);
        }


        while (true) {
            try {
                socket = server.accept();
                recognizeSocket(socket);
            } catch (IOException e) {
                System.out.println("Accept failed");
                System.exit(-1);
            }

        } //listens for coming TCP connections
    }

    /**
     * This method recognizes Sockets by flags that they are sending.
     * If first flag is "Node" then it means this is a node which wants to connect to this node.
     * Then another flag is considered. This flag is a port of node that wants to connect. This flag is sent to operate() method on node field.
     * If result of this method is "OK" then is means that node can be connected. Then next node's port and address which should be port and ip address of first node of distributed database are sent to this node and his are assigned in their place. Then "Node connected" is printed.
     * If result of this method is "ERROR" then it means the node can not be connected. "ERROR", "PORT ALREADY CONNECTED" messages are sent back and "Node rejected" is printed.
     * If first flag is "Client" then node's new TCPClient class is created to handle this client. Then "Client connected is printed"
     * If first flag does not match then "Not recognized" message is sent back and "Not recognized socket :: ip = [that socket ip address]" is printed.
     *
     * @param socket socket that communicates with TCP server.
     */
    private synchronized void recognizeSocket(Socket socket) {
        try {
            BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter write = new PrintWriter(socket.getOutputStream(), true);

            switch (read.readLine()) {
                case "Node": {
                    int nodePort = Integer.parseInt(read.readLine());
                    write.println(node.getNextNodePort());
                    write.println(node.getNextNodeAddress().getHostAddress());
                    node.setNextNodeAddress(socket.getInetAddress());
                    node.setNextNodePort(nodePort);
                    System.out.println("Node connected");

                    socket.close();
                }
                break;
                case "Client": {
                    System.out.println("Client connected");

                    String requestMessage = read.readLine();
                    node.addOperateThread(requestMessage, write);
                }
                break;

                default: {
                    write.println("Not recognized");
                    System.out.println("Not recognized socket :: ip = " + socket.getInetAddress());
                    socket.close();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
