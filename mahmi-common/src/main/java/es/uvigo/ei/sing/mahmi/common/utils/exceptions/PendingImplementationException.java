package es.uvigo.ei.sing.mahmi.common.utils.exceptions;

/**
 * Exception to denote a non-yet implemented method or class. Instead of
 * directly throwing an {@link UnsupportedOperationException}, this serves as a
 * more semantically correct exception to throw while something is in process of
 * being created.
 */
public final class PendingImplementationException extends UnsupportedOperationException {

    private static final long serialVersionUID = 1L;

    public static final PendingImplementationException notYetImplemented =
        new PendingImplementationException("Not yet implemented!");

    private PendingImplementationException(final String message) {
        super(message);
    }

}
