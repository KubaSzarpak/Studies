import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClient {

    public static void main(String[] args) {


        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        String address = "172.23.65.91";
        int port = 4445;

        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(address, port), 500);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.out.println("Unknown host");
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("No I/O");
            System.exit(-1);
        }

        try {
            out.println(105425);
            System.out.println(in.readLine());
            out.println("172.23.129.66:1122");
            System.out.println(in.readLine());
            in.readLine();
        } catch (IOException e) {
            System.out.println("Error during communication");
            System.exit(-1);
        }


        try {
            socket.close();
            System.out.println("Scocet close");
        } catch (IOException e) {
            System.out.println("Cannot close the socket");
            System.exit(-1);
        }


    }
}