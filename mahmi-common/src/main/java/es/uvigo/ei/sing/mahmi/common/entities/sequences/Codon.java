package es.uvigo.ei.sing.mahmi.common.entities.sequences;

import static fj.Equal.v3Equal;
import static fj.Hash.v3Hash;
import static fj.data.Stream.asString;
import static fj.data.vector.V.v;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import es.uvigo.ei.sing.mahmi.common.entities.compounds.Nucleobase;
import fj.Equal;
import fj.Hash;
import fj.data.vector.V3;

@Getter
@RequiredArgsConstructor(staticName = "codon")
public final class Codon implements ChemicalCompoundSequence<Nucleobase> {

    private final V3<Nucleobase> triplet;

    public static Codon codon(final Nucleobase first, final Nucleobase second, final Nucleobase third) {
        return new Codon(v(first, second, third));
    }

    @Override
    public Iterable<Nucleobase> getSequence() {
        return triplet;
    }

    @Override
    public boolean equals(final Object that) {
        return that instanceof Codon
            && v3Equal(Equal.<Nucleobase>anyEqual()).eq(triplet, ((Codon) that).triplet);
    }

    @Override
    public int hashCode() {
        return v3Hash(Hash.<Nucleobase>anyHash()).hash(triplet);
    }

    @Override
    public String toString() {
        return asString(triplet.map(Nucleobase::getCode).toStream());
    }

}
