package es.uvigo.ei.sing.mahmi.loader;

import static es.uvigo.ei.sing.mahmi.psort.PSortFilterType.Extracellular;
import static es.uvigo.ei.sing.mahmi.psort.PSortGramMode.Positive;
import static fj.P.p;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.stream.Collectors;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.CompoundSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleobaseSequence;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaReader;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableExtensionMethods;
import es.uvigo.ei.sing.mahmi.psort.PSortFastaFilter;
import fj.P2;
import fj.data.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;

/**
 * {@linkplain MGRastProjectLoader} is an implementation for {@link ProjectLoader} to loading 
 * MGRast based projects. Is the default project loader of MAHMI
 * 
 * @author Alberto Gutierrez-Jacome
 * @author Aitor Blanco-Miguez
 * 
 * @see Fasta
 * @see FastaReader
 * @see NucleobaseSequence
 * @see AminoAcidSequence
 * @see CompoundSequence
 * @see PSortFastaFilter
 * @see P2
 *
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ExtensionMethod(IterableExtensionMethods.class)
public final class MGRastProjectLoader implements ProjectLoader {

    /**
     * The {@link FastaReader} for {@link NucleobaseSequence}s FASTA
     */
    private static final FastaReader<NucleobaseSequence> genomeReader  = FastaReader.forNucleobase();
    // Protein reader for save all proteins
    // private static final FastaReader<AminoAcidSequence> proteinReader = FastaReader.forAminoAcid();

    // TODO: receive in constructor
    private final PSortFastaFilter psort = PSortFastaFilter.of(
        Positive, Extracellular.single()
    );

    /**
     * Default constructor of {@linkplain MGRastProjectLoader}
     * 
     * @return a new instance of {@linkplain MGRastProjectLoader}
     */
    public static ProjectLoader mgRastLoader() {
        return new MGRastProjectLoader();
    }

    /**
     * Loads a MAHMI project (metagenomes and proteomes) from a folder and insert it on the 
     * database.
     * 
     * @param projectPath The folder containing the project
     * @return The pair of metagenomes and proteomes of the project
     * @throws LoaderException
     */
    @Override
    public Stream<P2<Fasta<NucleobaseSequence>, Fasta<AminoAcidSequence>>> loadProject(
        final Path projectPath
    ) {
        return findFastaFiles(projectPath).map(this::readFastas);
    }

    /**
     * Gets the genomes and proteins {@link Fasta}s of a project
     * 
     * @param projectPath The path to the project
     * @return The genomes and proteins {@link Fasta}s of the project
     */
    private Stream<P2<Path, Path>> findFastaFiles(final Path projectPath) {
        log.info("Searching for Fasta files in {}", projectPath);
        val genomeFiles  = findFiles(projectPath, match("*.fna"));
        val proteinFiles = findFiles(projectPath, match("*.faa"));

        return genomeFiles.zip(proteinFiles);
    }

    /**
     * Gets the genomes and proteins {@link Fasta}s of a single metagenome
     * 
     * @param paths The paths to the FASTA files
     * @return The genomes and proteins {@link Fasta}s of the metagenome
     */
    private P2<Fasta<NucleobaseSequence>, Fasta<AminoAcidSequence>> readFastas(
        final P2<Path, Path> paths
    ) {
        log.info("Reading genome fasta file {} and protein fasta file {}", paths._1(), paths._2());
        val genomes  = readFasta(genomeReader , paths._1());
        // Save all proteins
        // val proteins = readFasta(proteinReader, paths._2());        
        // Save only filter proteins
        val proteins =  filterFasta(paths._2());

        return p(genomes, proteins);
    }

    /**
     * Reads a FASTA file
     * 
     * @param reader The {@link FastaReader}
     * @param file The {@linkplain Path} of the FASTA file
     * @return The read {@link Fasta}
     */
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

    /**
     * Filters FASTA file by subcellular location using PSortB
     * 
     * @param file The {@linkplain Path} of the FASTA file
     * @return The filtered {@link Fasta}
     */
    private Fasta<AminoAcidSequence> filterFasta(final Path file) {
        try {
            log.info("Parsing and filtering sequences from fasta file {}", file);
            return psort.filter(file);
        } catch (final IOException ioe) {
            log.error("I/O error while loading and filtering sequences from file", ioe);
            throw LoaderException.withCause(ioe);
        }
    }

    /**
     * Finds files that matches a pattern on a directory
     * 
     * @param directory The path to the directory
     * @param filePattern The pattern
     * @return The paths that match the pattern
     */
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

    /**
     * Creates a {@linkplain PathMatcher} from a {@linkplain String}
     * 
     * @param pattern The pattern to match
     * @return The created PathMatcher
     */
    private PathMatcher match(final String pattern) {
        return FileSystems.getDefault().getPathMatcher("glob:" + pattern);
    }

}
