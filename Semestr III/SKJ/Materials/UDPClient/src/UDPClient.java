
import java.io.IOException;
import java.net.*;

public class UDPClient {

    private static int port;
    private static int clientPort;
    private static String ip;
    private DatagramSocket socket;
    private InetAddress address;

    public UDPClient(){
        try {
            socket = new DatagramSocket(port); /** The port you listen on */
            address = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            System.out.println("Unknown Host");
            System.exit(-1);
        } catch (SocketException e) {
            System.out.println("Socket Error");
            System.exit(-1);
        }
    }

    public void sendMsg(String msg){
        byte[] buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, clientPort); /** port on which you want to send message */

        try {
            socket.send(packet);
        }
        catch (IOException e){
            System.out.println("No IO");
            System.exit(-1);
        }
    }

    public String receiveMsg(){
        byte[] buf = new byte[100];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        try {
            socket.receive(packet);
        } catch (IOException e){
            System.out.println("No IO");
            System.exit(-1);
        }

        return new String(packet.getData(),0, packet.getLength());
    }

    public void close() {
        socket.close();
    }

    public static void main(String[] args){
        /**
        * If someone wants to communicate with you, he needs to have the same ip as you
        */
        ip = "192.168.5.105";

        /**
        * Your port on which messages can be sent
        */
        port = 4445;

        /**
        * If you want to send back messages to recipient, you need to specify his port
        * If you want to start the communication by sending a message, you need to specify port of the recipient
        */
        clientPort = 0000;

        UDPClient client = new UDPClient();
        System.out.println("Waiting on port: " + port + '\n');

        /**
        * String message that you receive
        */
        String response;

        /**
        * Operations which you can make
        */
        {

            client.sendMsg("");

            response = client.receiveMsg();

        }
        client.close();
    }
}
