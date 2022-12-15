package Distributed_database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class SocketListener extends Thread {
    private final DatabaseNode node;

    public SocketListener (DatabaseNode node) {
        this.node = node;
    }

    @Override
    public synchronized void run() {
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
                case "Node" -> {
                    node.setNextNodeAddress(socket.getInetAddress());
                    node.setNextNodePort(4446);
                }
                case "Client" -> {
                    new TCPClient(socket, node).start();

                }

                default -> {
                    write.println("Not recognized");
                    System.out.println("Not recognized socket :: ip = " + socket.getInetAddress());
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
