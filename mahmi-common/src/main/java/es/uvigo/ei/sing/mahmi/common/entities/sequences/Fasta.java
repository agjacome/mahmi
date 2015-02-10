package es.uvigo.ei.sing.mahmi.common.entities.sequences;

import static java.util.Collections.emptyIterator;

import java.util.Iterator;

import lombok.Value;
import es.uvigo.ei.sing.mahmi.common.entities.compounds.Compound;
import fj.data.Stream;

@Value(staticConstructor = "of")
public final class Fasta<A extends CompoundSequence<? extends Compound>> implements Iterable<A> {

    private final Iterator<A> sequences;

    public static <A extends CompoundSequence<? extends Compound>> Fasta<A> empty() {
        return of(emptyIterator());
    }

    @Override
    public Iterator<A> iterator() {
        return sequences;
    }

    public Stream<A> toStream() {
        return Stream.iterableStream(this);
    }

}
