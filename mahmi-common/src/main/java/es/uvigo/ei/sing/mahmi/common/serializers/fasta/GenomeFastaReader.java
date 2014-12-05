package es.uvigo.ei.sing.mahmi.common.serializers.fasta;

import java.util.Map;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import es.uvigo.ei.sing.mahmi.common.entities.fasta.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.fasta.GenomeFasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.DNASequence;
import fj.Monoid;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
final class GenomeFastaReader extends AbstractFastaReader<DNASequence> implements FastaReader<DNASequence> {

    @Override
    protected Monoid<DNASequence> getSequenceMonoid() {
        return DNASequence.getMonoid();
    }

    @Override
    protected Fasta<DNASequence> getFastaFromMap(final Map<DNASequence, Integer> map) {
        return GenomeFasta.of(map);
    }

    @Override
    protected DNASequence getSequenceFromString(final String str) {
        return DNASequence.fromString(str);
    }

}
