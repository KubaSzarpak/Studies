/**
 *
 *  @author Szarpak Jakub S25511
 *
 */

package UTP42;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/*<-- niezbędne import */
public class Main {
  public static void main(String[] args) throws IOException {
    FunctionThrowingExeption<String, List<String>> flines = fname -> {
      List<String> list = new ArrayList<>();

      BufferedReader reader;

        reader = new BufferedReader(new FileReader(fname));

        String line = reader.readLine();
        while (line != null) {
          list.add(line);
          line = reader.readLine();
        }
        reader.close();

      return list;
    };

    Function<List<String>, String> join = strings -> {
      StringBuilder sb = new StringBuilder();
      for (String item : strings) {
        sb.append(item);
      }
      return sb.toString();
    };

    Function<String, List<Integer>> collectInts = s -> {
      List<Integer> list = new ArrayList<>();
      StringBuilder sb = new StringBuilder();
      boolean isStreak = false;

      for (int i = 0; i < s.length(); i++) {
        char a = s.charAt(i);

        if (a >= '0' && a <= '9') {
          sb.append(a);
          isStreak = true;
        } else {
          if(isStreak){
            list.add(Integer.parseInt(sb.toString()));
            isStreak = false;
            sb.delete(0, sb.length());
          }
        }
      }

      if(isStreak){
        list.add(Integer.parseInt(sb.toString()));
      }

      return list;
    };

    Function<List<Integer>, Integer> sum = integers -> {
      int sum1 = 0;

      for (Integer item : integers){
        sum1 += item;
      }

      return sum1;
    };
    /*<--
     *  definicja operacji w postaci lambda-wyrażeń:
     *  - flines - zwraca listę wierszy z pliku tekstowego
     *  - join - łączy napisy z listy (zwraca napis połączonych ze sobą elementów listy napisów)
     *  - collectInts - zwraca listę liczb całkowitych zawartych w napisie
     *  - sum - zwraca sumę elmentów listy liczb całkowitych
     */

    String fname = System.getProperty("user.home") + "/LamComFile.txt";
    InputConverter<String> fileConv = new InputConverter<>(fname);
    List<String> lines = fileConv.convertBy(flines);
    String text = fileConv.convertBy(flines, join);
    List<Integer> ints = fileConv.convertBy(flines, join, collectInts);
    Integer sumints = fileConv.convertBy(flines, join, collectInts, sum);

    System.out.println(lines);
    System.out.println(text);
    System.out.println(ints);
    System.out.println(sumints);

    List<String> arglist = Arrays.asList(args);
    InputConverter<List<String>> slistConv = new InputConverter<>(arglist);  
    sumints = slistConv.convertBy(join, collectInts, sum);
    System.out.println(sumints);

    // Zadania badawcze:
    // Operacja flines zawiera odczyt pliku, zatem może powstac wyjątek IOException
    // Wymagane jest, aby tę operację zdefiniowac jako lambda-wyrażenie
    // Ale z lambda wyrażeń nie możemy przekazywac obsługi wyjatków do otaczającego bloku
    // I wobec tego musimy pisać w definicji flines try { } catch { }
    // Jak spowodować, aby nie było to konieczne i w przypadku powstania wyjątku IOException
    // zadziałała klauzula throws metody main
  }
}
