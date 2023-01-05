package Code;

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

public class DatabaseNodeCenter {
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
            writer.println(PORT);

            String line = read.readLine();

            if (line.equals("ERROR")) {
                System.out.println(line);
                System.out.println(read.readLine());
                return;
            } else {
                nextNodePort = Integer.parseInt(line);
                nextNodeAddress = getByName(read.readLine());
                nodeCommunication.sendMsg("newPrevious " + InetAddress.getByName(ADDRESS) + ":" + PORT, nextNodeAddress, nextNodePort);

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
        Pattern findPortPattern = Pattern.compile("find-port");

        Matcher matcher = checkPortPattern.matcher(task.substring(0, 4));

        if (matcher.matches()) {
            addYourPort = false;
            sendNext = (nextNodePort != Integer.parseInt(task.substring(0, 4)));
        } else {
            addYourPort = true;
            sendNext = (nextNodePort != PORT);
        }
        matcher = findPortPattern.matcher(task);
        if (matcher.find()) {
            answer = findPort(Integer.parseInt(task.substring(matcher.end() + 1)),
                    addYourPort ? "" : task.substring(0, matcher.start()),
                    addYourPort);
            return endOperate(task, answer);
        }

        matcher = setValuePattern.matcher(task);
        if (matcher.find()) {
            String[] tmp = task.substring(matcher.end() + 1).split(":");
            answer = setValue(Integer.parseInt(tmp[0]),
                    Integer.parseInt(tmp[1]),
                    addYourPort ? "" : task.substring(0, matcher.start()),
                    addYourPort);
            return endOperate(task, answer);
        }

        matcher = getValuePattern.matcher(task);
        if (matcher.find()) {
            answer = getValue(Integer.parseInt(task.substring(matcher.end() + 1)),
                    addYourPort ? "" : task.substring(0, matcher.start()),
                    addYourPort);
            return endOperate(task, answer);
        }

        matcher = findKeyPattern.matcher(task);
        if (matcher.find()) {
            answer = findKey(Integer.parseInt(task.substring(matcher.end() + 1)),
                    addYourPort ? "" : task.substring(0, matcher.start()),
                    addYourPort);
            return endOperate(task, answer);
        }

        matcher = getMaxPattern.matcher(task);
        if (matcher.find()) {
            if (task.substring(4).length() > getMaxPattern.pattern().length()) {
                answer = getMax(Integer.parseInt(task.substring(matcher.end() + 1)),
                        addYourPort ? "" : task.substring(0, matcher.start()),
                        addYourPort);
            } else {
                answer = getMax(this.value,
                        addYourPort ? "" : task.substring(0, matcher.start()),
                        addYourPort);
            }
            return endOperate(task, answer);
        }

        matcher = getMinPattern.matcher(task);
        if (matcher.find()) {
            if (task.substring(4).length() > getMaxPattern.pattern().length()) {
                answer = getMin(Integer.parseInt(task.substring(matcher.end() + 1)),
                        addYourPort ? "" : task.substring(0, matcher.start()),
                        addYourPort);
            } else {
                answer = getMin(this.value,
                        addYourPort ? "" : task.substring(0, matcher.start()),
                        addYourPort);
            }
            return endOperate(task, answer);
        }

        matcher = newRecordPattern.matcher(task);
        if (matcher.find()) {
            String[] tmp = task.substring(matcher.end() + 1).split(":");
            newRecord(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]));
            answer = "OK";
            return endOperate(task, answer);
        }

        matcher = terminatePattern.matcher(task);
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
            return endOperate(task, answer);
        }

        matcher = newNextPattern.matcher(task);
        if (matcher.find()) {
            String[] tmp = task.substring(matcher.end() + 1).split(":");
            try {
                nextNodeAddress = getByName(tmp[0].substring(1));
                nextNodePort = Integer.parseInt(tmp[1]);
                answer = "null";
                return endOperate(task, answer);
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
                answer = "null";
                return endOperate(task, answer);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }


        return endOperate(task, answer);
    }

    private String endOperate(String task, String answer) {
        prepareCommunication(500);
        wait = false;
        return answer.isEmpty() ? task : answer;
    }

    private String forwardRequest(String msg, String failure) {
        if (sendNext) {
            nodeCommunication.sendMsg(msg, nextNodeAddress, nextNodePort);
            return nodeCommunication.receiveMsg();
        } else {
            return failure;
        }
    }

    private String setValue(int key, int value, String prefix, boolean addYourPort) {
        if (this.key == key) {
            this.value = value;
            return "OK";
        }
        return forwardRequest((addYourPort ? PORT + "set-value " : prefix + "set-value ") + key + ":" + value, "ERROR");
    }

    private String getValue(int key, String prefix, boolean addYourPort) {
        if (this.key == key)
            return key + ":" + value;
        return forwardRequest((addYourPort ? PORT + "get-value " : prefix + "get-value ") + key, "ERROR");

    }

    private String findKey(int key, String prefix, boolean addYourPort) {
        if (this.key == key)
            return ADDRESS + ":" + PORT;
        return forwardRequest((addYourPort ? PORT + "find-key " : prefix + "find-key ") + key, "ERROR");

    }

    private String getMax(int max, String prefix, boolean addYourPort) {
        int newMax = Math.max(this.value, max);
        return forwardRequest((addYourPort ? PORT + "get-max " : prefix + "get-max ") + newMax, String.valueOf(newMax));
    }

    private String getMin(int min, String prefix, boolean addYourPort) {
        int newMin = Math.min(this.value, min);
        return forwardRequest((addYourPort ? PORT + "get-min " : prefix + "get-min ") + newMin, String.valueOf(newMin));
    }

    private void newRecord(int key, int value) {
        this.key = key;
        this.value = value;
    }

    private void terminate() {
        nodeCommunication.sendMsg("newPrevious " + previousNodeAddress + ":" + previousNodePort, nextNodeAddress, nextNodePort);
        nodeCommunication.sendMsg("newNext " + nextNodeAddress + ":" + nextNodePort, previousNodeAddress, previousNodePort);
    }

    public String findPort(int port, String prefix, boolean addYourPort) {
        if (PORT == port)
            return "ERROR";
        return forwardRequest((addYourPort ? PORT + "find-port " : prefix + "find-port ") + port, "OK");
    }
}