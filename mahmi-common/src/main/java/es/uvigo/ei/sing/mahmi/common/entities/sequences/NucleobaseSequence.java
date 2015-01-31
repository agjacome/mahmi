package es.uvigo.ei.sing.mahmi.common.entities.sequences;

import static fj.Monoid.monoid;
import lombok.Value;
import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.compounds.Nucleobase;
import fj.Monoid;
import fj.data.List;
import fj.data.Option;

@Value(staticConstructor = "fromList")
public final class NucleobaseSequence implements CompoundSequence<Nucleobase> {

    public static final Monoid<NucleobaseSequence> monoid =
        monoid(d1 -> d2 -> fromList(d1.residues.append(d2.residues)), empty());

    private final List<Nucleobase> residues;

    public static NucleobaseSequence empty() {
        return fromList(List.nil());
    }

    public static Option<NucleobaseSequence> fromString(
        final String sequence
    ) {
        val seq = List.fromString(sequence).map(Nucleobase::fromCode);
        return Option.sequence(seq).map(NucleobaseSequence::fromList);
    }

}
