package es.uvigo.ei.sing.mahmi.common.serializers.fasta;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
final class ProteinFastaWriter extends AbstractFastaWriter<AminoAcidSequence> implements FastaWriter<AminoAcidSequence> {

    @Override
    protected String getStringFromSequence(final AminoAcidSequence sequence) {
        return sequence.toString();
    }

}
