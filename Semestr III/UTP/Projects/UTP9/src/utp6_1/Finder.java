/**
 * @author Szarpak Jakub S25511
 */

package utp6_1;


import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Finder {
    private final String path;
    private final List<String> file;
    private int ifNumber;
    private int warNumber;


    public Finder(String path) {
        this.path = path;
        file = new ArrayList<>();
        ifNumber = 0;
        warNumber = 0;

        readFile();
    }

    private void readFile() {
        try {
            Scanner reader = new Scanner(new FileReader(path));
            while (reader.hasNext()) {
                file.add(reader.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public int getIfCount() {
        StringBuilder word;
        List<String> words;
        boolean isLargeComment = false;

        Pattern p1 = Pattern.compile("//");
        Pattern p2 = Pattern.compile("/[*]");
        Pattern p3 = Pattern.compile("[*]/");
        Matcher matcher;

        for (String line : file) {
            word = new StringBuilder();
            words = new ArrayList<>();

            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);

                if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c == '/' || c == '*' || c == '"')) {
                    word.append(c);
                } else {
                    if (word.length() == 0) {
                        continue;
                    }

                    matcher = p1.matcher(word.toString());
                    if (matcher.find()) {
                        continue;
                    } //If finds "//" it means the comment is open and it skips the rest of line

                    matcher = p2.matcher(word.toString());
                    if (matcher.find()) {
                        isLargeComment = true;
                    } //If finds "/*" it means that the large comment is open

                    matcher = p3.matcher(word.toString());
                    if (matcher.find() && isLargeComment) {
                        isLargeComment = false;
                    } //If finds "*/" and the large comment is open then it means the large comment have to be closed here

                    if (!isLargeComment) {
                        if (!(word.toString().charAt(0) == '"')) {
                            words.add(word.toString());
                            word.delete(0, word.length());
                        }
                    }

                }
            }
            matcher = p1.matcher(word.toString());
            if (matcher.find()) {
                word = new StringBuilder(word.substring(matcher.regionStart(), matcher.start()));
            } //If finds "//" it means the comment is open and it skips the rest of line

            matcher = p2.matcher(word.toString());
            if (matcher.find()) {
                isLargeComment = true;
            } //If finds "/*" it means that the large comment is open

            matcher = p3.matcher(word.toString());
            if (matcher.find() && isLargeComment) {
                isLargeComment = false;
            } //If finds "*/" and the large comment is open then it means the large comment have to be closed here
            if (!isLargeComment) {
                if (word.length() != 0) {
                    if (!(word.toString().charAt(0) == '"')) {
                        words.add(word.toString());
                        word.delete(0, word.length());
                    }
                }
            }

            for (String item : words) {
                switch (item){
                    case "if" : ifNumber++;
                    break;
                }
            }
        }


        return ifNumber;
    }

    public int getStringCount(String wariant) {
        StringBuilder word;
        List<String> words;
        Pattern p1 = Pattern.compile("wariant");
        Matcher matcher;

        for (String line : file) {
            word = new StringBuilder();
            words = new ArrayList<>();
            String tmpLine;
            matcher = p1.matcher(line);
            if (!matcher.find())
                continue;
            else {
                tmpLine = line.substring(matcher.start(), matcher.regionEnd());
            }


            //przeszukiwanie jedynie miejsc, gdzie może wystąpić słowo "wariant"
            for (int i = 0; i < tmpLine.length(); i++) {
                char c = tmpLine.charAt(i);

                if (c == 'a' || c == 'i' || c == 'n' || c == 't' || c == 'r' || c == 'w') {
                    word.append(c);
                    if (word.toString().equals("wariant")) {
                        words.add(word.toString());
                        word.delete(0, word.length());
                    }
                }
            }


            for (String item : words) {
                if (item.equals(wariant))
                    warNumber++;
            }
        }

        return warNumber;
    }
}
