import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import static java.net.InetAddress.getByName;

/**
 * This class represents DatabaseNode hearth. It does operations that are required.
 * It is based on TCP protocol and UDP protocol.
 * Using TCP protocol, it handles nodes that try to connect and clients that want to communicate.
 * Using UDP protocol, it handles communication between nodes which are in the distributed database web.
 * <p>
 * Operation's methods are designed to be used with unlimited amount of node in distributed database web.
 * These method work in recursive way. If this node does not have required information then request is sent to next node. Result of this request in next node comes back to this node and is returned.
 *
 * @author Jakub Szarpak
 */
public class DatabaseNodeCenter {
    /**
     * Port and ip address of this server.
     */
    public final int PORT;
    public final String ADDRESS;
    /**
     * Port and InetAddress of next connected node.
     * Next connected node is a node that connects with this node and is a next node in order after this one.
     */
    private int nextNodePort;
    private InetAddress nextNodeAddress;
    /**
     * Port and InetAddress of previous node.
     * Previous node is the node to which the current node is connected and is a previous node in order.
     */
    private int previousNodePort;
    private InetAddress previousNodeAddress;

    private final SocketListener socketListener;
    /**
     * Stored key and value.
     */
    private int key;
    private int value;
    public boolean running;
    private boolean sendNext;

