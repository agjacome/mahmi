package es.uvigo.ei.sing.mahmi.common.utils.extensions;

import java.util.function.Supplier;

import fj.F0;
import fj.data.Option;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static fj.P.lazy;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OptionExtensionMethods {

    public static <A> boolean is(final Option<A> a) {
        return a.isSome();
    }

    public static <A> boolean isNot(final Option<A> a) {
        return a.isNone();
    }

    public static <A> Option<A> some(final A a) {
        return Option.some(a);
    }

    public static <A> Option<A> cond(final boolean p, final A a) {
        return Option.iif(p, a);
    }

    public static <A> Option<A> cond(final boolean p, final F0<A> a) {
        return Option.iif(p, lazy(a));
    }

    public static <A> A or(final Option<A> opt, final A a) {
        return opt.orSome(a);
    }

    public static <A> A or(final Option<A> opt, final F0<A> a) {
        return opt.orSome(lazy(a));
    }

     public static <A, B extends Throwable> A orThrow(
         final Option<A> opt, final Supplier<B> err
     ) throws B {
         if (opt.isSome()) return opt.some(); else throw err.get();
     }

    public static <A, B extends Throwable> A orThrow(
        final Option<A> opt, final B err
    ) throws B {
        if (opt.isSome()) return opt.some();
        else throw err;
    }

}
