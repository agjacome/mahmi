package es.uvigo.ei.sing.mahmi.common.entities.sequences;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.Compound;
import es.uvigo.ei.sing.mahmi.common.utils.Tuple;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableMap;

import static es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableUtils.mapify;
import static es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableUtils.streamify;

public final class Fasta<A extends CompoundSequence<? extends Compound>> implements Iterable<Tuple<String, A>> {

    private final Map<String, A> sequences;

    private Fasta(final Map<String, A> sequences) {
        this.sequences = unmodifiableMap(sequences);
    }

    private Fasta(final Iterable<Tuple<String, A>> sequences) {
        this(mapify(sequences, Tuple::getLeft, Tuple::getRight));
    }

    public static <A extends CompoundSequence<? extends Compound>> Fasta<A> of(
        final Map<String, A> sequences
    ) {
        return new Fasta<>(sequences);
    }

    public static <A extends CompoundSequence<? extends Compound>> Fasta<A> of(
        final Iterable<Tuple<String, A>> sequences
    ) {
        return new Fasta<>(sequences);
    }

    public static <A extends CompoundSequence<? extends Compound>> Fasta<A> empty() {
        return new Fasta<>(emptyList());
    }

    public Map<String, A> getSequences() {
        return sequences;
    }

    public Optional<A> getSequence(final String key) {
        return Optional.ofNullable(sequences.get(key));
    }

    @Override
    public Iterator<Tuple<String, A>> iterator() {
        return streamify(sequences.entrySet()).map(Tuple::fromEntry).iterator();
    }

    @Override
    public int hashCode() {
        return sequences.hashCode();
    }

    @Override
    public boolean equals(final Object that) {
        if (this == that) return true;
        if (that == null) return false;

        return getClass() == that.getClass()
            && this.sequences.equals(((Fasta<?>) that).sequences);
    }

    @Override
    public String toString() {
        return String.format(
            "Fasta of %d entries (hashCode: %d)", sequences.size(), hashCode()
        );
    }

}
