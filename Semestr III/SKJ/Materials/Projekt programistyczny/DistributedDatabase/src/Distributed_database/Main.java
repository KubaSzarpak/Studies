package Distributed_database;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static int port;
    private static int destinationPort;
    private static String destinationAddress;
    private static int key;
    private static int value;


    public static void main(String[] args) {
        recognizeCommand(args);

        DatabaseNode server;
        if (key != -1 && value != -1) {
            server = new DatabaseNode(port, "192.168.8.176", key, value);
        } else {
            server = new DatabaseNode(port, "192.168.8.176");
        }

        if (destinationPort != -1 && !destinationAddress.equals(""))
            server.connect(destinationAddress, destinationPort);
    }

    private static void recognizeCommand(String[] commands) {
        key = -1;
        value = -1;

        destinationPort = -1;
        destinationAddress = "";

        Pattern portPattern = Pattern.compile("-tcpport");
        Pattern connectionPattern = Pattern.compile("-connect");
        Pattern recordPattern = Pattern.compile("-record");

        Matcher matcher;

        for (int i = 0; i < commands.length; i++) {
            matcher = portPattern.matcher(commands[i]);
            if (matcher.find()){
                port = Integer.parseInt(commands[i + 1]);
                continue;
            }

            matcher = connectionPattern.matcher(commands[i]);
            if (matcher.find()) {
                String[] tmp = commands[i + 1].split(":");
                destinationAddress = tmp[0];
                destinationPort = Integer.parseInt(tmp[1]);
                continue;
            }

            matcher = recordPattern.matcher(commands[i]);
            if (matcher.find()){
                String[] tmp = commands[i + 1].split(":");
                key = Integer.parseInt(tmp[0]);
                value = Integer.parseInt(tmp[1]);
            }
        }
    }
}
