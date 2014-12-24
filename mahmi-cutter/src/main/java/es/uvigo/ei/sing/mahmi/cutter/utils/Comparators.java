package es.uvigo.ei.sing.mahmi.cutter.utils;

import java.util.function.Predicate;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Comparators {

    public static Predicate<Integer> lessThan(final int max) {
        return value -> value < max;
    }

    public static Predicate<Integer> moreThan(final int min) {
        return value -> value > min;
    }

    public static Predicate<Integer> equalTo(final int value) {
        return v -> v == value;
    }

    public static Predicate<Integer> lessOrEqualThan(final int max) {
        return lessThan(max).or(equalTo(max));
    }

    public static Predicate<Integer> moreOrEqualThan(final int min) {
        return moreThan(min).or(equalTo(min));
    }

    public static Predicate<Integer> between(final int min, final int max) {
        return lessOrEqualThan(max).and(moreOrEqualThan(min));
    }

}
