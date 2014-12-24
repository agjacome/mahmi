package es.uvigo.ei.sing.mahmi.common.entities.sequences;

import static fj.Monoid.monoid;
import static fj.data.List.iterableList;
import lombok.Getter;
import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.compounds.Nucleobase;
import fj.Monoid;
import fj.data.List;

@Getter
public final class DNASequence extends ChemicalCompoundSequence<Nucleobase> {

    public static Monoid<DNASequence> monoid =
        monoid(d1 -> d2 -> new DNASequence(d1.sequence.append(d2.sequence)), empty());


    private DNASequence(final List<Nucleobase> sequence) {
        super(sequence);
    }

    public static DNASequence empty() {
        return new DNASequence(List.nil());
    }

    public static DNASequence fromIterable(final Iterable<Nucleobase> seq) {
        val sequence = iterableList(seq);
        if (isValid(sequence))
            return new DNASequence(sequence);
        else
            throw new IllegalArgumentException("Invalid DNA sequence given.");
    }

    public static DNASequence fromString(final String str) {
        val seq = List.fromString(str).map(Nucleobase::fromCode);
        return new DNASequence(seq);
    }

    public static boolean isValid(final List<Nucleobase> seq) {
        return seq.forall(n -> !n.equals(Nucleobase.U));
    }

}
