package es.uvigo.ei.sing.mahmi.common.utils.extensions;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import es.uvigo.ei.sing.mahmi.common.utils.annotations.DisallowConstruction;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;
import static java.util.stream.StreamSupport.stream;

public final class IterableUtils {

    @DisallowConstruction
    private IterableUtils() { }

    public static <A> Stream<A> streamify(final Iterable<A> it) {
        return stream(it.spliterator(), false);
    }

    public static <A> List<A> listify(final Iterable<A> it) {
        return streamify(it).collect(toList());
    }

    public static <A> Set<A> setify(final Iterable<A> it) {
        return streamify(it).collect(toSet());
    }

    public static <A, B> Map<A, B> mapify(
        final Iterable<A> it, final Function<A, B> valueMapper
    ) {
        return streamify(it).collect(toMap(identity(), valueMapper));
    }

    public static <A, B, C> Map<B, C> mapify(
        final Iterable<A>    it,
        final Function<A, B> keyMapper,
        final Function<A, C> valueMapper
    ) {
        return streamify(it).collect(toMap(keyMapper, valueMapper));
    }

    public static <A> Map<A, Long> countFrequencies(final Iterable<A> it) {
        return streamify(it).collect(groupingBy(identity(), counting()));
    }

}
