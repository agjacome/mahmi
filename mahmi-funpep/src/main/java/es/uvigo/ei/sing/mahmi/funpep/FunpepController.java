package es.uvigo.ei.sing.mahmi.funpep;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import funpep.data.Analysis;
import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import scalaz.Catchable;
import scalaz.MonadError;
import scalaz.concurrent.Task;
import scalaz.stream.Process;

import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaWriter;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.SHA1;
import es.uvigo.ei.sing.mahmi.database.daos.PeptidesDAO;

import static java.lang.String.format;

import static scalaz.concurrent.Task.taskInstance;

import static es.uvigo.ei.sing.mahmi.funpep.FunpepAnalyzer.funpepAnalyzer;
import static es.uvigo.ei.sing.mahmi.funpep.util.JavaToScala.asScala;

@Slf4j
@AllArgsConstructor(staticName = "funpepCtrl")
public final class FunpepController {

    private static final Config config = ConfigFactory.load("funpep").getConfig("funpep");

    private static final Path   clustalo  = Paths.get(config.getString("clustalo"));
    private static final Path   funpepDB  = Paths.get(config.getString("database"));
    private static final double threshold =           config.getDouble("threshold");
    private static final Path   reference = Paths.get(config.getString("reference"));

    private final PeptidesDAO peptides;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Analysis analyze(final MetaGenome metagenome) {
        log.info("FUNPEP: Starting Funpep Process for metagenome " + metagenome.getId().toString() + "...");

        val comparing = getComparingFasta(metagenome);
        val analyzer  = funpepAnalyzer(clustalo, funpepDB);

        final Process<Task, Analysis> proc = analyzer.create(
            reference, comparing, threshold
        ).map(asScala(a -> {
            log.info("FUNPEP: Assigned ID " + a.id().toString() + " to metagenome " + metagenome.getId().toString());
            return a;
        })).flatMap(asScala(analyzer::analyze));

        final Task<Analysis> task = proc.<Task>run(
            (MonadError) taskInstance(), (Catchable) taskInstance()
        );

        final Analysis finished = task.run();
        log.info("FUNPEP: Finished analysis " + finished.id() + " for metagenome " + metagenome.getId().toString());

        writeMetagenomeInfo(metagenome.getId(), finished);
        deleteTemporalFiles(comparing);

        return finished;
    }

    private void deleteTemporalFiles(final Path ... files) {
        Arrays.stream(files).forEach(file -> {
            try {
                Files.deleteIfExists(file);
            } catch (final IOException e) {
                log.error("FUNPEP: could not deletefile " + file.toString());
            }
        });
    }

    private void writeMetagenomeInfo(
        final Identifier metagenomeId, final Analysis analysis
    ) {
        val mgInfoFile = analysis.directory().resolve("metagenome.info");

        try (final PrintWriter writer = new PrintWriter(mgInfoFile.toFile())) {

            writer.print("MAHMI METAGENOME ID: ");
            writer.print(metagenomeId.toString());
            writer.println();

        } catch (IOException e) {
            log.error("FUNPEP: Could not write metagenome.info in " + analysis.directory() + " => " + metagenomeId.toString());
        }
    }

    private Path getComparingFasta(final MetaGenome metagenome) {
        try {
            Files.createDirectories(funpepDB.resolve("tmp"));
        } catch (IOException e1) {
            log.error("FUNPEP: Could not create temporal dir " + funpepDB.resolve("tmp").toString());
        }

        val mgId = metagenome.getId().toString();
        val uuid = UUID.randomUUID().toString();
        val path = funpepDB.resolve("tmp").resolve(
            format("mg%s-%s.fasta", mgId, uuid)
        );

        val mgPeptides = peptides.getAllPeptidesFromMetagenome(metagenome);
        val fasta  = Fasta.of(mgPeptides.map(
            SHA1.ord.contramap(AminoAcidSequence::getSHA1),
            Peptide::getSequence
        ).iterator());

        try {
            FastaWriter.forAminoAcid().toPath(fasta, path);
            return path;
        } catch (final IOException e) {
            log.error("Error writing comparing fasta: ", e);
            throw new RuntimeException("Error writing comparing fasta", e);
        }
    }

}
