package es.uvigo.ei.sing.mahmi.common.utils.extensions;

import java.util.Collection;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;

import fj.Equal;
import fj.Hash;
import fj.Ord;
import fj.data.HashMap;
import fj.data.List;
import fj.data.Set;
import fj.data.Stream;

import static fj.P.p;
import static fj.data.IterableW.wrap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IterableExtensionMethods {

    public static <A> HashMap<A, Long> frequencies(
        final Iterable<A> as, final Equal<A> equal, final Hash<A> hash
    ) {
        val map = HashMap.<A, Long>from(List.nil(), equal, hash);
        as.forEach(a -> map.set(a, map.get(a).orSome(0L) + 1L));
        return map;
    }

    public static <A> Collection<A> toCollection(final Iterable<A> as) {
        return wrap(as).toStandardList();
    }

    public static <A> List<A> toList(final Iterable<A> as) {
        return List.iterableList(as);
    }

    public static <A> Stream<A> toStream(final Iterable<A> as) {
        return Stream.iterableStream(as);
    }

    public static <A> Set<A> toSet(final Iterable<A> as, final Ord<A> ord) {
        return Set.iterableSet(ord, as);
    }

    public static <A> HashMap<A, A> toIdentityMap(
        final Iterable<A> as, final Equal<A> equal, final Hash<A> hash
    ) {
        return HashMap.from(wrap(as).map(a -> p(a, a)), equal, hash);
    }

}
