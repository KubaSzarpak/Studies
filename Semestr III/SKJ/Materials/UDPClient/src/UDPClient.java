
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
            socket = new DatagramSocket(port); //port na którym nasłuchuję
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
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, clientPort); //port na jaki chcę wysłać wiadomość

        try {
            socket.send(packet);
        }
        catch (IOException e){
            System.out.println("No IO");
            System.exit(-1);
        }
    }

    public String reciveMsg(){
        byte[] buf = new byte[100];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        try {
            System.out.println("Czekam na porcie: " + port + '\n');
            socket.receive(packet);
        }
        catch (IOException e){
            System.out.println("No IO");
            System.exit(-1);
        }

        return new String(packet.getData(),0, packet.getLength());
    }

    public void close() {
        socket.close();
    }

    public static void main(String[] args){

        ip = "172.23.129.28";
        port = 4445;
        clientPort = 0000;

        UDPClient client = new UDPClient();
        client.sendMsg("Message");

        String odp = client.reciveMsg();

        client.close();
        System.out.println(odp);

    }
}
