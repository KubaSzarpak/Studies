package Code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketListener extends Thread {
    private final DatabaseNodeCenter node;

    public SocketListener(DatabaseNodeCenter node) {
        this.node = node;
    }

    @Override
    public void run() {
        ServerSocket server = null;
        Socket client = null;

        try {
            server = new ServerSocket(node.PORT);
            System.out.println("Server listens on port: " + server.getLocalPort());
        } catch (IOException e) {
            System.out.println("Could not listen");
            System.exit(-1);
        }


        while (true) {
            try {
                client = server.accept();
            } catch (IOException e) {
                System.out.println("Accept failed");
                System.exit(-1);
            }

            recognizeSocket(client);
        } //listens for new connection
    }

    private synchronized void recognizeSocket(Socket socket) {
        try {
            BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter write = new PrintWriter(socket.getOutputStream(), true);

            switch (read.readLine()) {
                case "Node" : {
                    int nodePort = Integer.parseInt(read.readLine());

                    switch (node.operate("find-port " + nodePort)) {
                        case "OK":
                            write.println(node.getNextNodePort());
                            write.println(node.getNextNodeAddress().getHostAddress());
                            node.setNextNodeAddress(socket.getInetAddress());
                            node.setNextNodePort(nodePort);
                            System.out.println("Node connected");
                            break;
                        case "ERROR":
                            write.println("ERROR");
                            write.println("PORT ALREADY CONNECTED");
                            System.out.println("Node rejected");
                            break;
                    }

                    socket.close();
                }
                    break;
                case "Client" : {
                    new TCPClient(socket, node).start();
                    System.out.println("Client connected");
                }
                    break;

                default : {
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
