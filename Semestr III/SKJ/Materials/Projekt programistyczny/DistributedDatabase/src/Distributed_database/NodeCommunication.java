package Distributed_database;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class NodeCommunication {
    private DatagramSocket nodeSocket;
    private final DatabaseNode node;
    private int sourcePort;
    private InetAddress sourceAddress;

    public NodeCommunication(DatabaseNode node) {
        this.node = node;
        new RequestListener().start();
    }
    public void createSocket(int localPort) {
        try {
            nodeSocket = new DatagramSocket(localPort);
            nodeSocket.setSoTimeout(500);

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
            sourcePort = packet.getPort();
            sourceAddress = packet.getAddress();
        } catch (IOException e) {
//            System.out.println("No IO receive");
            return "null";
        }
        System.out.println("Message received from " + sourcePort + " " + new String(packet.getData(), 0, packet.getLength()));
        return new String(packet.getData(), 0, packet.getLength());
    }

    public void sendMsg(String msg, InetAddress destinationAddress, int destinationPort) {
        byte[] buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, destinationAddress, destinationPort);

        try {
            nodeSocket.send(packet);
            System.out.println("Message sent from " + nodeSocket.getLocalPort() + " to " + destinationPort + " " + msg);
        } catch (IOException e) {
            System.out.println("No IO send");
            System.exit(-1);
        }
    }

    public void setTimeOut (int timeOut) {
        try {
            nodeSocket.setSoTimeout(timeOut);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    private class RequestListener extends Thread {
        @Override
        public void run() {
            while (node.running) {
                if (node.wait) {
                    continue;
                }
                if (nodeSocket != null) {
                    String receivedMsg = receiveMsg();
                    InetAddress tmpAddress = sourceAddress;
                    int tmpPort = sourcePort;
                    String msg = node.operate(receivedMsg);
                    if (!msg.equals("null")) {
                        System.out.println(msg);
                        sendMsg(msg, tmpAddress, tmpPort);
                        System.out.println("Wysłano");
                    }
                }
            }
        }
    }
}
