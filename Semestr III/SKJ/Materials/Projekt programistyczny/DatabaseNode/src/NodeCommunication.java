import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * This class represents UDP server, which is used for communication with another nodes of distributed database web.
 * @author Jakub Szarpak
 */
public class NodeCommunication {
    private DatagramSocket nodeSocket;
    private final DatabaseNodeCenter node;
    private int timeout;
    /**
     * Port and ip address of UDP server that sent a message.
     */
    private int sourcePort;
    private InetAddress sourceAddress;

    /**
     * Constructor which assigns node field and starts RequestListener thread.
     *
     * @param node reference to its DatabaseNodeCenter.
     */
    public NodeCommunication(DatabaseNodeCenter node) {
        this.node = node;
        new RequestListener().start();
    }

    /**
     * Creates UDP server with given localPort and sets timeout ot 500 milliseconds.
     *
     * @param localPort port on which UDP server will be created. It should be server's port.
     */
    public void createSocket(int localPort) {
        try {
            nodeSocket = new DatagramSocket(localPort);
            nodeSocket.setSoTimeout(1);
            timeout = 1;

        } catch (SocketException e) {
            System.out.println("Connection error");
            throw new RuntimeException(e);
        }
    }

    /**
     * Receives massages from another UDP servers/another nodes.
     * It also prints message "Message received from [source port] [received massage]".
     *
     * @return Received message in String format or "null" message it no message has come.
     */
    public String receiveMsg() {
        byte[] buf = new byte[100];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        try {
            nodeSocket.receive(packet);
            sourcePort = packet.getPort();
            sourceAddress = packet.getAddress();
        } catch (IOException e) {
            return "null";
        }
        System.out.println("Message received from " + sourcePort + " " + new String(packet.getData(), 0, packet.getLength()));
        return new String(packet.getData(), 0, packet.getLength());
    }

    /**
     * Sends given message to UDP server with given destinationAddress and destinationPort.
     * It also prints message "Message send from [source port/local port] to [destination port] [message]" if message is sent or "Message send ERROR" if message could not be sent.
     *
     * @param msg massage that needs to be sent.
     * @param destinationAddress address of destination UDP server.
     * @param destinationPort port of destination UDP server.
     */
    public void sendMsg(String msg, InetAddress destinationAddress, int destinationPort) {
        byte[] buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, destinationAddress, destinationPort);

        try {
            nodeSocket.send(packet);
            System.out.println("Message send from " + nodeSocket.getLocalPort() + " to " + destinationPort + " " + msg);
        } catch (IOException e) {
            System.out.println("Message send ERROR");
            System.exit(-1);
        }
    }

    /**
     * Sets timeout of the server.
     * @param timeout value od timeout.
     */
    public void setTimeOut(int timeout) {
        try {
            nodeSocket.setSoTimeout(timeout);
            this.timeout = timeout;
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public int getTimeout() {
        return timeout;
    }

    /**
     * Inner class of NodeConnection which listens for incoming messages.
     */
    private class RequestListener extends Thread {
        /**
         * Run method from Runnable interface.
         * In while loop it listens for incoming messages and gives them to operate() method on node field.
         * If result of operate() method is "null" then it does not send this message to the source UDP server.
         * In other cases it sends result of operate() method to source UDP server.
         */
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
                    }
                }
            }
        }
    }
}
