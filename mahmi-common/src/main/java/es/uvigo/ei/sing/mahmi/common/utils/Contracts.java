package es.uvigo.ei.sing.mahmi.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import fj.F;

/**
 * A helper non-constructible class providing runtime checks of pre- and
 * post-conditions, similar to assertions but without the ability to turn them
 * off. All methods are static and it is recommended to statically import them
 * wherever they are needed instead of using them directly through the Contracts
 * class. Example:
 *
 * <pre>
 * // do not do this:
 * import es.uvigo.ei.sing.mahmi.common.utils.Contracts;
 * ...
 * Contracts.require(x.equals(y), "%s given where %s was expected", x, y);
 *
 * // but instead prefer this:
 * import static es.uvigo.ei.sing.mahmi.common.utils.Contracts.require;
 * ...
 * require(x.equals(y), "%s given where %s was expected", x, y);
 * </pre>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Contracts {

    /**
     * Checks if a given precondition holds, throwing a {@link RuntimeException}
     * if it does not.
     *
     * @param condition
     *            Precondition that should hold before an operation takes place.
     * @param constructor
     *            Function that takes a String a returns a RuntimeException,
     *            probably a RuntimeException constructor. Will be called when
     *            the precondition fails.
     * @param message
     *            Error message to construct the RuntimeException with.
     * @param args
     *            Optional arguments that should be included into the message
     *            (in the same way as {@link String#format(String, Object...)})
     *
     * @throws RuntimeException
     *             If the given condition does not hold.
     */
    public static void require(
        final boolean condition,
        final F<String, ? extends RuntimeException> constructor,
        final String     message,
        final Object ... args
    ) {
        if (!condition) throw constructor.f(String.format(message, args));
    }

    /**
     * Checks if a given precondition holds, throwing a
     * {@link IllegalArgumentException} if it does not.
     *
     * @param condition
     *            Precondition that should hold before an operation takes place.
     * @param message
     *            Error message to construct the IllegalArgumentException with.
     * @param args
     *            Optional arguments that should be included into the message
     *            (in the same way as {@link String#format(String, Object...)})
     *
     * @throws IllegalArgumentException
     *             If the given condition does not hold.
     */
    public static void require(
        final boolean condition, final String message, final Object ... args
    ) {
        require(condition, IllegalArgumentException::new, message, args);
    }

    /**
     * Checks if a given precondition holds, throwing a
     * {@link IllegalArgumentException} if it does not.
     *
     * @param condition
     *            Precondition that should hold before an operation takes place.
     *
     * @throws IllegalArgumentException
     *             If the given condition does not hold.
     */
    public static void require(final boolean condition) {
        require(condition, "Required condition does not hold");
    }

    /**
     * Checks that a given value is not null, throwing a
     * {@link RuntimeException} if it does not.
     *
     * @param value
     *            Value that should be checked to be non null.
     * @param constructor
     *            Function that takes a String a returns a RuntimeException,
     *            probably a RuntimeException constructor. Will be called when
     *            the value is null.
     * @param message
     *            Error message to construct the RuntimeException with.
     * @param args
     *            Optional arguments that should be included into the message
     *            (in the same way as {@link String#format(String, Object...)})
     *
     * @throws RuntimeException
     *             If the value is found to be null.
     */
    public static <A> void requireNonNull(
        final A value,
        final F<String, ? extends RuntimeException> constructor,
        final String     message,
        final Object ... args
    ) {
        require(value != null, constructor, message, args);
    }

    /**
     * @param value
     * @param message
     * @param args
     */
    public static <A> void requireNonNull(
        final A value, final String message, final Object ... args
    ) {
        requireNonNull(value, NullPointerException::new, message, args);
    }

    /**
     * @param value
     */
    public static <A> void requireNonNull(final A value) {
        requireNonNull(value, "Non-null requirement does not hold");
    }

    /**
     * @param condition
     * @param message
     * @param args
     */
    public static void ensure(
        final boolean condition, final String message, final Object ... args
    ) {
        if (!condition) throw new AssertionError(String.format(message, args));
    }

    /**
     * @param message
     * @param args
     */
    public static void fail(final String message, final Object ... args) {
        ensure(false, message, args);
    }

}
