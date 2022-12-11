
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class DistributedDatabase {
    private final int port;

    public DistributedDatabase(int port) {
        this.port = port;
    }

    private class NewNode extends Thread {
        private final DatagramSocket nodeSocket;
        private final int destinationPort;
        private final InetAddress destinationAddress;


        public NewNode(InetAddress destinationAddress, int destinationPort) {
            super("Node");

            try {
                this.destinationPort = destinationPort;
                this.destinationAddress = destinationAddress;

                nodeSocket = new DatagramSocket(port);
            } catch (SocketException e) {
                System.out.println("Socket Error");
                throw new RuntimeException(e);
            }
        }

        @Override
        public void run() {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public String receiveMsg() {
            byte[] buf = new byte[100];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            try {
                nodeSocket.receive(packet);
            } catch (IOException e) {
                System.out.println("No IO");
                System.exit(-1);
            }

            return new String(packet.getData(), 0, packet.getLength());
        }

        public void sendMsg(String msg) {
            byte[] buf = msg.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, destinationAddress, destinationPort);

            try {
                nodeSocket.send(packet);
            } catch (IOException e) {
                System.out.println("No IO");
                System.exit(-1);
            }
        }
    }

    private static class NewClient extends Thread {
        private final Socket clientSocket;

        public NewClient(Socket socket) {
            super("Client");
            this.clientSocket = socket;
        }

        public void run() {
            try {
                BufferedReader read = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter write = new PrintWriter(clientSocket.getOutputStream(), true);


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

    public void listenSocket() {
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
            } catch (IOException e) {
                System.out.println("Accept failed");
                System.exit(-1);
            }

            recognizeSocket(client);
        } //listens for new connection
    }

    private void recognizeSocket(Socket socket) {
        try {
            BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter write = new PrintWriter(socket.getOutputStream(), true);

            switch (read.readLine()) {
                case "Node" -> {
                    new NewNode(socket.getInetAddress(), socket.getPort()).start();
                    System.out.println("New node");
                }
                case "Client" -> {
                    new NewClient(socket).start();
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


    public static void main(String[] args) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(args[0], Integer.parseInt(args[1])));

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("Node");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DistributedDatabase server = new DistributedDatabase(Integer.parseInt(args[1]));
        server.listenSocket();
    }

}