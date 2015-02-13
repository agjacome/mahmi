package es.uvigo.ei.sing.mahmi.common.serializers.fasta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.AllArgsConstructor;
import lombok.val;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;

import fj.F;
import fj.Monoid;
import fj.P1;
import fj.data.Option;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.Compound;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.CompoundSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleobaseSequence;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.OptionExtensionMethods;

import static fj.P.lazy;
import static fj.data.Option.none;
import static fj.data.Option.some;

@Slf4j
@AllArgsConstructor(staticName = "fastaReader")
@ExtensionMethod(OptionExtensionMethods.class)
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
        return fromReader(Files.newBufferedReader(path));
    }

    public Fasta<A> fromInput(final InputStream input) throws IOException {
        return fromReader(new BufferedReader(new InputStreamReader(
            input, StandardCharsets.UTF_8
        )));
    }

    // FIXME: receive a "Reader", and create the "BufferedReader" internally
    public Fasta<A> fromReader(final BufferedReader reader) throws IOException {
        return Fasta.of(new Iterator<A>() {

            private final AtomicInteger line = new AtomicInteger(1);

            private Option<A> sequence = getNextSequence(
                reader, line.incrementAndGet()
            );

            @Override
            public boolean hasNext() {
                val hasNext = sequence.isSome();

                try {
                    if (!hasNext) reader.close();
                } catch (final IOException ioe) {
                    log.error("Error while closing Fasta reader", ioe);
                    throw new RuntimeException(ioe);
                }

                return hasNext;
            }

            @Override
            public A next() {
                final A toRet = sequence.orThrow(nextError(line.get()));

                try {
                    sequence = getNextSequence(reader, line.incrementAndGet());
                } catch (final IOException ioe) {
                    log.error("Error while parsing Fasta input", ioe);
                    sequence = none();
                }

                return toRet;
            }

        });
    }

    public Option<A> getNextSequence(
        final BufferedReader reader, final int lineNumber
    ) throws IOException {
        String line = null;
        A sequence = monoid.zero();

        while ((line = reader.readLine()) != null) {
            if  (!line.startsWith(">"))
                sequence = monoid.sum(sequence, parseLine(line, lineNumber));
            else if (!sequence.isEmpty())
                return some(sequence);
        }

        return Option.iif(!sequence.isEmpty(), sequence);
    }

    private A parseLine(
        final String line, final int lineNumber
    ) throws IOException {
        return constructor.f(line.trim()).orThrow(
            parseError(line, lineNumber)
        );
    }

    private P1<NoSuchElementException> nextError(final int lineNumber) {
        return lazy(u -> new NoSuchElementException(String.format(
            "No more sequences found after line %d", lineNumber
        )));
    }

    private P1<IOException> parseError(
        final String line, final int lineNumber
    ) {
        return lazy(u -> new IOException(String.format(
            "Could not parse sequence before line %d: %s", lineNumber, line
        )));
    }

}
