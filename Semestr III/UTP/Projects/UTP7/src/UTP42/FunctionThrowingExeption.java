package UTP42;

import java.util.function.Function;

public interface FunctionThrowingExeption<X, Y> extends Function<X, Y> {
    default Y apply(X item) {
        try {
            return applyThrows(item);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    Y applyThrows(X item) throws Exception;
}
