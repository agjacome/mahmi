package es.uvigo.ei.sing.mahmi.common.serializers.fasta;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.DNASequence;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
final class GenomeFastaWriter extends AbstractFastaWriter<DNASequence> implements FastaWriter<DNASequence> {

    @Override
    protected String getStringFromSequence(final DNASequence sequence) {
        return sequence.toString();
    }

}
