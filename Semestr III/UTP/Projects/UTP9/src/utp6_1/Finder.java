/**
 * @author Szarpak Jakub S25511
 */

package utp6_1;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Finder {
    private String path;
    private int ifNumber;
    private int warNumber;
    boolean isLargeComment;

    public Finder(String path) {
        this.path = path;
        ifNumber = 0;
        warNumber = 0;
        isLargeComment = false;

        readFile();
    }

    private void readFile() {
        try {
            Scanner reader = new Scanner(new FileReader(path));
            String line;

            while (reader.hasNext()) {
                line = reader.nextLine();
                find(line);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void find(String line) {
        StringBuilder word = new StringBuilder();
        List<String> words = new ArrayList<>();

        Pattern p1 = Pattern.compile("//");
        Pattern p2 = Pattern.compile("/[*]");
        Pattern p3 = Pattern.compile("[*]/");
        Matcher m1;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c == '/' || c == '*' || c=='"')) {
                word.append(c);
            } else {
                if (word.length() != 0) {
                    m1 = p1.matcher(word.toString());
                    if (m1.find()) {
                        break;
                    }
                    m1 = p2.matcher(word.toString());
                    if (m1.find()) {
                        isLargeComment = true;
                    }
                    m1 = p3.matcher(word.toString());
                    if (m1.find()) {
                        isLargeComment = false;
                    }
                    if (!isLargeComment) {
                        if (!(word.toString().charAt(0) == '"')) {
                            words.add(word.toString());
                            word.delete(0, word.length());
                        }
                    }
                }
            }
        }

        if (word.length() != 0) {
            m1 = p1.matcher(word.toString()); // "//"
            if (!m1.find()) {


                m1 = p2.matcher(word.toString()); // "/*"
                if (m1.find()) {
                    isLargeComment = true;
                }
                m1 = p3.matcher(word.toString()); // "*/"
                if (m1.find()) {
                    isLargeComment = false;
                }
                if (!isLargeComment) {
                    if (!(word.toString().charAt(0) == '"')) {
                        words.add(word.toString());
                        word.delete(0, word.length());
                    }
                }
            }
        }

        for (String item : words) {
            switch (item) {
                case "if":
                    ifNumber++;
                    break;
                case "wariant":
                    warNumber++;
                    break;
            }
        }

    }

    public int getIfCount() {
        return ifNumber;
    }

    public int getStringCount(String wariant) {
        return warNumber;
    }
}
