package es.uvigo.ei.sing.mahmi.common.serializers.fasta;

import java.util.Map;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import es.uvigo.ei.sing.mahmi.common.entities.fasta.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.fasta.ProteinFasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import fj.Monoid;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
final class ProteinFastaReader extends AbstractFastaReader<AminoAcidSequence> implements FastaReader<AminoAcidSequence> {

    @Override
    protected Monoid<AminoAcidSequence> getSequenceMonoid() {
        return AminoAcidSequence.getMonoid();
    }

    @Override
    protected Fasta<AminoAcidSequence> getFastaFromMap(final Map<AminoAcidSequence, Long> map) {
        return ProteinFasta.of(map);
    }

    @Override
    protected AminoAcidSequence getSequenceFromString(final String str) {
        return AminoAcidSequence.fromString(str);
    }

}
