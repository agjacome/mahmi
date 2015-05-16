package es.uvigo.ei.sing.mahmi.common.utils.functions;

import java.util.function.Predicate;

import es.uvigo.ei.sing.mahmi.common.utils.annotations.DisallowConstruction;

public final class NumericPredicates {

    // FIXME: do not use Number directly, create different implementations for
    // Byte, Int, Long, Float and Double.

    @DisallowConstruction
    private NumericPredicates() { }

    public static <N extends Number> Predicate<N> lessThan(final N max) {
        return value -> value.doubleValue() < max.doubleValue();
    }

    public static <N extends Number> Predicate<N> moreThan(final N min) {
        return value -> value.doubleValue() > min.doubleValue();
    }

    public static <N extends Number> Predicate<N> equalTo(final N eq) {
        return value -> value.doubleValue() == eq.doubleValue();
    }

    public static <N extends Number> Predicate<N> equalOrLessThan(final N max) {
        return value -> value.doubleValue() <= max.doubleValue();
    }

    public static <N extends Number> Predicate<N> equalOrMoreThan(final N min) {
        return value -> value.doubleValue() >= min.doubleValue();
    }

    public static <N extends Number> Predicate<N> between(
        final N min, final N max
    ) {
        return value -> value.doubleValue() <= max.doubleValue()
                     && value.doubleValue() >= min.doubleValue();
    }

}
