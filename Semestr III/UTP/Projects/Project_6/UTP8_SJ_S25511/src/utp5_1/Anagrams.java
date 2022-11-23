/**
 * @author Szarpak Jakub S25511
 */

package utp5_1;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Anagrams {

    List<List<String>> anagramsList;
    List<String> allwords;

    public Anagrams(String path) {
        anagramsList = new ArrayList<>();
        allwords = new ArrayList<>();

        readFile(path);
    }

    void readFile(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            while (line != null) {
                allwords.addAll(Arrays.asList(line.split(" ")));
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean matchWords(String word1, String word2) {
        if (word1.length() != word2.length())
            return false;

        boolean equals = true;
        for (int i = 0; i < word2.length(); i++) {
            boolean equalLetter = false;
            for (int j = 0; j < word1.length(); j++) {
                if (word2.charAt(i) == word1.charAt(j)) {
                    equalLetter = true;
                    break;
                }
            }
            if (!equalLetter) {
                equals = false;
                break;
            }
        }

        return equals;
    }

    private void sortWords(String word) {

        for (List<String> list : anagramsList) {
            String s = list.get(0);

            if (matchWords(s, word)) {
                list.add(word);
                return;
            }
        }

        List<String> newList = new ArrayList<>();
        newList.add(word);
        anagramsList.add(newList);

    }

    public List<List<String>> getSortedByAnQty() {
        for (String word : allwords)
            sortWords(word);

        anagramsList.sort((Comparator.comparingInt(List::size)));
        Collections.reverse(anagramsList);
        return anagramsList;
    }

    private List<String> findAnagrams(String word) {
        List<String> result = new ArrayList<>();
        Boolean finded = true;
        for (List<String> list : anagramsList)
            if (matchWords(list.get(0), word)) {
                for (String s : list)
                    if (!s.equals(word))
                        result.add(s);
                finded = true;
                break;
            } else {
                finded = false;
            }
        if (finded)
            return result;
        else
            return null;
    }

    public String getAnagramsFor(String word) {
        StringBuilder sb = new StringBuilder();
        sb.append(word).append(": ");

        List<String> anagrams = findAnagrams(word);
        if (anagrams == null) {
            sb.append("null");
            return sb.toString();
        } else {
            sb.append("[");
        }

        for (String item : anagrams) {
            sb.append(item).append(", ");
        }

        sb.deleteCharAt(sb.length() - 2);
        sb.append("]");
        return sb.toString();
    }
}
