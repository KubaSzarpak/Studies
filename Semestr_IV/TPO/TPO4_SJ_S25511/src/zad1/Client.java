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

public class Client {
    private SocketChannel socket;
    private final InetSocketAddress address;

    private final String id;

    public Client(String host, int port, String id) {
        this.id = id;
        address = new InetSocketAddress(host, port);
    }

    public void connect() {
        try {
            socket = SocketChannel.open();
            socket.connect(address);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String send(String message) {
        String response;
        try {
            ByteBuffer bbuf = ByteBuffer.allocate(1024);
            bbuf.put(message.getBytes());
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    public String getId() {
        return id;
    }
}
