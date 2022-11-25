
import java.io.IOException;
import java.net.*;

public class UDPClient {

    private static int port;
    private static String ip;
    private DatagramSocket socket;
    private InetAddress address;

    public UDPClient(){
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            System.out.println("Unknown Host");
            System.exit(-1);
        } catch (SocketException e) {
            System.out.println("Socket Error");
            System.exit(-1);
        }
    }

    public String sendMsg(String msg){
        byte[] buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);

        try {
            socket.send(packet);
        }
        catch (IOException e){
            System.out.println("No IO");
            System.exit(-1);
        }

        packet = new DatagramPacket(buf, buf.length);

        try {
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

        ip = "127.0.0.1";
        port = 2000;

        UDPClient client = new UDPClient();
        String odp = client.sendMsg("piotr");

        client.close();
        System.out.println(odp);

    }
}
