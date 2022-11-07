
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TCPServer {
    private static int numberOfClients;
    private List<Integer> listOfClientPorts;

    public TCPServer() {
        this.listOfClientPorts = new ArrayList<>();
        this.numberOfClients = 0;
    }

    public static class ServerThread extends Thread {
        private final Socket socket;


        public ServerThread(Socket socket, int numberOfClients, List<Integer> listOfClientPorts) {
            super();
            this.socket = socket;
        }

        public ServerThread(Socket socket) {
            super();
            this.socket = socket;
        }


        public void run() {
            try {
                BufferedReader in;
                PrintWriter out;
                while (!socket.isConnected()) {
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(socket.getOutputStream(), true);

                    /*
                    * code here --------------------------------
                    */
                }


//                System.out.println(socket.getRemoteSocketAddress());

            } catch (IOException e1) {
                // do nothing
                System.out.println('1');
            }

            try {
                socket.close();
            } catch (IOException e) {
                // do nothing
                System.out.println('2');
            }
        }

    }

    public synchronized void listenSocket(int port) {
        ServerSocket server = null;
        Socket client = null;
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Could not listen");
            System.exit(-1);
        }

        System.out.println("Server listens on port: " + server.getLocalPort());

        while (true) {
            try {
                client = server.accept();

                if (check(client.getLocalPort())) {
                    numberOfClients++;
                    listOfClientPorts.add(client.getPort());
                    System.out.println("Numbre of Clients: " + numberOfClients);
                }
            } catch (IOException e) {
                System.out.println("Accept failed");
                System.exit(-1);
            }

            new ServerThread(client).start();
        }


    }

    public boolean check(int clientPort) {
        for (Integer item : listOfClientPorts) {
//            System.out.println(item);
            if (item == clientPort)
                return false;
        }
        return true;
    }

    public static void main(String[] args) {

        int port = 1122;

        TCPServer server = new TCPServer();
        server.listenSocket(port);
    }

}
