package es.uvigo.ei.sing.mahmi.common.utils.exceptions;

public final class PendingImplementationException extends UnsupportedOperationException {

    private static final long serialVersionUID = 1L;

    public static final PendingImplementationException NOT_YET_IMPLEMENTED = notYetImplemented();

    private PendingImplementationException(final String message) {
        super(message);
    }

    public static PendingImplementationException notYetImplemented() {
        return new PendingImplementationException("Not yet implemented!");
    }

}
