package es.uvigo.ei.sing.mahmi.common.serializers.fasta;

import static fj.P.lazy;
import static fj.data.Stream.iterableStream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.AllArgsConstructor;
import lombok.val;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import es.uvigo.ei.sing.mahmi.common.entities.compounds.Compound;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.CompoundSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleobaseSequence;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.OptionExtensionMethods;
import fj.F;
import fj.Monoid;
import fj.P1;
import fj.data.Option;

@Slf4j
@AllArgsConstructor(staticName = "fastaReader")
@ExtensionMethod({ Option.class, OptionExtensionMethods.class })
public final class FastaReader<A extends CompoundSequence<? extends Compound>> {

    private final F<String, Option<A>> constructor;
    private final Monoid<A>            monoid;

    public static FastaReader<AminoAcidSequence> forAminoAcid() {
        return new FastaReader<>(
            AminoAcidSequence::fromString,
            AminoAcidSequence.monoid
        );
    }

    public static FastaReader<NucleobaseSequence> forNucleobase() {
        return new FastaReader<>(
            NucleobaseSequence::fromString,
            NucleobaseSequence.monoid
        );
    }


    public Fasta<A> fromPath(final Path path) throws IOException {
        return fromFile(path.toFile());
    }

    public Fasta<A> fromFile(final File file) throws IOException {
        return fromInput(new FileInputStream(file));
    }

    public Fasta<A> fromInput(final InputStream input) throws IOException {
        try (val reader = new BufferedReader(new InputStreamReader(input))) {

            val sequences   = new java.util.LinkedList<A>();
            val lineCounter = new AtomicInteger();

            String line;
            A sequence = monoid.zero();

            while ((line = reader.readLine()) != null) {
                final int lineNumber = lineCounter.incrementAndGet();
                line = line.trim();

                sequence = line.startsWith(">")
                    ? addSequence(sequences, sequence)
                    : appendLine(sequence, line, lineNumber);
            }

            addSequence(sequences, sequence);
            log.info("Parsed {} sequences from Fasta of {} lines", sequences.size(), lineCounter.get());

            return Fasta.of(iterableStream(sequences));
        }
    }


    private A addSequence(final java.util.List<A> list, final A seq) {
        if (!seq.isEmpty()) list.add(seq);
        return monoid.zero();
    }

    private A appendLine(
        final A sequence, final String line, final int lineNumber
    ) throws IOException {
        return monoid.sum(sequence, parse(line, lineNumber));
    }

    private A parse(final String line, final int lineNumber) throws IOException {
        return constructor.f(line).orThrow(parseError(line, lineNumber));
    }

    private P1<IOException> parseError(
        final String line, final int lineNumber
    ) {
        return lazy(u -> new IOException(String.format(
            "Could not parse sequence before line %d: %s", lineNumber, line
        )));
    }

}
