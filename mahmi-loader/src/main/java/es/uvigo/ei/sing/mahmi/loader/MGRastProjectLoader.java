package es.uvigo.ei.sing.mahmi.loader;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.stream.Collectors;

import fj.P2;
import fj.data.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.CompoundSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleobaseSequence;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaReader;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableExtensionMethods;
import es.uvigo.ei.sing.mahmi.psort.PSortFastaFilter;

import static fj.P.p;

import static es.uvigo.ei.sing.mahmi.psort.PSortFilterType.Cytoplasmic;
import static es.uvigo.ei.sing.mahmi.psort.PSortFilterType.CytoplasmicMembrane;
import static es.uvigo.ei.sing.mahmi.psort.PSortGramMode.Positive;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ExtensionMethod(IterableExtensionMethods.class)
public final class MGRastProjectLoader implements ProjectLoader {

    private static final FastaReader<NucleobaseSequence> genomeReader = FastaReader.forNucleobase();
//    private static final FastaReader<AminoAcidSequence> proteinReader = FastaReader.forAminoAcid();

    // TODO: receive in constructor
    private final PSortFastaFilter psort = PSortFastaFilter.of(
        Positive, Cytoplasmic.or(CytoplasmicMembrane)//Extracellular.single()
    );

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

        return genomeFiles.zip(proteinFiles);
    }

    private P2<Fasta<NucleobaseSequence>, Fasta<AminoAcidSequence>> readFastas(
        final P2<Path, Path> paths
    ) {
        log.info("Reading genome fasta file {} and protein fasta file {}", paths._1(), paths._2());
        val genomes  = readFasta(genomeReader , paths._1());
        val proteins = /*readFasta(proteinReader, paths._2());*/filterFasta(paths._2());

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

    private Fasta<AminoAcidSequence> filterFasta(final Path file) {
        try {
            log.info("Parsing and filtering sequences from fasta file {}", file);
            return psort.filter(file);
        } catch (final IOException ioe) {
            log.error("I/O error while loading and filtering sequences from file", ioe);
            throw LoaderException.withCause(ioe);
        }
    }

    private Stream<Path> findFiles(
        final Path directory, final PathMatcher filePattern
    ) {
        try {
            return Files.walk(directory).filter(
                path -> filePattern.matches(path.getFileName())
            ).collect(Collectors.toList()).toStream();
        } catch (final IOException ioe) {
            log.error("I/O error while searching Fasta files", ioe);
            throw LoaderException.withCause(ioe);
        }
    }

    private PathMatcher match(final String pattern) {
        return FileSystems.getDefault().getPathMatcher("glob:" + pattern);
    }

}
