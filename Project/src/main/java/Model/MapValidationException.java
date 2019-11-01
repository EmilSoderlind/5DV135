package Model;

/**
 * MapValidationException is used when validation Map objects.
 * @see Map
 */
public class MapValidationException extends Exception {
    public MapValidationException(String message){
        super(message);
    }

    /**
     *
     * @param message of the exception
     * @param cause of the exception
     */
    public MapValidationException(String message, Throwable cause){
        super(message, cause);
    }

    @Override
    public String toString() {
        return this.getCause() != null ? getMessage() + ": " + getCause() :
                getMessage();
    }
}
