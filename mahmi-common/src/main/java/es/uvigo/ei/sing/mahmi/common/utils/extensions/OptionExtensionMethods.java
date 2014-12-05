package es.uvigo.ei.sing.mahmi.common.utils.extensions;

import java.util.function.Supplier;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import fj.data.Option;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OptionExtensionMethods {

    public static <A> String optToString(final Option<A> opt, final String undef) {
        return opt.map(Object::toString).orSome(undef);
    }

    public static <A> String optToString(final Option<A> opt) {
        return optToString(opt, "undefined");
    }

    public static <A, B extends Throwable> A getOrThrow(
        final Option<A> opt, final Supplier<? extends B> thrower
    ) throws B {
        if (opt.isSome())
            return opt.some();
        else
            throw thrower.get();
    }

}
