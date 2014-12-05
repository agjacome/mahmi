package es.uvigo.ei.sing.mahmi.common.entities.fasta;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

import java.util.Map;

import lombok.EqualsAndHashCode;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;

@EqualsAndHashCode(callSuper = true)
public class ProteinFasta extends Fasta<AminoAcidSequence> {

    private ProteinFasta(final Map<AminoAcidSequence, Integer> sequences) {
        super(unmodifiableMap(sequences));
    }

    public static ProteinFasta empty() {
        return new ProteinFasta(emptyMap());
    }

    public static ProteinFasta of(final Map<AminoAcidSequence, Integer> sequences) {
        return new ProteinFasta(sequences);
    }

}
