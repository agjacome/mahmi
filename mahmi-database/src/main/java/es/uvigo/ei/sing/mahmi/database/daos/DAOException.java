package es.uvigo.ei.sing.mahmi.database.daos;

public final class DAOException extends Exception {

    private static final long serialVersionUID = 1L;

    private DAOException(final String message) {
        super(message);
    }

    private DAOException(final Throwable cause) {
        super(cause);
    }

    private DAOException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public static DAOException withMessage(final String message) {
        return new DAOException(message);
    }

    public static DAOException withCause(final Throwable cause) {
        return new DAOException(cause);
    }

    public static DAOException withMessageAndCause(final String message, final Throwable cause) {
        return new DAOException(message, cause);
    }

}
