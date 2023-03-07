package zad1;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Futil {
    public static void processDir(String dirName, String resultFileName) {

        try {
            Files.walkFileTree(Paths.get(dirName), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Charset in = Charset.forName("Cp1250");
                    Charset out = StandardCharsets.UTF_8;


                    FileChannel channel = FileChannel.open(Paths.get(file.toUri()));
                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect((int)channel.size());
                    channel.read(byteBuffer);
                    byteBuffer.flip();

                    CharBuffer charBuffer = in.decode(byteBuffer);
                    ByteBuffer byteBufferOut = out.encode(charBuffer);

                    FileChannel channelOut = FileChannel.open(Paths.get(resultFileName), StandardOpenOption.WRITE);

                    channelOut.write(byteBufferOut);

                    return super.visitFile(file, attrs);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
