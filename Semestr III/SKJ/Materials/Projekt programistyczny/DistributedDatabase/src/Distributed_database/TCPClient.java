package Distributed_database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient extends Thread {
    private final Socket clientSocket;
    private final DatabaseNode node;

    public TCPClient(Socket socket, DatabaseNode node) {
        super("Client");
        this.node = node;
        this.clientSocket = socket;
    }

    public void run() {
        try {
            BufferedReader read = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter write = new PrintWriter(clientSocket.getOutputStream(), true);


            String request = read.readLine();
            String response = node.operate(request);
            write.println(response);


            clientSocket.close();
            System.out.println("client closed");
        } catch (IOException e) {
            // if there is any problem during connection, the socketClient need to be closed
            try {
                clientSocket.close();
                System.out.println("client closed");
//                e.printStackTrace();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
