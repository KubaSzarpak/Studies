/**
 * @author Szarpak Jakub S25511
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ChatClient {
    private final SocketChannel socket;
    public final String id;
    private final List<String> responses;
    private final Thread readingThread;
    private volatile boolean run;

    public ChatClient(String host, int port, String id) {
        InetSocketAddress address = new InetSocketAddress(host, port);
        this.id = id;
        responses = new ArrayList<>();

        try {
            socket = SocketChannel.open();
            socket.connect(address);
            socket.configureBlocking(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        readingThread = new Thread(){
            @Override
            public synchronized void run() {
                run = true;
                while (run) {

                        String response;
                        ByteBuffer bbuf = ByteBuffer.allocate(1024);

                        Charset charset = Charset.defaultCharset();

                        StringBuilder reqString = new StringBuilder();


                        boolean loop = true;
                        while (loop) {
                            int n = 0;
                            try {
                                n = socket.read(bbuf);
                            } catch (IOException e) {
//                                throw new RuntimeException(e);
                            }
                            if (n > 0) {
                                bbuf.flip();
                                CharBuffer cbuf = charset.decode(bbuf);
                                while (cbuf.hasRemaining()) {
                                    char c = cbuf.get();
                                    if (c == '\r') {
                                        loop = false;
                                        break;
                                    }

                                    reqString.append(c);
                                }
                            }
                        }
                        response = reqString.toString();
//            System.out.println(id + " - " + response + "\n");
                        responses.add(response);

                }
            }
        }; //reading thread

    }

    public void login() {
        readingThread.start();
        send("login\t" + id);
    }

    public void logout() {
        send("logout\t" + id);

        try {
            Thread.sleep(500);
            run = false;
            readingThread.interrupt();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            socket.socket().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String req) {
        try {
            ByteBuffer bbuf = ByteBuffer.allocate(1024);
            bbuf.put(req.getBytes());
            bbuf.put("\r".getBytes());

            bbuf.flip();
            socket.write(bbuf);

            bbuf.clear();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized String getChatView() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("=== ").append(id).append(" chat view\n");

        for (String s : responses) {
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }
}
