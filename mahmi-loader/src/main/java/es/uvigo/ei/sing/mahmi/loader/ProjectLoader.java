package es.uvigo.ei.sing.mahmi.loader;

import java.nio.file.Path;

import fj.P2;
import fj.data.Stream;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleobaseSequence;

public interface ProjectLoader {

    public Stream<P2<Fasta<NucleobaseSequence>, Fasta<AminoAcidSequence>>> loadProject(
        final Path projectPath
    ) throws LoaderException;

}