    /**
     * Constructor without key and value that will be stored.
     *
     * @param port    port of this server.
     * @param address ip address of this server.
     */
    public DatabaseNodeCenter(int port, String address) {
        this.running = true;
        this.PORT = port;
        this.ADDRESS = address;
        this.socketListener = new SocketListener(this);
        this.socketListener.start();
        this.sendNext = true;
        new RequestListener().start();

        try {
            this.nextNodePort = PORT;
            this.nextNodeAddress = InetAddress.getByName(ADDRESS);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        try {
            this.previousNodePort = PORT;
            this.previousNodeAddress = InetAddress.getByName(ADDRESS);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructor with key and value that will be stored.
     *
     * @param port    port of this server.
     * @param address ip address of this server.
     * @param key     key of a value that will be stored.
     * @param value   value that will be stored.
     */
    public DatabaseNodeCenter(int port, String address, int key, int value) {
        this.running = true;
        this.PORT = port;
        this.ADDRESS = address;
        this.socketListener = new SocketListener(this);
        this.socketListener.start();
        this.sendNext = true;
        new RequestListener().start();

        this.key = key;
        this.value = value;

        try {
            this.nextNodePort = PORT;
            this.nextNodeAddress = InetAddress.getByName(ADDRESS);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        try {
            this.previousNodePort = PORT;
            this.previousNodeAddress = InetAddress.getByName(ADDRESS);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return nextNodePort.
     */
    public int getNextNodePort() {
        return nextNodePort;
    }

    /**
     * @return nextNodeAddress.
     */
    public InetAddress getNextNodeAddress() {
        return nextNodeAddress;
    }

    /**
     * @param nextNodeAddress InetAddress of next node.
     */
    public void setNextNodeAddress(InetAddress nextNodeAddress) {
        this.nextNodeAddress = nextNodeAddress;
    }

    /**
     * @param nextNodePort port of next node.
     */
    public void setNextNodePort(int nextNodePort) {
        this.nextNodePort = nextNodePort;
    }

    public void addOperateThread(String requestMessage, PrintWriter writer) {
        new OperateThread(requestMessage, writer).start();
    }

    /**
     * This method creates TCP connection with another node to connect this node to it.
     * It sends:
     * "Node" which informs, that it is a node.
     * "PORT" which is value from PORT field.
     * <p>
     * Then it reads:
     * Line that informs if connection can be done or not.
     * If not than it read another line which is the reason.
     * If yes, then message will be port of first node of the distributed database web.
     * Line with address of first node of the distributed database web.
     * <p>
     * If there is any problem with connection then it prints "No I/O" message.
     *
     * @param destinationAddress ip address of node that it trys to connect.
     * @param destinationPort    port of node that is trys to connect.
     */
    public void connect(String destinationAddress, int destinationPort) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(destinationAddress, destinationPort));

            BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            writer.println("Node");
            writer.println(PORT);

            String line = read.readLine();

            if (line.equals("ERROR")) {
                System.out.println(line);
                System.out.println(read.readLine());
                return;
            } else {
                nextNodePort = Integer.parseInt(line);
                nextNodeAddress = getByName(read.readLine());
                new OperateThread(PORT + " newPrevious " + ADDRESS + ":" + PORT).start();

                previousNodeAddress = new InetSocketAddress(destinationAddress, destinationPort).getAddress();
                previousNodePort = destinationPort;
            }

            socket.close();

            System.out.println("Connected to the node");
        } catch (IOException e) {
            System.out.println("No I/O");
            throw new RuntimeException(e);
        }
    }


    private class OperateThread extends Thread {
        private final String operation;
        private final String values;
        private final String prefix;
        private DatagramSocket socket;
        private InetAddress sourceAddress;
        private int sourcePort;
        private final boolean isFirst;
        private PrintWriter writer;

        public OperateThread(String requestMessage, InetAddress sourceAddress, int sourcePort) {
            this.isFirst = false;
            this.sourceAddress = sourceAddress;
            this.sourcePort = sourcePort;
            String[] tmpArr = requestMessage.split(" ");

            if (tmpArr.length == 3) {
                this.prefix = tmpArr[0];
                this.operation = tmpArr[1];
                this.values = tmpArr[2];
            } else {
                this.prefix = tmpArr[0];
                this.operation = tmpArr[1];
                this.values = String.valueOf(PORT);
            }

            sendNext = Integer.parseInt(prefix) != nextNodePort;

            createSocket();
        }

        public OperateThread(String requestMessage, PrintWriter writer) {
            this.isFirst = true;
            this.writer = writer;
            String[] tmpArr = requestMessage.split(" ");

            this.prefix = String.valueOf(PORT);
            if (tmpArr.length == 2) {
                this.operation = tmpArr[0];
                this.values = tmpArr[1];
            } else {
                this.operation = tmpArr[0];
                this.values = String.valueOf(value);
            }

            sendNext = Integer.parseInt(prefix) != nextNodePort;

            createSocket();
        }

        public OperateThread(String requestMessage) {
            this.isFirst = true;

            String[] tmpArr = requestMessage.split(" ");

            this.prefix = tmpArr[0];
            this.operation = tmpArr[1];
            this.values = tmpArr[2];

            sendNext = true;

            createSocket();
        }

        private void createSocket() {
            try {
                this.socket = new DatagramSocket();
                this.socket.setSoTimeout(500);
            } catch (SocketException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void run() {
            String[] result = new String[2];

            switch (operation) {
                case "set-value":
                    result = setValue(values.split(":"));
                    break;
                case "get-value":
                    result = getValue(Integer.parseInt(values));
                    break;
                case "find-key":
                    result = findKey(Integer.parseInt(values));
                    break;
                case "get-max":
                    result = getMax(Integer.parseInt(values));
                    break;
                case "get-min":
                    result = getMin(Integer.parseInt(values));
                    break;
                case "new-record":
                    result = newRecord(values.split(":"));
                    break;
                case "terminate":
                    result = terminate();
                    break;
                case "newPrevious":
                    try {
                        if (isFirst) {
                            sendMsg(PORT + " " + operation + " " + values, nextNodeAddress, nextNodePort);
                        } else {
                            String[] tmpArr = values.split(":");
                            previousNodeAddress = InetAddress.getByName(tmpArr[0]);
                            previousNodePort = Integer.parseInt(tmpArr[1]);
                        }
                    } catch (UnknownHostException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                case "newNext":
                    try {
                        if (isFirst) {
                            sendMsg(PORT + " " + operation + " " + values, previousNodeAddress, previousNodePort);
                        } else {
                            String[] tmpArr = values.split(":");
                            nextNodeAddress = InetAddress.getByName(tmpArr[0]);
                            nextNodePort = Integer.parseInt(tmpArr[1]);
                        }
                    } catch (UnknownHostException e) {
                        throw new RuntimeException(e);
                    }
                    return;
            }

            switch (result[0]) {
                case "OK":
                    if (isFirst) {
                        writer.println(result[1]);
                    } else {
                        sendMsg(result[1], sourceAddress, sourcePort);
                    }
                    break;
                case "ERROR":
                    if (sendNext) {
                        sendMsg(result[1], nextNodeAddress, nextNodePort);

                        String response = receiveMsg();
                        if (!response.equals("NULL")) {
                            if (isFirst) {
                                writer.println(response);
                            } else {
                                sendMsg(response, sourceAddress, sourcePort);
                            }
                        } else {
                            if (isFirst) {
                                writer.println(result[0]);
                            } else {
                                sendMsg(result[0], sourceAddress, sourcePort);
                            }
                        }
                    } else {
                        if (isFirst) {
                            writer.println(result[0]);
                        } else {
                            sendMsg(result[0], sourceAddress, sourcePort);
                        }
                    }
                    break;
                case "ROUND":
                    if (sendNext) {
                        sendMsg(result[1], nextNodeAddress, nextNodePort);

                        String response = receiveMsg();
                        if (!response.equals("NULL")) {
                            if (isFirst) {
                                writer.println(response);
                            } else {
                                sendMsg(response, sourceAddress, sourcePort);
                            }
                        } else {
                            if (isFirst) {
                                writer.println(result[1].split(" ")[2]);
                            } else {
                                sendMsg(result[1].split(" ")[2], sourceAddress, sourcePort);
                            }
                        }
                    } else {
                        if (isFirst) {
                            writer.println(result[1].split(" ")[2]);
                        } else {
                            sendMsg(result[1].split(" ")[2], sourceAddress, sourcePort);
                        }
                    }
                    break;
                default:
            }
            if (writer != null)
                writer.close();

        }

        public String receiveMsg() {
            byte[] buf = new byte[100];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            try {
                socket.receive(packet);
                System.out.println("Message received from " + packet.getPort() + ": " + new String(packet.getData(), 0, packet.getLength()));
            } catch (IOException e) {
                return "NULL";
            }
            return new String(packet.getData(), 0, packet.getLength());
        }

        public void sendMsg(String msg, InetAddress destinationAddress, int destinationPort) {
            byte[] buf = msg.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, destinationAddress, destinationPort);

            try {
                socket.send(packet);
                System.out.println("Message send from " + socket.getLocalPort() + " to " + destinationPort + ": " + msg);
            } catch (IOException e) {
                System.out.println("No IO");
                System.exit(-1);
            }
        }


        private String[] setValue(String[] values) {

            if (key == Integer.parseInt(values[0])) {
                value = Integer.parseInt(values[1]);
                return new String[]{"OK", "OK"};
            } else {
                return new String[]
                        {
                                "ERROR",
                                prefix + " set-value " + values[0] + ":" + values[1]
                        };
            }
        }


        private String[] getValue(int givenKey) {
            if (key == givenKey) {
                return new String[]
                        {
                                "OK",
                                key + ":" + value
                        };
            } else {
                return new String[]
                        {
                                "ERROR",
                                prefix + " get-value " + givenKey
                        };
            }

        }


        private String[] findKey(int givenKey) {
            if (key == givenKey) {
                return new String[]
                        {
                                "OK",
                                ADDRESS + ":" + PORT
                        };
            } else {
                return new String[]
                        {
                                "ERROR",
                                prefix + " find-key " + givenKey
                        };
            }
        }


        private String[] getMax(int max) {
            int newMax = Math.max(value, max);

            return new String[]
                    {
                            "ROUND",
                            prefix + " get-max " + newMax
                    };
        }


        private String[] getMin(int min) {
            int newMin = Math.min(value, min);

            return new String[]
                    {
                            "ROUND",
                            prefix + " get-min " + newMin
                    };
        }


        private String[] newRecord(String[] values) {
            key = Integer.parseInt(values[0]);
            value = Integer.parseInt(values[1]);

            return new String[]
                    {
                            "OK",
                            "OK"
                    };
        }

        /**
         * Method informing previous and next nodes about server shutdown.
         */
        private String[] terminate() {
            sendMsg(PORT + " newPrevious " + previousNodeAddress.getHostAddress() + ":" + previousNodePort, nextNodeAddress, nextNodePort);
            sendMsg(PORT + " newNext " + nextNodeAddress.getHostAddress() + ":" + nextNodePort, previousNodeAddress, previousNodePort);

            new Thread(() -> {
                try {
                    Thread.sleep(500);
                    writer.close();
                    socket.close();
                    System.exit(0);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();

            return new String[]
                    {
                            "OK",
                            "OK"
                    };
        }
    }

    private class RequestListener extends Thread {
        private final DatagramSocket socket;
        /**
         * Port and ip address of UDP server that sent a message.
         */
        private int sourcePort;
        private InetAddress sourceAddress;

        public RequestListener() {
            try {
                socket = new DatagramSocket(PORT);
            } catch (SocketException e) {
                System.out.println("Connection error");
                throw new RuntimeException(e);
            }
        }


        @Override
        public void run() {
            while (running) {
                String receivedMsg = receiveMsg();
                new OperateThread(receivedMsg, sourceAddress, sourcePort).start();
            }
            this.socket.close();
        }

        /**
         * Receives massages from UDP clients.
         * It also prints message "Message received from [source port] [received massage]".
         *
         * @return Received message in String format or "null" message it no message has come.
         */
        public String receiveMsg() {
            byte[] buf = new byte[100];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            try {
                socket.receive(packet);
                sourcePort = packet.getPort();
                sourceAddress = packet.getAddress();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Message received from " + sourcePort + " : " + new String(packet.getData(), 0, packet.getLength()));
            return new String(packet.getData(), 0, packet.getLength());
        }
    }
}