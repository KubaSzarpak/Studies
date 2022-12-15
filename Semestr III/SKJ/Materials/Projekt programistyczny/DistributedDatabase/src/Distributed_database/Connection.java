package Distributed_database;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Connection {
    private DatagramSocket nodeSocket;
    boolean newMessage;

    public Connection() {
        this.newMessage = false;
    }
    public synchronized void createSocket(int localPort) {
        try {
            nodeSocket = new DatagramSocket(localPort);
        } catch (SocketException e) {
            System.out.println("Connection error");
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

    public void sendMsg(String msg, InetAddress destinationAddress, int destinationPort) {
        byte[] buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, destinationAddress, destinationPort);

        try {
            nodeSocket.send(packet);
            System.out.println("Message sent");
        } catch (IOException e) {
            System.out.println("No IO");
            System.exit(-1);
        }
    }
}
