package es.uvigo.ei.sing.mahmi.common.utils.extensions;

import static fj.Function.identity;
import static fj.P.p;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import fj.F;
import fj.P2;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CollectionExtensionMethods {

    public static <E> Collection<E> toCollection(final Iterable<E> iter) {
        final Collection<E> collection = new ArrayList<E>();
        for (final E item : iter) collection.add(item);

        return collection;
    }

    public static <A> Map<A, Long> frequencies(final Collection<A> as) {
        return as.stream().collect(toMap(a -> a, a -> 1L, Long::sum));
    }

    public static <A> Collector<A, ?, Map<A, Long>> frequencies() {
        return Collectors.<A, A, Long>toMap(a -> a, a -> 1L, Long::sum);
    }

    public static <A> Map<A, A> setToMap(final Set<A> as) {
        return as.stream().collect(toMap(a -> a, a -> a));
    }

    public static <A extends Enum<A>, B, C> Map<B, C> enumToMap(
        final Class<A> enumClass,
        final F<A, B>  keyMapper,
        final F<A, C>  valueMapper
    ) {
        val all = setToMap(EnumSet.allOf(enumClass));
        return transform(all, keyMapper, valueMapper);
    }

    public static <A, B, C, D> Map<C, D> transform(
        final Map<A, B> map, final F<A, C> keyMapper, final F<B, D> valueMapper
    ) {
        return map.entrySet().stream().collect(toMap(
            entry -> keyMapper.f(entry.getKey()),
            entry -> valueMapper.f(entry.getValue())
        ));
    }

    public static <A, B, C> Map<C, B> transformKeys(
        final Map<A, B> map, final F<A, C> mapper
    ) {
        return transform(map, mapper, identity());
    }

    public static <A, B, C> Map<A, C> transformValues(
        final Map<A, B> map, final F<B, C> mapper
    ) {
        return transform(map, identity(), mapper);
    }

    public static <A, B> Collection<P2<A, B>> combine(
        final Iterable<A> as, final Iterable<B> bs
    ) {
        val zipped = new LinkedList<P2<A, B>>();

        val asIterator = as.iterator();
        val bsIterator = bs.iterator();
        while (asIterator.hasNext() && bsIterator.hasNext()) {
            val a = asIterator.next();
            val b = bsIterator.next();
            zipped.add(p(a, b));
        }

        return zipped;
    }

}
