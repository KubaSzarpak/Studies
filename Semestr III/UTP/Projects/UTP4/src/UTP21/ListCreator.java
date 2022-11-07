/**
 * @author Szarpak Jakub S25511
 */

package UTP21;

import java.util.ArrayList;
import java.util.List;

public class ListCreator<T> {
    private List<T> list;

    public ListCreator(List<T> list) {
        this.list = list;
    }


    public static <T> ListCreator<T> collectFrom(final List<T> src) {
        return new ListCreator<>(src);
    }

    public ListCreator<T> when(Selector<T> sel) {
        List<T> tmp = new ArrayList<>();
        for (T item : list) {
            if (sel.select(item))
                tmp.add(item);
        }
        list = tmp;
        return this;
    }

    public <X> List<X> mapEvery(Mapper<T, X> map) {
        List<X> tmp = new ArrayList<>();
        for (T item : list) {
            tmp.add(map.map(item));
        }
        return tmp;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
