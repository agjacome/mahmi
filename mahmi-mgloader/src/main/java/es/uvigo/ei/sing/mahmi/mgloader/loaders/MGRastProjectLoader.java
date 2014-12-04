package es.uvigo.ei.sing.mahmi.mgloader.loaders;

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
import fj.data.Validation;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MGRastProjectLoader implements ProjectLoader {

    private final FastaReader<DNASequence>       genomeReader  = FastaReader.forDNA();
    private final FastaReader<AminoAcidSequence> proteinReader = FastaReader.forAminoAcid();


    public static ProjectLoader mgRastProjectLoader() {
        return new MGRastProjectLoader();
    }


    @Override
    public Stream<Validation<IOException, P2<GenomeFasta, ProteinFasta>>> loadProject(final Path projectPath) {
        return findFastaFiles(projectPath).toCollection().stream().map(
            paths -> getFastas(paths._1(), paths._2())
        );
    }


    private Validation<IOException, P2<GenomeFasta, ProteinFasta>> getFastas(
        final Path genomePath, final Path proteinPath
    ) {
        log.info("Reading genome fasta file {} and protein fasta file {}", genomePath, proteinPath);
        return getFasta(genomeReader,  genomePath).bind(
            gs -> getFasta(proteinReader, proteinPath).map(
                ps -> p(gs, ps).split(this::toGenomeFasta, this::toProteinFasta)
            )
        );
    }

    private <A extends ChemicalCompoundSequence<?>> Validation<IOException, Fasta<A>> getFasta(
        final FastaReader<A> reader, final Path file
    ) {
        log.info("Reading Fasta file {}", file);
        return reader.fromPath(file);
    }

    private GenomeFasta toGenomeFasta(final Fasta<DNASequence> fasta) {
        return GenomeFasta.of(fasta.getSequences());
    }

    private ProteinFasta toProteinFasta(final Fasta<AminoAcidSequence> fasta) {
        return ProteinFasta.of(fasta.getSequences());
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
            else if (filePattern.matches(path.getFileName()))
                buffer.add(path);
        }

        return buffer;
    }

}
