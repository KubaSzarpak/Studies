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
    private SocketChannel socket;
    private final InetSocketAddress address;
    public final String id;
    private List<String> responses;

    public ChatClient(String host, int port, String id) {
        this.address = new InetSocketAddress(host, port);
        this.id = id;
        responses = new ArrayList<>();
    }

    public void login() {
        try {
            socket = SocketChannel.open();
            socket.connect(address);
            socket.configureBlocking(false);
            send("login\t" + id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void logout() {
        try {
            send("logout\t" + id);
            socket.socket().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String req) {
        String response;
        try {
            ByteBuffer bbuf = ByteBuffer.allocate(1024);
            bbuf.put(req.getBytes());
            bbuf.put("\r".getBytes());

            bbuf.flip();
            socket.write(bbuf);

            bbuf.clear();

            Charset charset = Charset.defaultCharset();

            StringBuilder reqString = new StringBuilder();

            boolean loop = true;
            while (loop) {
                int n = socket.read(bbuf);
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public synchronized String getChatView() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("=== " + id + " chat view\n");

        for (String s : responses) {
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }
}
