package es.uvigo.ei.sing.mahmi.funpep;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import funpep.data.Analysis;
import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaWriter;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.SHA1;
import es.uvigo.ei.sing.mahmi.database.daos.PeptidesDAO;

import static java.lang.String.format;

import static es.uvigo.ei.sing.mahmi.funpep.FunpepAnalyzer.funpepAnalyzer;

@Slf4j
@AllArgsConstructor(staticName = "funpepCtrl")
public final class FunpepController {

    private static final Config config = ConfigFactory.load("funpep").getConfig("funpep");

    private static final Path   clustalo  = Paths.get(config.getString("clustalo"));
    private static final Path   funpepDB  = Paths.get(config.getString("database"));
    private static final double threshold =           config.getDouble("threshold");
    private static final Path   reference = Paths.get(config.getString("reference"));

    private final PeptidesDAO peptides;

    public void analyze(final MetaGenome metagenome) {
        val mgId = metagenome.getId();
        log.info("FUNPEP: Starting funpep analysis for metagenome " + mgId);

        val comparing = getComparingFasta(metagenome);
        val analyzer  = funpepAnalyzer(clustalo, funpepDB);

        final Function<Analysis, Analysis> onCreated = analysis -> {
            log.info("FUNPEP: Assigned ID " + analysis.id() + " to metagenome " + mgId);
            writeMetagenomeInfo(mgId, analysis);
            return analysis;
        };

        final Runnable onComplete = () -> {
            deleteTemporalFiles(comparing);
            log.info("FUNPEP: Finished analysis for metagenome " + mgId);
        };

        final Consumer<Throwable> onError = t -> log.error("FUNPEP: error while analyzing", t);

        analyzer.unsafeRunFunpep(
            reference, comparing, threshold, onCreated, onComplete, onError
        );
    }

    private void deleteTemporalFiles(final Path ... files) {
        Arrays.stream(files).forEach(file -> {
            try {
                Files.deleteIfExists(file);
            } catch (final IOException e) {
                log.error("FUNPEP: could not delete file " + file.toString());
            }
        });
    }

    private void writeMetagenomeInfo(
        final Identifier mgId, final Analysis analysis
    ) {
        val mgInfoFile = analysis.directory().resolve("metagenome.info");

        try (final PrintWriter writer = new PrintWriter(mgInfoFile.toFile())) {

            writer.print("MAHMI METAGENOME ID: ");
            writer.print(mgId.toString());
            writer.println();

        } catch (IOException e) {
            log.error("FUNPEP: Could not write metagenome.info in " + analysis.directory() + " => " + mgId);
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
