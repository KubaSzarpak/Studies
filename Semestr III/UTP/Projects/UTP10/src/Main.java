import java.lang.invoke.LambdaMetafactory;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        Map<Integer, Integer> map = new HashMap<>();

        map.put(1, 2);
        map.put(3, 10);
        map.put(2, 1);
        map.put(4, 11);

        filtered(map, o -> o.getValue()>10).forEach((k,v)->System.out.println(k+ " = " + v));
    }

    public static <X, Y> Map<X, Y> filtered(Map<X, Y> map, Predicate<Map.Entry<X, Y>> predicate) {
        Map<X, Y> result = new LinkedHashMap<>();
        List<Map.Entry<X, Y>> list = new ArrayList<>(map.entrySet());

        list.removeIf(predicate.negate());

        for (Map.Entry<X, Y> item : list)
            result.put(item.getKey(), item.getValue());

        return result;
    }
}
