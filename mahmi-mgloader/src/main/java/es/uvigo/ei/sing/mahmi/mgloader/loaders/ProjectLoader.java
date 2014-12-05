package es.uvigo.ei.sing.mahmi.mgloader.loaders;

import java.nio.file.Path;
import java.util.stream.Stream;

import es.uvigo.ei.sing.mahmi.common.entities.fasta.GenomeFasta;
import es.uvigo.ei.sing.mahmi.common.entities.fasta.ProteinFasta;
import fj.P2;

public interface ProjectLoader {

    public Stream<P2<GenomeFasta, ProteinFasta>> loadProject(final Path projectPath) throws LoaderException;

}
