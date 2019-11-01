package Utils;
import Model.MapValidationException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * Error handler for SAX-parsing.
 */
public class ValidationErrorHandler implements ErrorHandler {
    private boolean isValid = true;
    private String errorMessage = "";
    private Callback<Exception> exceptionCallback;

    /**
     *
     * @param exceptionCallback used to handle exceptions
     */
    public ValidationErrorHandler(Callback<Exception> exceptionCallback){
        this.exceptionCallback = exceptionCallback;
    }

    /**
     * @return true if the XML could be parsed correctly, else false
     */
    public boolean isValid() {
        return this.isValid;
    }

    @Override
    public void warning(SAXParseException e) {
        isValid = false;
        String warnigEx = "Map validation threw a warning. Message: %s";
        errorMessage = String.format(warnigEx, e.getLocalizedMessage());
        exceptionCallback.callBack(new MapValidationException(errorMessage));
    }

    @Override
    public void error(SAXParseException e) {
        this.isValid = false;
        String errorEx = "Map validation threw an error. Message: %s";
        errorMessage = String.format(errorEx, e.getLocalizedMessage());
        exceptionCallback.callBack(new MapValidationException(errorMessage));
    }

    @Override
    public void fatalError(SAXParseException e) {
        this.isValid = false;
        String fatalEx = "Map validation threw hoverX mayor error. Message: %s";
        errorMessage = String.format(fatalEx, e.getLocalizedMessage());
        exceptionCallback.callBack(new MapValidationException(errorMessage));
    }
}