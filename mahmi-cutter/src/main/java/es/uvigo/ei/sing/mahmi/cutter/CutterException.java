package es.uvigo.ei.sing.mahmi.cutter;

public final class CutterException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private CutterException(final String message) {
        super(message);
    }

    private CutterException(final Throwable cause) {
        super(cause);
    }

    private CutterException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public static CutterException withMessage(final String message) {
        return new CutterException(message);
    }

    public static CutterException withCause(final Throwable cause) {
        return new CutterException(cause);
    }

    public static CutterException withMessageAndCause(
        final String message, final Throwable cause
    ) {
        return new CutterException(message, cause);
    }

}
