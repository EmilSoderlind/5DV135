package Model.Units;

/**
 * Specific Exception used when invoking tiles from XML.
 */
public class TileInvokeException extends RuntimeException{
    private String message = "";
    public TileInvokeException(String message){
        this.message = message;
    }

    public TileInvokeException(String message, Throwable cause){
        super(message, cause);
    }

    @Override
    public String toString() {
        return this.getCause() != null ? getMessage() + ": " + getCause() :
                getMessage();
    }
}
