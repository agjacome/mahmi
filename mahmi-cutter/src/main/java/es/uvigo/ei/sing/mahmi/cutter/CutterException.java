package es.uvigo.ei.sing.mahmi.cutter;


/**
 * {@linkplain CutterException} is the MAHMI {@code Cutter} custom {@linkplain RuntimeException}
 * 
 * @author Alberto Gutierrez-Jacome
 *
 */
public final class CutterException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of {@code CutterException} from a message
     * 
     * @param message The exception message
     */
    private CutterException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance of {@code CutterException} from a cause
     * 
     * @param cause The exception cause
     */
    private CutterException(final Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance of {@code CutterException} from a message and a cause
     * 
     * @param message The exception message
     * @param cause The exception cause
     */
    private CutterException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor of {@code CutterException} from a message
     * 
     * @param message The exception message
     * @return A new instance of {@code CutterException}
     */
    public static CutterException withMessage(final String message) {
        return new CutterException(message);
    }

    /**
     * Constructor of {@code CutterException} from a cause
     * 
     * @param cause The exception cause
     * @return A new instance of {@code CutterException}
     */
    public static CutterException withCause(final Throwable cause) {
        return new CutterException(cause);
    }

    /**
     * Constructor of {@code CutterException} from a message and a cause
     * 
     * @param message The exception message
     * @param cause The exception cause
     * @return A new instance of {@code CutterException}
     */
    public static CutterException withMessageAndCause(
        final String message, final Throwable cause
    ) {
        return new CutterException(message, cause);
    }

}
