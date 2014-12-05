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
public final class DNASequence implements ChemicalCompoundSequence<Nucleobase> {

    private final List<Nucleobase> sequence;

    private DNASequence(final List<Nucleobase> sequence) {
        this.sequence = sequence;
    }

    public static DNASequence empty() {
        return new DNASequence(List.nil());
    }

    public static DNASequence fromIterable(final Iterable<Nucleobase> seq) throws IllegalArgumentException {
        if (isValidDNA(seq))
            return new DNASequence(iterableList(seq));
        else
            throw new IllegalArgumentException("Invalid DNA sequence given");
    }

    public static DNASequence fromString(final String str) throws IllegalArgumentException {
        return fromIterable(List.fromString(str).map(Nucleobase::fromCode));
    }

    public static Monoid<DNASequence> getMonoid() {
        return monoid(d1 -> d2 -> new DNASequence(d1.sequence.append(d2.sequence)), empty());
    }

    public static boolean isValidDNA(final Iterable<Nucleobase> seq) {
        return iterableList(seq).forall(n -> !n.equals(Nucleobase.U));
    }

    @Override
    public boolean equals(final Object that) {
        return that instanceof DNASequence
            && listEqual(Equal.<Nucleobase>anyEqual()).eq(sequence, ((DNASequence) that).sequence);
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
