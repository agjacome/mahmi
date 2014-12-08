package es.uvigo.ei.sing.mahmi.common.entities.sequences;

import static fj.data.Stream.iterableStream;

import java.util.Iterator;
import java.util.stream.StreamSupport;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.ChemicalCompound;
import es.uvigo.ei.sing.mahmi.common.utils.SHA1;
import fj.data.Stream;

public interface ChemicalCompoundSequence<A extends ChemicalCompound> extends Iterable<A> {

    public Iterable<A> getSequence();

    @Override
    public default Iterator<A> iterator() {
        return getSequence().iterator();
    }

    public default Stream<A> toStream() {
        return iterableStream(this);
    }

    public default java.util.stream.Stream<A> toJavaStream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public default boolean isEmpty() {
        return toStream().isEmpty();
    }

    public default SHA1 toSHA1() {
        return SHA1.of(toString());
    }

}
