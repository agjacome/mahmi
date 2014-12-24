package es.uvigo.ei.sing.mahmi.loader;

import java.nio.file.Path;
import java.util.stream.Stream;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.DNASequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import fj.P2;

public interface ProjectLoader {

    public Stream<P2<Fasta<DNASequence>, Fasta<AminoAcidSequence>>> loadProject(
        final Path projectPath
    ) throws LoaderException;

}
