package es.uvigo.ei.sing.mahmi.common.utils.extensions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import fj.P1;
import fj.data.Option;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OptionExtensionMethods {

    public static <A, B extends Throwable> A orThrow(
        final Option<A> option, final P1<B> exception
    ) throws B {
        if (option.isSome())
            return option.some();
        else
            throw exception._1();
    }

    public static <A, B extends Throwable> A orThrow(
        final Option<A> option, final B exception
    ) throws B {
        if (option.isSome())
            return option.some();
        else
            throw exception;
    }

}
