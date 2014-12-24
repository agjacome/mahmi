package es.uvigo.ei.sing.mahmi.common.utils.extensions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import fj.data.Option;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OptionExtensionMethods {

    public static <A, B extends Throwable> A getOrThrow(
        final Option<A> opt, final B err
    ) throws B {
        if (opt.isSome()) return opt.some(); else throw err;
    }

    public static <A> String optToString(final Option<A> opt) {
        return opt.map(Object::toString).orSome("undefined");
    }

}
