package es.uvigo.ei.sing.mahmi.loader.loaders;

import static fj.P.p;
import static fj.data.List.iterableList;
import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import es.uvigo.ei.sing.mahmi.common.entities.fasta.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.fasta.GenomeFasta;
import es.uvigo.ei.sing.mahmi.common.entities.fasta.ProteinFasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.ChemicalCompoundSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.DNASequence;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaReader;
import fj.P2;
import fj.data.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MGRastProjectLoader implements ProjectLoader {

    private final FastaReader<DNASequence>       genomeReader  = FastaReader.forDNA();
    private final FastaReader<AminoAcidSequence> proteinReader = FastaReader.forAminoAcid();


    public static ProjectLoader mgRastProjectLoader() {
        return new MGRastProjectLoader();
    }


    @Override
    public Stream<P2<GenomeFasta, ProteinFasta>> loadProject(final Path projectPath) throws LoaderException {
        return findFastaFiles(projectPath).toCollection().stream().map(paths -> {
            val genomePath  = paths._1();
            val proteinPath = paths._2();

            try {
                return getFastas(genomePath, proteinPath);
            } catch (final IOException ioe) {
                throw LoaderException.withCause(ioe);
            }
        });
    }


    private P2<GenomeFasta, ProteinFasta> getFastas(final Path genomePath, final Path proteinPath) throws IOException {
        log.info("Reading genome fasta file {} and protein fasta file {}", genomePath, proteinPath);
        val genomes  = (GenomeFasta)  getFasta(genomeReader, genomePath);
        val proteins = (ProteinFasta) getFasta(proteinReader, proteinPath);

        return p(genomes, proteins);
    }

    private <A extends ChemicalCompoundSequence<?>> Fasta<A> getFasta(final FastaReader<A> reader, final Path file) throws IOException {
        log.info("Reading Fasta file {}", file);
        return reader.fromPath(file);
    }

    private List<P2<Path, Path>> findFastaFiles(final Path projectPath) {
        log.info("Searching for Fasta files in {}", projectPath);
        val genomeFiles  = iterableList(findFiles(projectPath, match("*.fna")));
        val proteinFiles = iterableList(findFiles(projectPath, match("*.faa")));

        return genomeFiles.zip(proteinFiles);
    }

    private PathMatcher match(final String pattern) {
        return FileSystems.getDefault().getPathMatcher("glob:" + pattern);
    }

    private Collection<Path> findFiles(final Path directory, final PathMatcher filePattern) {
        // TODO: replace with a Files.walkFileTree??
        final File[ ] files = directory.toFile().listFiles();
        if (isNull(files)) return emptyList();

        val buffer = new ArrayList<Path>();

        for (val file : files) {
            val path = file.toPath();

            if (file.isDirectory())
                buffer.addAll(findFiles(path, filePattern));
            else if (filePattern.matches(path.getFileName())) {
                log.debug("Found file {}", path);
                buffer.add(path);
            }
        }

        return buffer;
    }

}
