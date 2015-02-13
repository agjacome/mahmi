package es.uvigo.ei.sing.mahmi.common.entities.sequences;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

import fj.Monoid;
import fj.data.Option;
import fj.data.Seq;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.Nucleobase;

import static fj.Monoid.monoid;
import static fj.data.Option.none;
import static fj.data.Option.some;

@Getter
@AllArgsConstructor(staticName = "fromSeq")
public final class NucleobaseSequence extends CompoundSequence<Nucleobase> {

    public static final Monoid<NucleobaseSequence> monoid =
        monoid(n1 -> n2 -> fromSeq(n1.residues.append(n2.residues)), empty());

    private final Seq<Nucleobase> residues;

    public static NucleobaseSequence empty() {
        return fromSeq(Seq.empty());
    }

    public static Option<NucleobaseSequence> fromString(
        final String sequence
    ) {
        Seq<Nucleobase> seq = Seq.empty();

        for (val code : sequence.toCharArray()) {
            val aa = Nucleobase.fromCode(code);
            if (aa.isNone()) return none();
            else seq = seq.snoc(aa.some());
        }

        return some(fromSeq(seq));
    }

}
