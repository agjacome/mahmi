package es.uvigo.ei.sing.mahmi.common.entities.sequences;

import static fj.Monoid.monoid;
import static fj.data.List.iterableList;
import lombok.Getter;
import es.uvigo.ei.sing.mahmi.common.entities.compounds.AminoAcid;
import fj.Monoid;
import fj.data.List;

@Getter
public final class AminoAcidSequence extends ChemicalCompoundSequence<AminoAcid> {

    public static Monoid<AminoAcidSequence> monoid = monoid(
        a1 -> a2 -> new AminoAcidSequence(a1.sequence.append(a2.sequence)),
        empty()
    );


    private AminoAcidSequence(final List<AminoAcid> sequence) {
        super(sequence);
    }

    public static AminoAcidSequence empty() {
        return new AminoAcidSequence(List.nil());
    }

    public static AminoAcidSequence fromIterable(
        final Iterable<AminoAcid> sequence
    ) {
        return new AminoAcidSequence(iterableList(sequence));
    }

    public static AminoAcidSequence fromString(
        final String sequence
    ) {
        return new AminoAcidSequence(
            List.fromString(sequence).map(AminoAcid::fromCode)
        );
    }

}
