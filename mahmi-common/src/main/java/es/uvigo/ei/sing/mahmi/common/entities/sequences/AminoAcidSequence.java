package es.uvigo.ei.sing.mahmi.common.entities.sequences;

import static fj.Equal.listEqual;
import static fj.Hash.listHash;
import static fj.Monoid.monoid;
import static fj.data.List.asString;
import static fj.data.List.iterableList;
import lombok.Getter;
import es.uvigo.ei.sing.mahmi.common.entities.compounds.AminoAcid;
import fj.Equal;
import fj.Hash;
import fj.Monoid;
import fj.data.List;

@Getter
public final class AminoAcidSequence implements ChemicalCompoundSequence<AminoAcid> {

    private final List<AminoAcid> sequence;

    private AminoAcidSequence(final List<AminoAcid> sequence) {
        this.sequence = sequence;
    }

    public static AminoAcidSequence empty() {
        return new AminoAcidSequence(List.nil());
    }

    public static AminoAcidSequence fromIterable(final Iterable<AminoAcid> seq) {
        return new AminoAcidSequence(iterableList(seq));
    }

    public static AminoAcidSequence fromString(final String str) {
        return new AminoAcidSequence(List.fromString(str).map(AminoAcid::fromCode));
    }

    public static Monoid<AminoAcidSequence> getMonoid() {
        return monoid(a1 -> a2 -> fromIterable(a1.sequence.append(a2.sequence)), empty());
    }

    @Override
    public boolean equals(final Object that) {
        return that instanceof AminoAcidSequence
            && listEqual(Equal.<AminoAcid>anyEqual()).eq(sequence, ((AminoAcidSequence) that).sequence);
    }

    @Override
    public int hashCode() {
        return listHash(Hash.<AminoAcid>anyHash()).hash(sequence);
    }

    @Override
    public String toString() {
        return asString(sequence.map(AminoAcid::getCode));
    }

}
