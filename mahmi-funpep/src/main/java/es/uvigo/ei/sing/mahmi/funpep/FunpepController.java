package es.uvigo.ei.sing.mahmi.funpep;

import static scalaz.concurrent.Task.taskInstance;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaWriter;
import es.uvigo.ei.sing.mahmi.database.daos.PeptidesDAO;

import static es.uvigo.ei.sing.mahmi.funpep.FunpepAnalyzer.funpepAnalyzer;
import static es.uvigo.ei.sing.mahmi.funpep.util.JavaToScala.asScala;

import funpep.data.Analysis;
import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import scalaz.Catchable;
import scalaz.MonadError;
import scalaz.concurrent.Task;
import scalaz.stream.Process;

@Slf4j
@AllArgsConstructor(staticName = "funpepCtrl")
public final class FunpepController {

    private static final Config config = ConfigFactory.load("funpep").getConfig("funpep");

    private static final double threshold = config.getDouble("threshold");
    private static final Path   reference = Paths.get(config.getString("reference"));

    private static final FunpepAnalyzer analyzer = funpepAnalyzer(
        Paths.get(config.getString("clustalo")),
        Paths.get(config.getString("database"))
    );

    private final PeptidesDAO peptides;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Analysis analyze(final MetaGenome metagenome) {
        val comparing = getComparingFasta(metagenome);

        log.info("FUNPEP: Starting Funpep Process for metagenome " + metagenome.getId().toString() + "...");
        final Process<Task, Analysis> proc = analyzer
            .create(reference, comparing, threshold)
            .map(asScala(a -> {
                log.info("FUNPEP: Assigned ID " + a.id().toString() + " to metagenome " + metagenome.getId().toString());
                return a;
            }))
            .flatMap(asScala(analyzer::analyze));

        final Task<Analysis> task = proc.<Task, Analysis>runLog(
            (MonadError) taskInstance(), (Catchable) taskInstance()
        );

        final Analysis finished = task.run();
        log.info("FUNPEP: Finished analysis " + finished.id() + " for metagenome " + metagenome.getId().toString());

        try (final PrintWriter w = new PrintWriter(finished.directory().resolve("metagenome.info").toFile())) {
            w.println("MAHMI METAGENOME ID: " + metagenome.getId().toString());
        } catch (IOException e) {
            log.error("FUNPEP: Could not write metagenome.info in " + finished.directory() + " => " + metagenome.getId().toString());
        }

        return finished;
    }

    // private Fasta<AminoAcidSequence> getReferenceFasta() {
    //     try {
    //         log.info("FUNPEP: Reading reference fasta from " + reference.toString());
    //         return FastaReader.forAminoAcid().fromPath(reference);
    //     } catch (final IOException ioe) {
    //         throw new RuntimeException("Could not parse reference fasta", ioe);
    //     }
    // }

    private Path getComparingFasta(final MetaGenome metagenome) {
        log.info("FUNPEP: Reading comparing fasta from metagenome " + metagenome.getId().toString());

        final Set<Peptide> ps = new LinkedHashSet<Peptide>();

        peptides.forEachPeptideOfMetagenome(
            metagenome, pss -> ps.addAll(ps)
        );

        val fasta = Fasta.of(ps.stream().map(Peptide::getSequence).iterator());
        val path  = Paths.get(config.getString("database")).resolve("tmp.fasta");
        try {
            FastaWriter.forAminoAcid().toPath(fasta, path);
            return path;
        } catch (IOException e) {
            log.error("Error writing comparing fasta: ", e);
            throw new RuntimeException("Error writing comparing fasta", e);
        }
    }

}
