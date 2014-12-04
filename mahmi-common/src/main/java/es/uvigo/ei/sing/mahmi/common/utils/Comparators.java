package es.uvigo.ei.sing.mahmi.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Comparators {

    public static boolean between(final int min, final int value, final int max) {
        return min <= value && value <= max;
    }

}
