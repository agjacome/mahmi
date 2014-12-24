package es.uvigo.ei.sing.mahmi.common.utils;

import static java.util.Objects.nonNull;

import java.util.function.Supplier;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Validators {

    public static <A, B extends Throwable> A requireNonNull(
        final A value, final Supplier<B> thrower
    ) throws B {
        if (nonNull(value)) return value;
        else throw thrower.get();
    }

    public static <A, B extends Throwable> A requireNonNull(
        final A value, final B exception
    ) throws B {
        return requireNonNull(value, () -> exception);
    }

}
