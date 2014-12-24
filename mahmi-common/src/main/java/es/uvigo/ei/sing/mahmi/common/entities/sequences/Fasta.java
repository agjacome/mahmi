package es.uvigo.ei.sing.mahmi.common.entities.sequences;

import static java.util.Collections.emptyMap;

import java.util.Map;

import lombok.Value;
import es.uvigo.ei.sing.mahmi.common.entities.compounds.ChemicalCompound;

@Value(staticConstructor = "of")
public final class Fasta<A extends ChemicalCompoundSequence<? extends ChemicalCompound>> {

    private final Map<A, Long> sequences;

    public static <A extends ChemicalCompoundSequence<? extends ChemicalCompound>> Fasta<A> empty() {
        return new Fasta<A>(emptyMap());
    }

}
