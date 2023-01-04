package Distributed_database;

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

public class DatabaseNode {
    public final int PORT;
    public final String ADDRESS;
    private int nextNodePort;
    private InetAddress nextNodeAddress;
    private int previousNodePort;
    private InetAddress previousNodeAddress;
    private final NodeCommunication nodeCommunication;
    private final SocketListener socketListener;
    private int key;
    public int value;
    public boolean wait;
    public boolean running;
    private boolean sendNext;

    public DatabaseNode(int port, String address) {
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

    public DatabaseNode(int port, String address, int key, int value) {
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

    public int getNextNodePort() {
        return nextNodePort;
    }

    public InetAddress getNextNodeAddress() {
        return nextNodeAddress;
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

            BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            writer.println("Node");
            nextNodePort = Integer.parseInt(read.readLine());
            nextNodeAddress = getByName(read.readLine());

            socket.close();

            previousNodeAddress = new InetSocketAddress(destinationAddress, destinationPort).getAddress();
            previousNodePort = destinationPort;

            System.out.println("Connected to the node");
        } catch (IOException e) {
            System.out.println("No I/O");
            throw new RuntimeException(e);
        }
    }

    private void prepareCommunication(int timeout) {
        try {
            Thread.sleep(500);
            nodeCommunication.setTimeOut(timeout);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized String operate(String task) {
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

        Matcher matcher = checkPortPattern.matcher(task.substring(0, 4));

        if (matcher.matches()) {
            addYourPort = false;
            sendNext = (nextNodePort != Integer.parseInt(task.substring(0, 4)));
        } else {
            addYourPort = true;
            sendNext = (nextNodePort != PORT);
        }

        matcher = setValuePattern.matcher(task);
        if (matcher.find()) {
            String[] tmp = task.substring(matcher.end() + 1).split(":");
            answer += setValue(Integer.parseInt(tmp[0]),
                    addYourPort ? "" : task.substring(0, matcher.start()),
                    Integer.parseInt(tmp[1]),
                    addYourPort);

        }

        matcher = getValuePattern.matcher(task);
        if (matcher.find()) {
            answer += getValue(Integer.parseInt(task.substring(matcher.end() + 1)),
                    addYourPort ? "" : task.substring(0, matcher.start()),
                    addYourPort);
        }

        matcher = findKeyPattern.matcher(task);
        if (matcher.find()) {
            answer += findKey(Integer.parseInt(task.substring(matcher.end() + 1)),
                    addYourPort ? "" : task.substring(0, matcher.start()),
                    addYourPort);
        }

        matcher = getMaxPattern.matcher(task);
        if (matcher.find()) {
            answer += getMax(0,
                    addYourPort ? "" : task.substring(0, matcher.start()),
                    addYourPort);
        }

        matcher = getMinPattern.matcher(task);
        if (matcher.find()) {
            answer += getMin(9999999,
                    addYourPort ? "" : task.substring(0, matcher.start()),
                    addYourPort);
        }

        matcher = newRecordPattern.matcher(task);
        if (matcher.find()) {
            String[] tmp = task.substring(matcher.end() + 1).split(":");
            newRecord(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]));
            answer += "OK";
        }

        matcher = terminatePattern.matcher(task);
        if (matcher.find()) {
            terminate();
            answer += "OK";
            new Thread(() -> {
                try {
                    running = false;
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.exit(7);
            }).start();
        }

        matcher = newNextPattern.matcher(task);
        if (matcher.find()) {
            String[] tmp = task.substring(matcher.end() + 1).split(":");
            try {
                nextNodeAddress = getByName(tmp[0].substring(1));
                nextNodePort = Integer.parseInt(tmp[1]);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }

        matcher = newPreviousPattern.matcher(task);
        if (matcher.find()) {
            String[] tmp = task.substring(matcher.end() + 1).split(":");
            try {
                previousNodeAddress = InetAddress.getByName(tmp[0].substring(1));
                previousNodePort = Integer.parseInt(tmp[1]);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }

        prepareCommunication(500);
        wait = false;

        if (answer.isEmpty())
            return task;
        return answer;
    }

    private String setValue(int key, String predicate, int value, boolean addYourPort) {
        String answer;

        if (this.key == key) {
            this.value = value;
            answer = "OK";
        } else {
            if (sendNext) {
                String msg = addYourPort ? PORT + "set-value " : predicate + "set-value ";
                nodeCommunication.sendMsg(msg + key + ":" + value, nextNodeAddress, nextNodePort);
                answer = nodeCommunication.receiveMsg();
            } else {
                answer = "ERROR";
            }
        }
        return answer;
    }

    private String getValue(int key, String predicate, boolean addYourPort) {
        String answer;

        if (this.key == key) {
            answer = key + ":" + value;
        } else {
            if (sendNext) {
                String msg = addYourPort ? PORT + "get-value " : predicate + "get-value ";
                nodeCommunication.sendMsg(msg + key, nextNodeAddress, nextNodePort);
                answer = nodeCommunication.receiveMsg();
            } else {
                answer = "ERROR";
            }
        }
        return answer;
    }

    private String findKey(int key, String predicate, boolean addYourPort) {
        String answer;
        if (this.key == key) {
            answer = ADDRESS + ":" + PORT;
        } else {
            if (sendNext) {
                String msg = addYourPort ? PORT + "find-key " : predicate + "find-key ";
                nodeCommunication.sendMsg(msg + key, nextNodeAddress, nextNodePort);
                answer = nodeCommunication.receiveMsg();
            } else {
                answer = "ERROR";
            }
        }
        return answer;
    }

    private String getMax(int max, String predicate, boolean addYourPort) {
        int newMax = Math.max(this.value, max);

        if (sendNext) {
            String msg = addYourPort ? PORT + "get-max " : predicate + "get-max ";
            nodeCommunication.sendMsg(msg + newMax, nextNodeAddress, nextNodePort);
            return nodeCommunication.receiveMsg();
        }
        return String.valueOf(newMax);
    }

    private String getMin(int min, String predicate, boolean addYourPort) {
        int newMin = Math.min(this.value, min);

        if (sendNext) {
            String msg = addYourPort ? PORT + "get-min " : predicate + "get-min ";
            nodeCommunication.sendMsg(msg + newMin, nextNodeAddress, nextNodePort);
            return nodeCommunication.receiveMsg();
        }
        return String.valueOf(newMin);
    }

    private void newRecord(int key, int value) {
        this.key = key;
        this.value = value;
    }

    private void terminate() {
        if (sendNext)
            nodeCommunication.sendMsg("newPrevious " + previousNodeAddress + ":" + previousNodePort, nextNodeAddress, nextNodePort);

        if (previousNodePort != -1 && previousNodeAddress != null)
            nodeCommunication.sendMsg("newNext " + nextNodeAddress + ":" + nextNodePort, previousNodeAddress, previousNodePort);
    }


}