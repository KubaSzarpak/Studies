package UTP44;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Maybe<T> {
    T item;
    public Maybe(T param){
        item = param;
    }
    public Maybe(){
    }
    public static <X> Maybe<X> of (X x) {
        return new Maybe<X>(x);
    }

    public void ifPresent(Consumer cons) {
        if(this.item != null)
            cons.accept(this.item);
    }

    public <X> Maybe<X> map(Function<T, X> func) {
        if(this.item != null){
            return new Maybe<X>((X) func.apply(this.item));
        }
        return new Maybe<X>();
    }

    public T get() {
        if(this.item == null)
            throw new NoSuchElementException();
        else
            return this.item;
    }

    public boolean isPresent() {
        return this.item != null;
    }

    public T orElse(T defVal) {
        if (this.item != null)
            return this.item;
        else
            return defVal;
    }

    public Maybe<T> filter(Predicate<T> pred) {
        if(pred.test(this.item) || this.item == null)
            return this;
        else
            return new Maybe<T>();
    }

    @Override
    public String toString() {
        if (this.item != null)
            return "Maybe has value " + this.item;
        else
            return "Maybe is empty";
    }
}
