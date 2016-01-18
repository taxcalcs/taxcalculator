package info.kuechler.bmf.taxcalculator.rw;

/**
 * Exception class.
 */
public class ReadWriteException extends Exception {

    private static final long serialVersionUID = -2901644753375539302L;

    /**
     * Constructor.
     * 
     * @param message
     *            the message
     * @param cause
     *            the cause exception
     */
    public ReadWriteException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor.
     * 
     * @param message
     *            the message
     */
    public ReadWriteException(String message) {
        super(message);
    }
}
