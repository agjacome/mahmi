package es.uvigo.ei.sing.mahmi.common.entities.fasta;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

import java.util.Map;

import lombok.EqualsAndHashCode;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.DNASequence;

@EqualsAndHashCode(callSuper = true)
public final class GenomeFasta extends Fasta<DNASequence> {

    private GenomeFasta(final Map<DNASequence, Integer> sequences) {
        super(unmodifiableMap(sequences));
    }

    public static GenomeFasta empty() {
        return new GenomeFasta(emptyMap());
    }

    public static GenomeFasta of(final Map<DNASequence, Integer> sequences) {
        return new GenomeFasta(sequences);
    }

    public static GenomeFasta toGenomeFasta(final Fasta<DNASequence> fasta) {
        return (GenomeFasta) fasta;
    }

}
