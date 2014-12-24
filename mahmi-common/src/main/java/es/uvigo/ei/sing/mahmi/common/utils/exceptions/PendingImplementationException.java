package es.uvigo.ei.sing.mahmi.common.utils.exceptions;

public final class PendingImplementationException extends UnsupportedOperationException {

    private static final long serialVersionUID = 1L;

    public static PendingImplementationException notYetImplemented =
        new PendingImplementationException("Not yet implemented!");

    private PendingImplementationException(final String message) {
        super(message);
    }

}
