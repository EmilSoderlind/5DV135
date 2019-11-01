package Utils;

/**
 **
 * Represents an operation that accepts a single input argument and returns no
 * result.
 *
 * @param <T> the type of the input parameter
 */
@FunctionalInterface
public interface Callback<T> {

    /**
     *
     * Performs this operation on the given arguments.
     *
     * @param data to process
     */
    void callBack(T data);

}
