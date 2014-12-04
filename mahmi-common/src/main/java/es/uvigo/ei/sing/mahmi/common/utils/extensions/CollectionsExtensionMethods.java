package es.uvigo.ei.sing.mahmi.common.utils.extensions;

import static fj.P.p;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import fj.F;
import fj.P2;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CollectionsExtensionMethods {

    public static <A> Map<A, Long> frequencies(final Stream<A> as) {
        return as.collect(toMap(identity(), a -> 1L, Long::sum));
    }

    public static <A> Map<A, Long> frequencies(final Collection<A> as) {
        return frequencies(as.parallelStream());
    }

    public static <A> Map<A, A> setToIdentityMap(final Set<A> as) {
        return as.parallelStream().collect(toMap(identity(), identity()));
    }

    public static <A, B, C> Map<C, B> mapKeys(final Map<A, B> map, final F<A, C> mapper) {
        return map.entrySet().parallelStream().map(
            entry -> p(mapper.f(entry.getKey()), entry.getValue())
        ).collect(toMap(P2::_1, P2::_2));
    }

    public static <A, B, C> Map<A, C> mapValues(final Map<A, B> map, final F<B, C> mapper) {
        return map.entrySet().parallelStream().map(
            entry -> p(entry.getKey(), mapper.f(entry.getValue()))
        ).collect(toMap(P2::_1, P2::_2));
    }

    public static <A, B, C, D> Map<C, D> mapMap(
        final Map<A, B> map, final F<A, C> keyMapper, final F<B, D> valueMapper
    ) {
        return map.entrySet().parallelStream().map(
            entry -> p(keyMapper.f(entry.getKey()), valueMapper.f(entry.getValue()))
        ).collect(toMap(P2::_1, P2::_2));
    }

}
