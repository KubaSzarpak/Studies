package UTP42;

import java.util.function.Function;

public class InputConverter<T> {
    private T arg;

    public InputConverter(T fname) {
        this.arg = fname;
    }

    public <X> X convertBy(Function... functions){
        Object input = arg;
        Object result = null;

        for (Function item : functions) {
            result = item.apply(input);
            input = result;
        }

        return (X) result;
    }

}
