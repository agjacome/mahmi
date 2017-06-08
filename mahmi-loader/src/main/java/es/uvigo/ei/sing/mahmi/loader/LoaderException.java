package es.uvigo.ei.sing.mahmi.loader;

/**
 * {@linkplain LoaderException} is the MAHMI {@code Loader} custom {@linkplain RuntimeException}
 * 
 * @author Alberto Gutierrez-Jacome
 *
 */
public final class LoaderException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of {@code LoaderException} from a message
     * 
     * @param message The exception message
     */
    private LoaderException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance of {@code LoaderException} from a cause
     * 
     * @param cause The exception cause
     */
    private LoaderException(final Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance of {@code LoaderException} from a message and a cause
     * 
     * @param message The exception message
     * @param cause The exception cause
     */
    private LoaderException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor of {@code LoaderException} from a message
     * 
     * @param message The exception message
     * @return A new instance of {@code LoaderException}
     */
    public static LoaderException withMessage(final String message) {
        return new LoaderException(message);
    }

    /**
     * Constructor of {@code LoaderException} from a cause
     * 
     * @param cause The exception cause
     * @return A new instance of {@code LoaderException}
     */
    public static LoaderException withCause(final Throwable cause) {
        return new LoaderException(cause);
    }

    /**
     * Constructor of {@code LoaderException} from a message and a cause
     * 
     * @param message The exception message
     * @param cause The exception cause
     * @return A new instance of {@code LoaderException}
     */
    public static LoaderException withMessageAndCause(
        final String message, final Throwable cause
    ) {
        return new LoaderException(message, cause);
    }

}
