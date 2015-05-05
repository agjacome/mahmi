package es.uvigo.ei.sing.mahmi.common.utils.extensions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import fj.Hash;
import fj.Ord;
import fj.Ordering;

import static fj.Ord.ord;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HashExtensionMethods {

    public static <A> Ord<A> toOrd(final Hash<A> hash) {
        return ord(a1 -> a2 -> {
            final int diff = hash.hash(a1) - hash.hash(a2);
            return diff < 0 ? Ordering.LT : diff == 0 ? Ordering.EQ : Ordering.GT;
        });
    }

}
