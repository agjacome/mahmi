package es.uvigo.ei.sing.mahmi.common.entities.sequences;

import static fj.Equal.listEqual;
import static fj.Hash.listHash;
import static fj.data.List.asString;

import java.util.Iterator;
import java.util.stream.StreamSupport;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.compounds.ChemicalCompound;
import es.uvigo.ei.sing.mahmi.common.utils.SHA1;
import fj.Equal;
import fj.Hash;
import fj.data.List;
import fj.data.Stream;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ChemicalCompoundSequence<A extends ChemicalCompound> implements Iterable<A> {

    protected final List<A> sequence;


    public final Stream<A> toStream() {
        return sequence.toStream();
    }

    public java.util.stream.Stream<A> toJavaStream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public final boolean isEmpty() {
        return sequence.isEmpty();
    }

    public final SHA1 toSHA1() {
        return SHA1.of(toString());
    }


    @Override
    public final Iterator<A> iterator() {
        return sequence.iterator();
    }


    @Override
    public final boolean equals(final Object other) {
        if (!(other instanceof ChemicalCompoundSequence)) return false;

        @SuppressWarnings("unchecked")
        val that = (ChemicalCompoundSequence<A>) other;
        return listEqual(Equal.<A>anyEqual()).eq(sequence, that.sequence);
    }

    @Override
    public final int hashCode() {
        return listHash(Hash.<A>anyHash()).hash(sequence);
    }

    @Override
    public final String toString() {
        return asString(sequence.map(ChemicalCompound::getCode));
    }

}
