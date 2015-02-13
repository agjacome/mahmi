package es.uvigo.ei.sing.mahmi.common.utils.functions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import fj.F;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NumericPredicates {

    public static <N extends Number> F<N, Boolean> lessThan(final N max) {
        return value -> value.doubleValue() < max.doubleValue();
    }

    public static <N extends Number> F<N, Boolean> moreThan(final N min) {
        return value -> value.doubleValue() > min.doubleValue();
    }

    public static <N extends Number> F<N, Boolean> equalTo(final N eq) {
        return value -> value.doubleValue() == eq.doubleValue();
    }

    public static <N extends Number> F<N, Boolean> lessOrEqualTo(final N max) {
        return value -> value.doubleValue() <= max.doubleValue();
    }

    public static <N extends Number> F<N, Boolean> moreOrEqualTo(final N min) {
        return value -> value.doubleValue() >= min.doubleValue();
    }

    public static <N extends Number> F<N, Boolean> between(
        final N min, final N max
    ) {
        return value -> value.doubleValue() <= max.doubleValue()
                     && value.doubleValue() >= min.doubleValue();
    }

}
