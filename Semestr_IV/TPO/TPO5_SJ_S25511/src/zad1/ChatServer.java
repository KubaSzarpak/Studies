/**
 * @author Szarpak Jakub S25511
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.time.LocalTime;
import java.util.*;

public class ChatServer {
    private final String host;
    private final int port;
    private final List<String> serverLog;
    private final Map<SelectionKey, String> names;
    private final Map<SelectionKey, List<String>> responses;
    private Thread serverThread;
    private ServerSocketChannel server;
    private Selector selector;
    private volatile boolean running;

    public ChatServer(String host, int port) {
        this.host = host;
        this.port = port;

        serverLog = new ArrayList<>();
        names = new HashMap<>();
        responses = new HashMap<>();
    }


    public void startServer() {
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.socket().bind(new InetSocketAddress(host, port));

            selector = Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        serverThread = new Thread() {
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

                        if (!names.containsKey(key)) {
                            names.put(key, "");
                            responses.put(key, new ArrayList<>());
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
//                System.out.println(reqString);

                recognizeRequest(key, reqString.toString());
            }

            private synchronized void recognizeRequest(SelectionKey key, String request) {
                String[] arr = request.split("\t");
                SocketChannel clientSocket = (SocketChannel) key.channel();

                String text;
                switch (arr[0]) {
                    case "login":
                        text = arr[1] + " logged in";
                        serverLog.add(LocalTime.now() + " " + text);
                        names.put(key, arr[1]);
                        handleWriter(text, key);
                        break;
                    case "logout":
                        text = names.get(key) + " logged out";
                        serverLog.add(LocalTime.now() + " " + text);
                        handleWriter(text, key);

                        try {
                            clientSocket.close();
                            clientSocket.socket().close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        names.remove(key);
                        responses.remove(key);
                        break;
                    default:
                        text = names.get(key) + ": " + request;
                        handleWriter(text, key);
                        serverLog.add(LocalTime.now() + " " + text);
                }
            }


            private void handleWriter(String addMsg, SelectionKey currentKey) {
                for (SelectionKey k : responses.keySet()) {
                    responses.get(k).add(addMsg);
                    if (!addMsg.contains("\n"))
                        responses.get(k).add("\n");
                }

                ByteBuffer buf = ByteBuffer.allocate(1024);


                try {
                    for (String s : responses.get(currentKey))
                        buf.put(s.getBytes());
                    buf.put("\r".getBytes());
                    buf.flip();

                    SocketChannel clientSocket = (SocketChannel) currentKey.channel();
                    clientSocket.write(buf);
                    buf.clear();

                    responses.get(currentKey).clear();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        serverThread.start();
        System.out.println("server started\n");
    }

    public void stopServer() {
        running = false;
        serverThread.interrupt();
        System.out.println("server stopped");
    }

    public String getServerLog() {
        StringBuilder logString = new StringBuilder();
        for (String s : serverLog)
            logString.append(s).append("\n");

        return logString.toString();
    }
}
