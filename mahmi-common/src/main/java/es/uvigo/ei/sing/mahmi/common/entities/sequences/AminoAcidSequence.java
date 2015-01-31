package es.uvigo.ei.sing.mahmi.common.entities.sequences;

import static fj.Monoid.monoid;
import lombok.Value;
import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.compounds.AminoAcid;
import fj.Monoid;
import fj.data.List;
import fj.data.Option;

@Value(staticConstructor = "fromList")
public final class AminoAcidSequence implements CompoundSequence<AminoAcid> {

    public static final Monoid<AminoAcidSequence> monoid =
        monoid(a1 -> a2 -> fromList(a1.residues.append(a2.residues)), empty());

    private final List<AminoAcid> residues;

    public static AminoAcidSequence empty() {
        return fromList(List.nil());
    }

    public static Option<AminoAcidSequence> fromString(
        final String sequence
    ) {
        val seq = List.fromString(sequence).map(AminoAcid::fromCode);
        return Option.sequence(seq).map(AminoAcidSequence::fromList);
    }

    /**
     * <p>
     * Please, use <code>CompoundSequence.show</code> or create the String
     * directly through
     * <code>List.asString(aas.getResidues().map(Compound::getCode))</code>.
     * </p>
     *
     * <p>
     * <strong>Never ever again rely on toString.</strong>
     * </p>
     */
    @Override
    @Deprecated
    public String toString() {
        return show.showS(this);
    }

}
