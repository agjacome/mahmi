package es.uvigo.ei.sing.mahmi.loader.loaders;

public final class LoaderException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private LoaderException(final String message) {
        super(message);
    }

    private LoaderException(final Throwable cause) {
        super(cause);
    }

    private LoaderException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public static LoaderException withMessage(final String message) {
        return new LoaderException(message);
    }

    public static LoaderException withCause(final Throwable cause) {
        return new LoaderException(cause);
    }

    public static LoaderException withMessageAndCause(final String message, final Throwable cause) {
        return new LoaderException(message, cause);
    }

}
