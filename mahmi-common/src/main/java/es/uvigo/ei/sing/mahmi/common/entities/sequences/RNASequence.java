package es.uvigo.ei.sing.mahmi.common.entities.sequences;

import static fj.Equal.listEqual;
import static fj.Hash.listHash;
import static fj.Monoid.monoid;
import static fj.data.List.asString;
import static fj.data.List.iterableList;
import lombok.Getter;
import es.uvigo.ei.sing.mahmi.common.entities.compounds.Nucleobase;
import fj.Equal;
import fj.Hash;
import fj.Monoid;
import fj.data.List;

@Getter
public final class RNASequence implements ChemicalCompoundSequence<Nucleobase> {

    private final List<Nucleobase> sequence;

    private RNASequence(final List<Nucleobase> sequence) {
        this.sequence = sequence;
    }

    public static RNASequence empty() {
        return new RNASequence(List.nil());
    }

    public static RNASequence fromIterable(final Iterable<Nucleobase> seq) throws IllegalArgumentException {
        if (isValidRNA(seq))
            return new RNASequence(iterableList(seq));
        else
            throw new IllegalArgumentException("Invalid RNA sequence given");
    }

    public static RNASequence fromString(final String str) throws IllegalArgumentException {
        return fromIterable(List.fromString(str).map(Nucleobase::fromCode));
    }

    public static Monoid<RNASequence> getMonoid() {
        return monoid(d1 -> d2 -> new RNASequence(d1.sequence.append(d2.sequence)), empty());
    }

    public static boolean isValidRNA(final Iterable<Nucleobase> seq) {
        return iterableList(seq).forall(n -> !n.equals(Nucleobase.T));
    }

    @Override
    public boolean equals(final Object that) {
        return that instanceof RNASequence
            && listEqual(Equal.<Nucleobase>anyEqual()).eq(sequence, ((RNASequence) that).sequence);
    }

    @Override
    public int hashCode() {
        return listHash(Hash.<Nucleobase>anyHash()).hash(sequence);
    }

    @Override
    public String toString() {
        return asString(sequence.map(Nucleobase::getCode));
    }

}
