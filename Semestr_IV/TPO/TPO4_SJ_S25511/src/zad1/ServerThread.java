package zad1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.time.LocalTime;
import java.util.*;

public class ServerThread extends Thread{
    private ServerSocketChannel server;
    private Selector selector;
    private List<String> serverLog;
    private Map<SelectionKey, List<String>> userLogs;
    private Map<SelectionKey, String> names;
    public boolean running;

    public ServerThread(ServerSocketChannel server, Selector selector) {
        this.server = server;
        this.selector = selector;
        serverLog = new ArrayList<>();
        userLogs = new HashMap<>();
        names = new HashMap<>();
    }

    public String getServerLog() {
        StringBuilder logString = new StringBuilder();
        for (String s : serverLog)
            logString.append(s).append("\n");

        return logString.toString();
    }

    private String getUserLog(SelectionKey key){
        StringBuilder logString = new StringBuilder();

        for (String s : userLogs.get(key)){
            logString.append(s).append("\n");
        }

        return logString.toString();
    }

    @Override
    public void run() {

        running = true;
        while (running) {
            try {
                selector.select();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iter = keys.iterator();

            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();

                if(!userLogs.containsKey(key)) {
                    userLogs.put(key, new ArrayList<>());
                    names.put(key, "");
                }




                if (key.isAcceptable()) {
                    try {
                        SocketChannel socketChannel = server.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        continue;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (key.isReadable()) {
                    try {
                        handleRequest(key);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        try {
            server.close();
            server.socket().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final Charset charset = Charset.defaultCharset();

    private void handleRequest(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();
        if (!sc.isOpen()) return;

        StringBuilder reqString = new StringBuilder();
        ByteBuffer bbuf = ByteBuffer.allocate(1024);

        boolean loop = true;
        while (loop) {
            int n = sc.read(bbuf);
            if (n > 0) {
                bbuf.flip();
                CharBuffer cbuf = charset.decode(bbuf);
                while (cbuf.hasRemaining()) {
                    char c = cbuf.get();
                    if (c == '\r' || c == '\n') {
                        loop = false;
                        break;
                    }

                    reqString.append(c);
                }
            }
        }

        recognizeRequest(key, reqString.toString());
    }

    private synchronized void recognizeRequest(SelectionKey key, String request) {
        String[] arr = request.split(" ");
        SocketChannel clientSocket = (SocketChannel)key.channel();

        switch (arr[0]) {
            case "login":
                handleWriter(clientSocket, "logged in");
                userLogs.get(key).add("logged in");
                serverLog.add(arr[1] + " logged in at " + LocalTime.now());
                names.put(key, arr[1]);
                break;
            case "bye":
                userLogs.get(key).add("logged out");
                serverLog.add(names.get(key) + " logged out at " + LocalTime.now());
                if (arr.length > 1)
                    handleWriter(clientSocket, "=== " + names.get(key) + " log start ===\n" + getUserLog(key) + "=== " + names.get(key) + " log end ===\n");
                else
                    handleWriter(clientSocket, "logged out");

                try {
                    clientSocket.close();
                    clientSocket.socket().close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                String result = Time.passed(arr[0], arr[1]);
                handleWriter(clientSocket, result);
                userLogs.get(key).add("Request: " + request + "\nResult:\n" + result);
                serverLog.add(names.get(key) + " request at " + LocalTime.now() + ": \"" + arr[0] + " " + arr[1] + "\"");
        }
    }



    private void handleWriter(SocketChannel sc, String addMsg) {
        ByteBuffer buf = ByteBuffer.allocate(1024);

        buf.put(addMsg.getBytes());
        buf.put("\r".getBytes());
        buf.flip();
        try {
            sc.write(buf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
