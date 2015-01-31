package es.uvigo.ei.sing.mahmi.common.utils.functions;

import java.util.function.Predicate;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NumericPredicates {

    public static <N extends Number> Predicate<N> lessThan(final N max) {
        return value -> value.doubleValue() < max.doubleValue();
    }

    public static <N extends Number> Predicate<N> moreThan(final N min) {
        return value -> value.doubleValue() > min.doubleValue();
    }

    public static <N extends Number> Predicate<N> equalTo(final N eq) {
        return value -> value.doubleValue() == eq.doubleValue();
    }

    public static <N extends Number> Predicate<N> lessOrEqualTo(final N max) {
        return lessThan(max).or(equalTo(max));
    }

    public static <N extends Number> Predicate<N> moreOrEqualTo(final N min) {
        return moreThan(min).or(equalTo(min));
    }

    public static <N extends Number> Predicate<N> between(
        final N min, final N max
    ) {
        return lessOrEqualTo(max).and(moreOrEqualTo(min));
    }

}
