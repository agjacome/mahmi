package es.uvigo.ei.sing.mahmi.common.utils;

import java.util.function.Function;

import es.uvigo.ei.sing.mahmi.common.utils.annotations.DisallowConstruction;

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
public final class Contracts {

    @DisallowConstruction
    private Contracts() { }

    public static void require(
        final boolean condition,
        final Function<String, ? extends RuntimeException> exception,
        final String     message,
        final Object ... args
    ) {
        if (!condition) throw exception.apply(String.format(message, args));
    }

    public static void require(
        final boolean condition, final String message, final Object ... args
    ) {
        require(condition, IllegalArgumentException::new, message, args);
    }

    public static void require(final boolean condition) {
        require(condition, "Required condition does not hold");
    }

    public static <A> A requireNonNull(
        final A value,
        final Function<String, ? extends RuntimeException> exception,
        final String     message,
        final Object ... args
    ) {
        require(value != null, exception, message, args);
        return value;
    }

    public static <A> A requireNonNull(
        final A value, final String message, final Object ... args
    ) {
        return requireNonNull(value, NullPointerException::new, message, args);
    }

    public static <A> A requireNonNull(final A value) {
        return requireNonNull(value, "Non-null requirement does not hold");
    }

    public static void ensure(
        final boolean condition, final String message, final Object ... args
    ) {
        if (!condition) throw new AssertionError(String.format(message, args));
    }

    public static void fail(final String message, final Object ... args) {
        ensure(false, message, args);
    }

}
