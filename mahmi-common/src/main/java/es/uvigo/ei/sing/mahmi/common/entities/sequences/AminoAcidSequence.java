package es.uvigo.ei.sing.mahmi.common.entities.sequences;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.val;

import fj.Monoid;
import fj.data.Option;
import fj.data.Seq;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.AminoAcid;

import static fj.Monoid.monoid;
import static fj.data.Option.none;
import static fj.data.Option.some;

@Value(staticConstructor = "fromSeq")
@EqualsAndHashCode(callSuper = false)
public final class AminoAcidSequence extends CompoundSequence<AminoAcid> {

    public static final Monoid<AminoAcidSequence> monoid =
        monoid(a1 -> a2 -> fromSeq(a1.residues.append(a2.residues)), empty());

    private final Seq<AminoAcid> residues;

    public static AminoAcidSequence empty() {
        return fromSeq(Seq.empty());
    }

    public static Option<AminoAcidSequence> fromString(
        final String sequence
    ) {
        Seq<AminoAcid> seq = Seq.empty();

        for (val code : sequence.toCharArray()) {
            val aa = AminoAcid.fromCode(code);
            if (aa.isNone()) return none();
            else seq = seq.snoc(aa.some());
        }

        return some(fromSeq(seq));
    }

}
