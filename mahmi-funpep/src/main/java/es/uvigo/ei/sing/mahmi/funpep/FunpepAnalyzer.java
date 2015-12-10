package es.uvigo.ei.sing.mahmi.funpep;

import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

import funpep.Analyzer;
import funpep.data.Analysis;
import lombok.AllArgsConstructor;
import lombok.val;

import static funpep.data.AminoAcid.AminoAcidParser;

import static es.uvigo.ei.sing.mahmi.funpep.util.JavaToScala.asScala;

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

    public void unsafeRunFunpep(
        final Path reference,
        final Path comparing,
        final double threshold,
        final Function<Analysis, Analysis> onCreated,
        final Runnable onComplete,
        final Consumer<Throwable> onError
    ) {
        analyzer().mahmiUnsafeRun(
            reference, comparing, threshold, clustalO, onCreated, onComplete, onError
        );
    }

}
