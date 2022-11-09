
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPServer {
    private static int numberOfClients;
    private final List<Integer> listOfClientPorts;

    public TCPServer() {
        this.listOfClientPorts = new ArrayList<>();
    }

    public static class ServerThread extends Thread {
        private final Socket socketClient;


        public ServerThread(Socket socket) {
            super();
            this.socketClient = socket;
        }


        public void run() {
            try {
                BufferedReader read = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
                PrintWriter write = new PrintWriter(socketClient.getOutputStream(), true);

                String odp = read.readLine();

                while (odp != null) {
                    System.out.println(odp);

                    /*
                     * code here
                     */

                    odp = read.readLine();
                } //if odp is null that means client has disconnected, so loop end and socketClient can be closed

                socketClient.close();
                System.out.println("client closed");
            } catch (IOException e) {
                // if there is any problem during connection, the socketClient need to be closed
                try {
                    socketClient.close();
                    System.out.println("client closed");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

    }



    public synchronized void listenSocket(int port) {
        ServerSocket server = null;
        Socket client = null;

        try {
            server = new ServerSocket(port);
            System.out.println("Server listens on port: " + server.getLocalPort());
        } catch (IOException e) {
            System.out.println("Could not listen");
            System.exit(-1);
        }


        while (true) {
            try {
                client = server.accept();

                if (check(client.getLocalPort())) {
                    numberOfClients++;
                    listOfClientPorts.add(client.getPort());
                    System.out.println("Numbre of Clients: " + numberOfClients);
                } // counts new clients

            } catch (IOException e) {
                System.out.println("Accept failed");
                System.exit(-1);
            }

            new ServerThread(client).start();
        } //listens for new clients


    }

    public boolean check(int clientPort) {
        for (Integer item : listOfClientPorts) {
            if (item == clientPort)
                return false;
        }
        return true;
    } //checks if client port already is in array of connected clients

    public static void main(String[] args) {

        int port = 1122;

        TCPServer server = new TCPServer();
        server.listenSocket(port);
    }

}
