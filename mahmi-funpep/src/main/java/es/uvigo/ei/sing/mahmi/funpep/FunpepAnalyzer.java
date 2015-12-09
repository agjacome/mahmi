package es.uvigo.ei.sing.mahmi.funpep;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.Executors;

import funpep.Analyzer;
import funpep.data.Analysis;
import lombok.AllArgsConstructor;
import lombok.val;
import scalaz.concurrent.Task;
import scalaz.stream.Process;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;

import static funpep.data.AminoAcid.AminoAcidParser;

import static es.uvigo.ei.sing.mahmi.funpep.util.JavaToScala.asScala;
import static es.uvigo.ei.sing.mahmi.funpep.util.JavaToScala.asScalaz;
import static es.uvigo.ei.sing.mahmi.funpep.util.MahmiToFunpep.asFunpep;

@SuppressWarnings({ "rawtypes", "unchecked" })
@AllArgsConstructor(staticName = "funpepAnalyzer")
final class FunpepAnalyzer {

    private final Path clustalO;
    private final Path funpepDB;

    private Analyzer<funpep.data.AminoAcid> analyzer() {
        val strategy = scalaz.concurrent.Strategy$.MODULE$.Executor(
            Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() * 10,
                scalaz.concurrent.Strategy$.MODULE$.DefaultDaemonThreadFactory()
            )
        );

        return Analyzer.apply(
            funpepDB,
            strategy,
            AminoAcidParser(),
            asScala(aa -> aa)
        );
    }

    public Process<Task, Analysis> create(
        final Fasta<AminoAcidSequence> reference,
        final Fasta<AminoAcidSequence> comparing,
        final double                   threshold,
        final Map<String, String>      annotations
    ) {
        return analyzer().create(
            asFunpep(reference),
            asFunpep(comparing),
            threshold,
            asScalaz(annotations)
        );
    }

    public Process<Task, Analysis> clear(final Analysis analysis) {
        return analyzer().clear(analysis);
    }

    public Process<Task, Analysis> analyze(final Analysis analysis) {
        return analyzer().analyze(analysis).run().apply(clustalO);
    }

}
