
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
    public static int numberOfAnswers;
    private final List<Integer> listOfClientPorts;

    public TCPServer() {
        this.listOfClientPorts = new ArrayList<>();
        numberOfAnswers = 0;
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
                StringBuilder clientsGivenNumber = new StringBuilder();

                String odp = read.readLine();
                clientsGivenNumber.append(odp);
                odp = read.readLine();
                clientsGivenNumber.append(odp);
                odp = read.readLine();
                clientsGivenNumber.append(odp);

                if (odp != null)
                    numberOfAnswers++;

                write.println(clientsGivenNumber);
                write.println(numberOfAnswers);

                System.out.println("Liczba odpowiedzi - krok pierwszy: " + numberOfAnswers);

                int[] arr = new int[2];

                arr[0] = Integer.parseInt(read.readLine());
                odp = read.readLine();
                arr[1] = Integer.parseInt(odp);

                if (odp != null)
                    numberOfAnswers++;


                System.out.println("Liczba odpowiedzi - krok drugi: " + numberOfAnswers);

//                System.out.println("tablica: " + arr[0] + ", " + arr[1] + "; port: " + socketClient.getPort());
//                System.out.println("NWD: " + NWD(arr[0], arr[1]) + "; port: " + socketClient.getPort());

                write.println(NWD(arr[0], arr[1]));
                write.println(numberOfAnswers);

                System.out.println("flaga: " + read.readLine());



                /*while (odp != null) {
                 *//*
                 * code here
                 *//*
                    odp = read.readLine();
                }*/
                /*
                 * this is used if you want to manualy contact with client
                 * if odp is null that means client has disconnected, so loop end and socketClient can be closed
                 */


                socketClient.close();
//                numberOfClients--;
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
        public synchronized static int NWD(int pierwsza, int druga)
        {
            if (druga == 0)
            {
                return pierwsza;
            }
            else {
                return NWD(druga, pierwsza%druga);  // dwóch liczb.
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

                if (check(client.getPort())) {
                    numberOfClients++;
                    listOfClientPorts.add(client.getLocalPort());
                    System.out.println("Number of Clients: " + numberOfClients);
                } else {
                    System.out.println("Client with this port already was connected"); // counts new clients
                }

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
    } //checks if client port already is in list of connected clients

    public static void main(String[] args) {

        int port = 1122;

        TCPServer server = new TCPServer();
        server.listenSocket(port);
    }

}