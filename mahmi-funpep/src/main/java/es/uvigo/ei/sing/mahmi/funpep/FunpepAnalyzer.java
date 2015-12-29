package es.uvigo.ei.sing.mahmi.funpep;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;

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
import es.uvigo.ei.sing.mahmi.database.daos.BioactivePeptidesDAO;
import es.uvigo.ei.sing.mahmi.database.daos.PeptidesDAO;
import es.uvigo.ei.sing.mahmi.database.daos.ReferencePeptidesDAO;

import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;
import static java.util.UUID.randomUUID;

import static es.uvigo.ei.sing.mahmi.database.connection.HikariConnectionPool.hikariCP;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLBioactivePeptidesDAO.mysqlBioactivePeptidesDAO;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLPeptidesDAO.mysqlPeptidesDAO;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLReferencePeptidesDAO.mysqlReferencePeptidesDAO;
import static es.uvigo.ei.sing.mahmi.funpep.FileUtils.deleteDirectory;

@Slf4j
@AllArgsConstructor(staticName = "funpep")
public final class FunpepAnalyzer {

    private static final Path   clustalo   = Paths.get("/usr/bin/clustalo");
    private static final Path   funpepDB   = Paths.get("/home/agjacome/mahmi-funpep/database");
    private static final Path   reference  = Paths.get("/home/agjacome/mahmi-funpep/database/reference.fasta");
    private static final double threshold  = 60.0;

    private final int blockCount;

    private final PeptidesDAO          peptides;
    private final ReferencePeptidesDAO referencePeptides;
    private final BioactivePeptidesDAO bioactivePeptides;


    public static void main(final String[] args) {
        final int start      = Integer.parseInt(args[0]);
        final int blockCount = Integer.parseInt(args[1]);

        val connectionPool = hikariCP();
        val funpep = funpep(
            blockCount,
            mysqlPeptidesDAO(connectionPool),
            mysqlReferencePeptidesDAO(connectionPool),
            mysqlBioactivePeptidesDAO(connectionPool)
        );

        try {
            funpep.generateReferenceFasta();
            funpep.startFrom(start);
        } catch (final IOException ioe) {
            log.error("FUNPEP: Execution error", ioe);
        }
    }


    public void generateReferenceFasta() throws IOException {
        log.info("FUNPEP: Generating reference.fasta from database");
        val fasta = Fasta.of(referencePeptides.getAll().iterator());
        Files.createDirectories(reference.getParent());
        FastaWriter.forAminoAcid().toPath(fasta, reference);
    }

    public void startFrom/*TheBottomNowWeHere*/(final int from) throws IOException {
        final int total = 311_445_726; // (int) peptides.count(); // TOO SLOW

        for (int start = from; start < total; start += blockCount) {
            System.gc();
            val analysis = analyze(start, blockCount);
            log.info("FUNPEP: Storing analysis '" + analysis.id() + "' report into DB.");
            persistReportData(analysis.directory().resolve("report.csv"));
            log.info("FUNPEP: Deleting analysis '" + analysis.id() + "' files.");
            deleteDirectory(analysis.directory());
        }
    }

    private void persistReportData(final Path csv) throws IOException {
        Files.lines(csv).skip(1).forEach(line -> {
            final String[] splitted = line.replace("\"", "").split(",");

            val p = SHA1.fromHexString(splitted[0]).some(); // peptide
            val r = SHA1.fromHexString(splitted[1]).some(); // reference
            val s = Double.parseDouble(splitted[2]);        // similarity

            log.debug("FUNPEP: Inserting bioactivity " + p + " → " + r + " [" + s + "]");

            bioactivePeptides.insert(r, p, s);
        });
    }

    private Analysis analyze(
        final int start, final int count
    ) throws IOException {
        log.info("FUNPEP: Reading " + count + " peptides, starting from peptide number " + start);

        val comparing = getComparingFastaPath(start, count);
        val analyzer  = Analyzer.mahmiUnsafeCreate(funpepDB, 10);

        final Analysis analysis = analyzer.mahmiUnsafeRun(
            clustalo, reference, comparing, threshold,
            created -> log.info("FUNPEP: Analysis '" + created.id() + "' started on " + formatDate(created.status().timestamp()))
        ).get();

        log.info("FUNPEP: Analysis '" + analysis.id() + "' finished on " + formatDate(analysis.status().timestamp()));

        log.info("FUNPEP: Deleting temporal files of analysis '" + analysis.id() + "'");
        deleteDirectory(funpepDB.resolve("tmp"));
        deleteDirectory(analysis.directory().resolve("tmp"));

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
