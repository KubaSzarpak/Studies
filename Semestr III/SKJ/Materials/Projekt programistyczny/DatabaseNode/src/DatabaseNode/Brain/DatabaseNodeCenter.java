package DatabaseNode.Brain;

import DatabaseNode.Communication.NodeCommunication;
import DatabaseNode.Communication.SocketListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.net.InetAddress.getByName;

/**
 * This class represents DatabaseNode hearth. It does operations that are required.
 * It is based on TCP protocol and UDP protocol.
 *  Using TCP protocol, it handles nodes that try to connect and clients that want to communicate.
 *  Using UDP protocol, it handles communication between nodes which are in the distributed database web.
 * <p>
 * Operation's methods are designed to be used with unlimited amount of node in distributed database web.
 * These method work in recursive way. If this node does not have required information then request is sent to next node. Result of this request in next node comes back to this node and is returned.
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
    /**
     * Node's classes which deal with communication.
     */
    private final NodeCommunication nodeCommunication;
    private final SocketListener socketListener;
    /**
     * Stored key and value.
     */
    private int key;
    public int value;
    /**
     * Boolean field which help with communication.
     * wait field says to NodeCommunication's RequestListener inner class to wait or not.
     * running field says if server is running or not.
     * sendNext says if request can be sent to next node.
     */
    public boolean wait;
    public boolean running;
    private boolean sendNext;

    /**
     * Constructor without key and value that will be stored.
     *
     * @param port port of this server.
     * @param address ip address of this server.
     */
    public DatabaseNodeCenter(int port, String address) {
        this.running = true;
        this.PORT = port;
        this.ADDRESS = address;
        this.nodeCommunication = new NodeCommunication(this);
        this.socketListener = new SocketListener(this);
        this.socketListener.start();
        this.wait = false;
        this.sendNext = true;
        this.nodeCommunication.createSocket(PORT);

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
     * @param port port of this server.
     * @param address ip address of this server.
     * @param key key of a value that will be stored.
     * @param value value that will be stored.
     */
    public DatabaseNodeCenter(int port, String address, int key, int value) {
        this.running = true;
        this.PORT = port;
        this.ADDRESS = address;
        this.nodeCommunication = new NodeCommunication(this);
        this.socketListener = new SocketListener(this);
        this.socketListener.start();
        this.wait = false;
        this.sendNext = true;
        this.nodeCommunication.createSocket(PORT);

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

    /**
     * This method creates TCP connection with another node to connect this node to it.
     * It sends:
     *  "Node" which informs, that it is a node.
     *  "PORT" which is value from PORT field.
     * <p>
     * Then it reads:
     *  Line that informs if connection can be done or not.
     *      If not than it read another line which is the reason.
     *      If yes, then message will be port of first node of the distributed database web.
     *  Line with address of first node of the distributed database web.
     * <p>
     *  If there is any problem with connection then it prints "No I/O" message.
     *
     * @param destinationAddress ip address of node that it trys to connect.
     * @param destinationPort port of node that is trys to connect.
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
                nodeCommunication.sendMsg("newPrevious " + ADDRESS + ":" + PORT, nextNodeAddress, nextNodePort);

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

    /**
     * Method which set nodeCommunication timeout to given in parameter.
     * It sleeps current thread for 500 milliseconds to give time nodeCommunication to re-roll.
     * @param timeout value of nodeCommunication timeout.
     */
    private void prepareCommunication(int timeout) {
        try {
            Thread.sleep(500);
            nodeCommunication.setTimeOut(timeout);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is a brain of the server.
     * It recognizes request and calls corresponding methods.
     * <p>
     * Firstly, it creates needed patterns.
     * Then it trys to match them with given requestMessage. If it matches, then it calls corresponding method and returns the answer.
     *
     * @param requestMessage the request that comes to the node.
     * @return result of matching operation or given requestMessage if it does not match any pattern.
     */
    public synchronized String operate(String requestMessage) {
        String answer = "";
        boolean addYourPort;
        wait = true;
        prepareCommunication(0);

        Pattern checkPortPattern = Pattern.compile("\\d\\d\\d\\d");
        Pattern setValuePattern = Pattern.compile("set-value");
        Pattern getValuePattern = Pattern.compile("get-value");
        Pattern findKeyPattern = Pattern.compile("find-key");
        Pattern getMaxPattern = Pattern.compile("get-max");
        Pattern getMinPattern = Pattern.compile("get-min");
        Pattern newRecordPattern = Pattern.compile("new-record");
        Pattern terminatePattern = Pattern.compile("terminate");
        Pattern newNextPattern = Pattern.compile("newNext");
        Pattern newPreviousPattern = Pattern.compile("newPrevious");
        Pattern findPortPattern = Pattern.compile("find-port");

        Matcher matcher = checkPortPattern.matcher(requestMessage.substring(0, 4));

        if (matcher.matches()) {
            addYourPort = false;
            sendNext = (nextNodePort != Integer.parseInt(requestMessage.substring(0, 4)));
        } else {
            addYourPort = true;
            sendNext = (nextNodePort != PORT);
        }
        matcher = findPortPattern.matcher(requestMessage);
        if (matcher.find()) {
            answer = findPort(Integer.parseInt(requestMessage.substring(matcher.end() + 1)),
                    addYourPort ? "" : requestMessage.substring(0, matcher.start()),
                    addYourPort);
            return endOperate(requestMessage, answer);
        }

        matcher = setValuePattern.matcher(requestMessage);
        if (matcher.find()) {
            String[] tmp = requestMessage.substring(matcher.end() + 1).split(":");
            answer = setValue(Integer.parseInt(tmp[0]),
                    Integer.parseInt(tmp[1]),
                    addYourPort ? "" : requestMessage.substring(0, matcher.start()),
                    addYourPort);
            return endOperate(requestMessage, answer);
        }

        matcher = getValuePattern.matcher(requestMessage);
        if (matcher.find()) {
            answer = getValue(Integer.parseInt(requestMessage.substring(matcher.end() + 1)),
                    addYourPort ? "" : requestMessage.substring(0, matcher.start()),
                    addYourPort);
            return endOperate(requestMessage, answer);
        }

        matcher = findKeyPattern.matcher(requestMessage);
        if (matcher.find()) {
            answer = findKey(Integer.parseInt(requestMessage.substring(matcher.end() + 1)),
                    addYourPort ? "" : requestMessage.substring(0, matcher.start()),
                    addYourPort);
            return endOperate(requestMessage, answer);
        }

        matcher = getMaxPattern.matcher(requestMessage);
        if (matcher.find()) {
            if (requestMessage.substring(4).length() > getMaxPattern.pattern().length()) {
                answer = getMax(Integer.parseInt(requestMessage.substring(matcher.end() + 1)),
                        addYourPort ? "" : requestMessage.substring(0, matcher.start()),
                        addYourPort);
            } else {
                answer = getMax(this.value,
                        addYourPort ? "" : requestMessage.substring(0, matcher.start()),
                        addYourPort);
            }
            return endOperate(requestMessage, answer);
        }

        matcher = getMinPattern.matcher(requestMessage);
        if (matcher.find()) {
            if (requestMessage.substring(4).length() > getMaxPattern.pattern().length()) {
                answer = getMin(Integer.parseInt(requestMessage.substring(matcher.end() + 1)),
                        addYourPort ? "" : requestMessage.substring(0, matcher.start()),
                        addYourPort);
            } else {
                answer = getMin(this.value,
                        addYourPort ? "" : requestMessage.substring(0, matcher.start()),
                        addYourPort);
            }
            return endOperate(requestMessage, answer);
        }

        matcher = newRecordPattern.matcher(requestMessage);
        if (matcher.find()) {
            String[] tmp = requestMessage.substring(matcher.end() + 1).split(":");
            newRecord(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]));
            answer = "OK";
            return endOperate(requestMessage, answer);
        }

        matcher = terminatePattern.matcher(requestMessage);
        if (matcher.find()) {
            terminate();
            answer = "OK";
            new Thread(() -> {
                try {
                    running = false;
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.exit(7);
            }).start();
            return endOperate(requestMessage, answer);
        }

        matcher = newNextPattern.matcher(requestMessage);
        if (matcher.find()) {
            String[] tmp = requestMessage.substring(matcher.end() + 1).split(":");
            try {
                nextNodeAddress = getByName(tmp[0]);
                nextNodePort = Integer.parseInt(tmp[1]);
                answer = "null";
                return endOperate(requestMessage, answer);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }

        matcher = newPreviousPattern.matcher(requestMessage);
        if (matcher.find()) {
            String[] tmp = requestMessage.substring(matcher.end() + 1).split(":");
            try {
                previousNodeAddress = InetAddress.getByName(tmp[0]);
                previousNodePort = Integer.parseInt(tmp[1]);
                answer = "null";
                return endOperate(requestMessage, answer);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }


        return endOperate(requestMessage, answer);
    }

    /**
     * Formalities that need to be done before operate() method end.
     * @param requestMessage the request that comes to the node.
     * @param answer the result of methods.
     * @return given answer if it is not empty. If it is empty then it returns given requestedMessage.
     */
    private String endOperate(String requestMessage, String answer) {
        prepareCommunication(500);
        wait = false;
        return answer.isEmpty() ? requestMessage : answer;
    }

    /**
     * This method is used to forward request to next node.
     * @param requestMessage request that need to be sent to next node.
     * @param failure message which will be returned if request can not be sent to next node.
     * @return Response of next node or given failure message.
     */
    private String forwardRequest(String requestMessage, String failure) {
        if (sendNext) {
            nodeCommunication.sendMsg(requestMessage, nextNodeAddress, nextNodePort);
            return nodeCommunication.receiveMsg();
        } else {
            return failure;
        }
    }

    /**
     * @param key key of a stored value that need to be changed.
     * @param value new stored value.
     * @param prefix a port that precedes the request.
     * @param addYourPort boolean value that says if this node's port need to be added as a prefix to request or a previous prefix should be added to request.
     * @return "OK" if value is set or "ERROR" if given key is not found.
     */
    private String setValue(int key, int value, String prefix, boolean addYourPort) {
        if (this.key == key) {
            this.value = value;
            return "OK";
        }
        return forwardRequest((addYourPort ? PORT + "set-value " : prefix + "set-value ") + key + ":" + value, "ERROR");
    }

    /**
     * @param key the key whose value is being searched for
     * @param prefix a port that precedes the request.
     * @param addYourPort boolean value that says if this node's port need to be added as a prefix to request or a previous prefix should be added to request.
     * @return "key:value" message if key is found or "ERROR" it key is not found.
     */
    private String getValue(int key, String prefix, boolean addYourPort) {
        if (this.key == key)
            return key + ":" + value;
        return forwardRequest((addYourPort ? PORT + "get-value " : prefix + "get-value ") + key, "ERROR");

    }

    /**
     *
     * @param key the key whose value is being searched for
     * @param prefix a port that precedes the request.
     * @param addYourPort boolean value that says if this node's port need to be added as a prefix to request or a previous prefix should be added to request.
     * @return "ip address:port" message of a node that has searched key or "ERROR" if key is not found.
     */
    private String findKey(int key, String prefix, boolean addYourPort) {
        if (this.key == key)
            return ADDRESS + ":" + PORT;
        return forwardRequest((addYourPort ? PORT + "find-key " : prefix + "find-key ") + key, "ERROR");

    }

    /**
     *
     * @param max current max value.
     * @param prefix a port that precedes the request.
     * @param addYourPort boolean value that says if this node's port need to be added as a prefix to request or a previous prefix should be added to request.
     * @return Max value of entire distributed database.
     */
    private String getMax(int max, String prefix, boolean addYourPort) {
        int newMax = Math.max(this.value, max);
        return forwardRequest((addYourPort ? PORT + "get-max " : prefix + "get-max ") + newMax, String.valueOf(newMax));
    }
    /**
     *
     * @param min current min value.
     * @param prefix a port that precedes the request.
     * @param addYourPort boolean value that says if this node's port need to be added as a prefix to request or a previous prefix should be added to request.
     * @return Min value of entire distributed database.
     */
    private String getMin(int min, String prefix, boolean addYourPort) {
        int newMin = Math.min(this.value, min);
        return forwardRequest((addYourPort ? PORT + "get-min " : prefix + "get-min ") + newMin, String.valueOf(newMin));
    }

    /**
     * @param key key of a new stored value.
     * @param value new stored value.
     */
    private void newRecord(int key, int value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Method informing previous and next nodes about server shutdown.
     */
    private void terminate() {
        nodeCommunication.sendMsg("newPrevious " + previousNodeAddress.getHostAddress() + ":" + previousNodePort, nextNodeAddress, nextNodePort);
        nodeCommunication.sendMsg("newNext " + nextNodeAddress.getHostAddress() + ":" + nextNodePort, previousNodeAddress, previousNodePort);
    }

    /**
     *
     * @param port port which is looked for.
     * @param prefix a port that precedes the request.
     * @param addYourPort boolean value that says if this node's port need to be added as a prefix to request or a previous prefix should be added to request.
     * @return "OK" if port is found or "ERROR" if port is not found.
     */
    public String findPort(int port, String prefix, boolean addYourPort) {
        if (PORT == port)
            return "ERROR";
        return forwardRequest((addYourPort ? PORT + "find-port " : prefix + "find-port ") + port, "OK");
    }
}