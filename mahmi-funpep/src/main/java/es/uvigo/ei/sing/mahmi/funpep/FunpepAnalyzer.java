package es.uvigo.ei.sing.mahmi.funpep;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import funpep.Analyzer;
import funpep.data.Analysis;
import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaWriter;
import es.uvigo.ei.sing.mahmi.common.utils.SHA1;
import es.uvigo.ei.sing.mahmi.database.daos.PeptidesDAO;

import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;
import static java.util.UUID.randomUUID;

@Slf4j
@AllArgsConstructor(staticName = "funpep")
public final class FunpepAnalyzer {

    private static final Config config = ConfigFactory.load("funpep").getConfig("funpep");

    private static final Path   clustalo  = Paths.get(config.getString("clustalo"));
    private static final Path   funpepDB  = Paths.get(config.getString("database"));
    private static final Path   reference = Paths.get(config.getString("reference"));
    private static final double threshold =           config.getDouble("threshold");

    private final PeptidesDAO peptides;

    public Analysis analyze(
        final int start, final int count
    ) throws IOException {
        log.info("FUNPEP: Reading " + count + " peptides, starting from peptide number " + start);

        val comparing = getComparingFastaPath(start, count);
        val analyzer  = Analyzer.mahmiUnsafeCreate(funpepDB, 50);

        final Analysis analysis = analyzer.mahmiUnsafeRun(
            clustalo, reference, comparing, threshold,
            created -> log.info("FUNPEP: Analysis '" + created.id() + "' started on " + formatDate(created.status().timestamp()))
        ).get();

        log.info("FUNPEP: Analysis '" + analysis.id() + "' finished on " + formatDate(analysis.status().timestamp()));

        log.info("FUNPEP: Deleting temporal files of analysis '" + analysis.id() + "'");
        FileUtils.deleteDirectory(funpepDB.resolve("tmp"));
        FileUtils.deleteDirectory(analysis.directory().resolve("tmp"));

        return analysis;
    }

    private Path getComparingFastaPath(
        final int start, final int count
    ) throws IOException {
        val sequences = peptides.getAll(start, count).map(
            SHA1.ord.contramap(AminoAcidSequence::getSHA1),
            Peptide::getSequence
        );

        val path = funpepDB.resolve("tmp").resolve(randomUUID() + ".fasta");

        Files.createDirectories(funpepDB.resolve("tmp"));
        FastaWriter.forAminoAcid().toPath(Fasta.of(sequences.iterator()), path);

        return path;
    }

    private String formatDate(final Instant instant) {
        return instant.atZone(ZoneId.systemDefault()).format(RFC_1123_DATE_TIME);
    }

}
