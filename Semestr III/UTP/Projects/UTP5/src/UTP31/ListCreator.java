/**
 * @author Szarpak Jakub S25511
 */

package UTP31;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ListCreator<T> {
    private List<T> list;

    public ListCreator(List<T> list) {
        this.list = list;
    }


    public static <T> ListCreator<T> collectFrom(final List<T> src) {
        return new ListCreator<>(src);
    }


    public ListCreator<T> when(Predicate<T> pred) {
        List<T> tmp = new ArrayList<>();
        for (T item : list) {
            if (pred.test(item))
                tmp.add(item);
        }
        list = tmp;
        return this;
    }

    public <X> List<X> mapEvery(Function<T, X> fun) {
        List<X> tmp = new ArrayList<>();
        for (T item : list) {
            tmp.add(fun.apply(item));
        }
        return tmp;
    }
}
