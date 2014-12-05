package es.uvigo.ei.sing.mahmi.common.entities.fasta;

import static java.util.Collections.emptyMap;

import java.util.Map;

import lombok.EqualsAndHashCode;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;

@EqualsAndHashCode(callSuper = true)
public class ProteinFasta extends Fasta<AminoAcidSequence> {

    private ProteinFasta(final Map<AminoAcidSequence, Long> sequences) {
        super(sequences);
    }

    public static ProteinFasta empty() {
        return new ProteinFasta(emptyMap());
    }

    public static ProteinFasta of(final Map<AminoAcidSequence, Long> sequences) {
        return new ProteinFasta(sequences);
    }

}
