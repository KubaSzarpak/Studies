package UTP45;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;


public class XList<T> extends ArrayList<T> {


    @SafeVarargs
    public XList(T... args) {
        super.addAll(List.of(args));
    }

    public XList(Collection<? extends T> c) {
        super.addAll(c);
    }


    @SafeVarargs
    public static <X> XList<X> of(X... args) {
        return new XList<>(args);
    }

    public static <X> XList<X> of(Collection<? extends X> args) {
        return new XList<>(args);
    }

    public static XList<String> charsOf(String arg) {
        String[] arr = new String[arg.length()];

        for (int i = 0; i < arg.length(); i++) {
            arr[i] = String.valueOf(arg.charAt(i));
        }

        return new XList<>(arr);
    }

    public static XList<String> tokensOf(String arg) {
        StringBuilder sb = new StringBuilder();
        List<String> tmp = new ArrayList<>();
        for (int i = 0; i < arg.length(); i++) {
            char c = arg.charAt(i);
            if (c == ' ') {
                tmp.add(sb.toString());
                sb.delete(0, sb.length());
            } else {
                sb.append(c);
            }
        }
        tmp.add(sb.toString());
        return new XList<>(tmp);
    }

    public static XList<String> tokensOf(String arg, String sep) {
        StringBuilder sb = new StringBuilder();
        List<String> tmp = new ArrayList<>();
        for (int i = 0; i < arg.length(); i++) {
            char c = arg.charAt(i);
            if (c == sep.charAt(0)) {
                tmp.add(sb.toString());
                sb.delete(0, sb.length());
            } else {
                sb.append(c);
            }
        }
        tmp.add(sb.toString());
        return new XList<>(tmp);
    }

    public XList<T> union(Collection<? extends T> args) {
        XList<T> tmp = new XList<>(this);
        tmp.addAll(args);
        return tmp;
    }

    @SafeVarargs
    public final XList<T> union(T... args) {
        XList<T> tmp = new XList<>(this);
        tmp.addAll(List.of(args));
        return tmp;
    }

    public XList diff(Collection<? extends T> args) {
        XList<T> tmp = new XList<T>();

        for (int i = 0; i < this.size(); i++) {
            boolean exist = false;

            for (T item : args){
                if (this.get(i) == item){
                    exist = true;
                    break;
                }
            }

            if(!exist){
                tmp.add(this.get(i));
            }
        }
        return tmp;
    }

    public XList<T> unique(){
        XList<T> tmp = new XList<>();

        for (int i = 0; i < this.size(); i++){
            boolean exist = false;

            for (T item : tmp){
                if(this.get(i) == item){
                    exist = true;
                    break;
                }
            }

            if (!exist) {
                tmp.add(this.get(i));
            }
        }

        return tmp;
    }

    public XList<XList<T>> combine(){
        XList<XList<T>> xList = combine(0, (XList<XList<T>>) this);
        xList.forEach(list -> Collections.reverse(list));
        return xList;
    }
    private static <T> XList<XList<T>> combine(int index, XList<XList<T>> args) {
        XList<XList<T>> a = new XList<>();

        if (index == args.size()) {
            a.add(new XList<>());
        } else {
            for (Object obj : args.get(index)) {
                for (XList<T> item : combine(index + 1, args)) {
                    item.add((T) obj);
                    a.add(item);
                }
            }
        }

        return a;
    }

    public <X> XList<X> collect(Function<T, X> func) {
        XList<X> tmp = new XList<X>();

        for (T item : this)
            tmp.add(func.apply(item));

        return tmp;
    }

    public String join () {
       StringBuilder sb = new StringBuilder();

       for (T item : this) {
           sb.append(item);
       }
       return sb.toString();
    }

    public String join (String sep) {
        StringBuilder sb = new StringBuilder();

        for (T item : this) {
            sb.append(item + sep);
        }
        return sb.toString();
    }

    public void forEachWithIndex (BiConsumer<T, Integer> c){
        for (int i = 0; i < this.size(); i++) {
            c.accept(this.get(i), i);
        }

    }
}
