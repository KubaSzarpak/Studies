import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DatabaseNode class recognizes information given in arguments of project to start the server. It uses "recognizeCommand()" method to assign the fields.
 * After recognizing and assigning the fields, "main()" method can set up the server.
 * <p>
 * DatabaseNode can be created with or without stored value.
 * <p>
 * If server need to be hooked to the web, then "connect()" method need to be called with destination address and destination port.
 * Connecting server to the node have to be done very precisely, because if node that you try to connect already have connected node, then you should not try to connect with it. It will break the existing connection and replace it with yours.
 * @author Jakub Szarpak
 */
public class DatabaseNode {
    /**
     * Fields needed to set up the server.
     *  port - port of yours server.
     *  destinationPort - node's port that you try to connect.
     *  destinationAddress - node's ip address that you try to connect.
     *  key - key of a value that you will be storing in this server.
     *  value - value that you will be storing in this server.
     */
    private static int port;
    private static String address;
    private static int destinationPort;
    private static String destinationAddress;
    private static int key;
    private static int value;

    /**
     * Handles information given in args by calling recognizeCommand() method.
     * Information are assigned to the corresponding fields.
     * Then DatabaseNodeCenter class is created with the corresponding values from the fields.
     * If "port" field has value of -1, then it means the node tried to be created without "-tcpport" command. This means the node can not be created. "Error arguments - please correct issues" is printed and program exits with code -1.
     *
     * @param args arguments given while running program. For example "-tcpport 4445 -record 17:256".
     */
    public static void main(String[] args) {
        recognizeCommand(args);

        if (port == -1){
            System.out.println("Error arguments - please correct issues");
            System.exit(-1);
        }

        DatabaseNodeCenter server;
        if (key != -1 && value != -1) {
            server = new DatabaseNodeCenter(port, address, key, value);
        } else {
            server = new DatabaseNodeCenter(port, address);
        }

        if (destinationPort != -1 && !destinationAddress.equals(""))
            server.connect(destinationAddress, destinationPort);
    }

    /**
     * Recognizes commands and corresponding values and assigns them to corresponding fields.
     * It starts by assigning error values to corresponding fields. Error values are "-1" and "".
     * Then it assigns local host ip address to "address" field and recognizes rest of the values given in parameter.
     * @param commands arguments given while running program. For example "-tcpport 4445 -record 17:256".
     */
    private static void recognizeCommand(String[] commands) {
        key = -1;
        value = -1;

        destinationPort = -1;
        destinationAddress = "";

        port = -1;

        try {
            address = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }



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
                if (tmp[0].equals("localhost")){
                    destinationAddress = address;
                } else {
                    destinationAddress = tmp[0];
                }
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
