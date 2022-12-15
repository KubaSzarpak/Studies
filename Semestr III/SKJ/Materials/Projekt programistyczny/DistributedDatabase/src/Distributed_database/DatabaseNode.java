package Distributed_database;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseNode {
    final int PORT;
    final String ADDRESS;
    private final Connection connection;
    private int previousNodePort;
    private int nextNodePort;
    private InetAddress previousNodeAddress;
    private InetAddress nextNodeAddress;
    private final SocketListener socketListener;
    private int key;
    private int value;

    public DatabaseNode(int port, String address) {
        this.PORT = port;
        this.ADDRESS = address;
        this.nextNodePort = -1;
        this.connection = new Connection();
        this.socketListener = new SocketListener(this);

        this.connection.createSocket(port);
    }

    public DatabaseNode(int port, String address, int key, int value) {
        this.PORT = port;
        this.ADDRESS = address;
        this.nextNodePort = -1;
        this.previousNodePort = -1;
        this.connection = new Connection();
        this.socketListener = new SocketListener(this);

        this.key = key;
        this.value = value;

        this.connection.createSocket(port);
    }



    public void listenSocket () {
        socketListener.start();
    }

    private String setValue(int key, int value) {
        if (this.key == key) {
            this.value = value;
            return "OK";
        } else {
            if (nextNodePort != -1 && nextNodeAddress != null) {
                connection.sendMsg("set-value " + key + ":" + value, nextNodeAddress, nextNodePort);
            } else {
                if (previousNodePort == -1 && previousNodeAddress == null) {
                    return "ERROR";
                } else {
                    connection.sendMsg("ERROR", previousNodeAddress, previousNodePort);
                }
            }
            return connection.receiveMsg();
        }
    }

    private String getValue(int key) {
        if (this.key == key) {
            return key + ":" + value;
        } else {
            if (nextNodePort != -1 && nextNodeAddress != null) {
                connection.sendMsg("get-value " + key, nextNodeAddress, nextNodePort);
            } else {
                if (previousNodePort == -1 && previousNodeAddress == null) {
                    return "ERROR";
                } else {
                    connection.sendMsg("ERROR", previousNodeAddress, previousNodePort);
                }
            }
            return connection.receiveMsg();
        }
    }

    private String findKey(int key) {
        if (this.key == key) {
            return ADDRESS + ":" + PORT;
        } else {
            if (nextNodePort != -1 && nextNodeAddress != null) {
                connection.sendMsg("find-key " + key, nextNodeAddress, nextNodePort);
            } else {
                connection.sendMsg("ERROR", previousNodeAddress, previousNodePort);
            }

            return connection.receiveMsg();
        }
    }

    private Object getMax(){
        return null;
    }

    private Object getMin() {
        return null;
    }

    private void newRecord(int key, int value){
        this.key = key;
        this.value = value;
    }

    private void terminate(){

    }

    public String operate (String task) {
        String answer = "";

        Pattern setValue = Pattern.compile("set-value");
        Pattern getValue = Pattern.compile("get-value");
        Pattern findKey = Pattern.compile("find-key");

        Matcher matcher = setValue.matcher(task);
        if (matcher.find()){
            String[] tmp = task.substring(10).split(":");
            answer = setValue(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]));
            if (!answer.equals("-1"))
                return answer;
        }

        matcher = getValue.matcher(task);
        if (matcher.find()){
            answer = getValue(Integer.parseInt(task.substring(10)));
            if (!answer.equals("-1"))
                return answer;
        }

        matcher = findKey.matcher(task);
        if (matcher.find()){
            return findKey(Integer.parseInt(task.substring(9)));
        }

        return task;
    }

    public void setNextNodeAddress(InetAddress nextNodeAddress) {
        this.nextNodeAddress = nextNodeAddress;
    }

    public void setNextNodePort(int nextNodePort) {
        this.nextNodePort = nextNodePort;
    }

    public void connect(String destinationAddress, int destinationPort) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(destinationAddress, destinationPort));

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("Node");

            System.out.println("Connected to the node");

            previousNodeAddress = new InetSocketAddress(destinationAddress, destinationPort).getAddress();
            previousNodePort = destinationPort;

            socket.close();
        } catch (IOException e) {
            System.out.println("No I/O");
            throw new RuntimeException(e);
        }
    }
}