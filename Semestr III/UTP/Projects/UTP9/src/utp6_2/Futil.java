package utp6_2;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;

public class Futil{

    public static void processDir(String dirName, String resultFileName) {
        try {
            Files.walkFileTree(Paths.get(dirName), new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    final FileWriter writer = new FileWriter(resultFileName, true);
                    Scanner reader = new Scanner(file, "windows-1250");



                    while (reader.hasNext()){
                        String line = reader.nextLine();
                        System.out.println(line);
                        writer.write(line +"\n");
                    }
                    writer.close();

                    return super.visitFile(file, attrs);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
