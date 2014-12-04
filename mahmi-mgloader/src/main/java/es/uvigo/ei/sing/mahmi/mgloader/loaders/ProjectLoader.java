package es.uvigo.ei.sing.mahmi.mgloader.loaders;

import es.uvigo.ei.sing.mahmi.common.entities.fasta.GenomeFasta;
import es.uvigo.ei.sing.mahmi.common.entities.fasta.ProteinFasta;
import fj.P2;
import fj.data.Validation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface ProjectLoader {

    public Stream<Validation<IOException, P2<GenomeFasta, ProteinFasta>>> loadProject(final Path projectPath);

}
