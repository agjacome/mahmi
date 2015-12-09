package es.uvigo.ei.sing.mahmi.funpep;

import static funpep.data.AminoAcid.AminoAcidParser;

import java.nio.file.Path;
import java.util.concurrent.Executors;

import static java.util.Collections.emptyMap;

import static es.uvigo.ei.sing.mahmi.funpep.util.JavaToScala.asScala;
import static es.uvigo.ei.sing.mahmi.funpep.util.JavaToScala.asScalaz;

import funpep.Analyzer;
import funpep.data.Analysis;
import lombok.AllArgsConstructor;
import lombok.val;
import scalaz.concurrent.Task;
import scalaz.stream.Process;

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
        final Path reference,
        final Path comparing,
        final double threshold
    ) {
        val ref = funpep.data.FastaParser$.MODULE$.fromFileW(reference, AminoAcidParser(), asScala(aa -> aa));
        val cmp = funpep.data.FastaParser$.MODULE$.fromFileW(comparing, AminoAcidParser(), asScala(aa -> aa));

        return ref.<Task, Analysis>flatMap(
            asScala(r -> cmp.<Task, Analysis>flatMap(
                asScala(c -> analyzer().create(r, c, threshold, asScalaz(emptyMap()))
        ))));
    }

    public Process<Task, Analysis> clear(final Analysis analysis) {
        return analyzer().clear(analysis);
    }

    public Process<Task, Analysis> analyze(final Analysis analysis) {
        return analyzer().analyze(analysis).run().apply(clustalO);
    }

}
