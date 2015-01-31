package es.uvigo.ei.sing.mahmi.loader;

import static es.uvigo.ei.sing.mahmi.common.utils.extensions.CollectionExtensionMethods.combine;
import static fj.P.p;
import static java.util.Objects.isNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.CompoundSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleobaseSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaReader;
import fj.P2;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MGRastProjectLoader implements ProjectLoader {

    private static final FastaReader<NucleobaseSequence>       genomeReader  = FastaReader.forNucleobase();
    private static final FastaReader<AminoAcidSequence> proteinReader = FastaReader.forAminoAcid();

    public static ProjectLoader mgRastLoader() {
        return new MGRastProjectLoader();
    }

    @Override
    public Stream<P2<Fasta<NucleobaseSequence>, Fasta<AminoAcidSequence>>> loadProject(
        final Path projectPath
    ) {
        return findFastaFiles(projectPath).map(this::readFastas);
    }

    private Stream<P2<Path, Path>> findFastaFiles(final Path projectPath) {
        log.info("Searching for Fasta files in {}", projectPath);
        val genomeFiles  = findFiles(projectPath, match("*.fna"));
        val proteinFiles = findFiles(projectPath, match("*.faa"));

        return combine(genomeFiles, proteinFiles).stream();
    }

    private P2<Fasta<NucleobaseSequence>, Fasta<AminoAcidSequence>> readFastas(
        final P2<Path, Path> paths
    ) {
        log.info("Reading genome fasta file {} and protein fasta file {}", paths._1(), paths._2());
        val genomes  = readFasta(genomeReader , paths._1());
        val proteins = readFasta(proteinReader, paths._2());

        return p(genomes, proteins);
    }

    private <A extends CompoundSequence<?>> Fasta<A> readFasta(
        final FastaReader<A> reader, final Path file
    ) {
        try {
            log.info("Parsing sequences from fasta file {}", file);
            return reader.fromPath(file);
        } catch (final IOException ioe) {
            log.error("I/O error while loading sequences from file", ioe);
            throw LoaderException.withCause(ioe);
        }
    }

    private List<Path> findFiles(
        final Path directory, final PathMatcher filePattern
    ) {
        return findFiles(directory, filePattern, new LinkedList<Path>());
    }

    private List<Path> findFiles(
        final Path        directory,
        final PathMatcher filePattern,
        final List<Path>  accumulator
    ) {
        final File[ ] files = directory.toFile().listFiles();
        if (isNull(files)) return accumulator;

        for (val file : files) {
            val path = file.toPath();

            if (file.isDirectory())
                findFiles(path, filePattern, accumulator);
            else if (filePattern.matches(path.getFileName()))
                accumulator.add(path);
        }

        return accumulator;
    }

    private PathMatcher match(final String pattern) {
        return FileSystems.getDefault().getPathMatcher("glob:" + pattern);
    }

}
