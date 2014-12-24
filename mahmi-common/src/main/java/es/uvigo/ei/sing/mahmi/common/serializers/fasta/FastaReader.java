package es.uvigo.ei.sing.mahmi.common.serializers.fasta;

import static java.text.MessageFormat.format;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import es.uvigo.ei.sing.mahmi.common.entities.compounds.ChemicalCompound;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.ChemicalCompoundSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.DNASequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import fj.F;
import fj.Monoid;

@Slf4j
@AllArgsConstructor(staticName = "of")
public final class FastaReader<A extends ChemicalCompoundSequence<? extends ChemicalCompound>> {

    private final F<String, A> sequenceConstructor;
    private final Monoid<A>    sequenceMonoid;

    public static FastaReader<AminoAcidSequence> forAminoAcid() {
        return of(AminoAcidSequence::fromString, AminoAcidSequence.monoid);
    }

    public static FastaReader<DNASequence> forDNA() {
        return of(DNASequence::fromString, DNASequence.monoid);
    }


    public Fasta<A> fromPath(final Path path) throws IOException {
        return fromFile(path.toFile());
    }

    public Fasta<A> fromFile(final File file) throws IOException {
        return fromInput(new FileInputStream(file));
    }

    public Fasta<A> fromInput(final InputStream input) throws IOException {
        try (val reader = new BufferedReader(new InputStreamReader(input))) {

            val parsedSequences  = new LinkedHashMap<A, Long>();
            val lineCounter      = new AtomicInteger(0);

            String line;
            A sequence = sequenceMonoid.zero();

            while ((line = reader.readLine()) != null) {
                final int lineNumber = lineCounter.incrementAndGet();
                line = line.trim();

                log.debug("line: {}", lineNumber);
                sequence = line.startsWith(">")
                    ? addSequenceToMap(parsedSequences, sequence)
                    : appendLineToSequence(line, lineNumber, sequence);
            }

            addSequenceToMap(parsedSequences, sequence);
            log.info("Parsed {} sequences from Fasta of {} lines", parsedSequences.size(), lineCounter.get());

            return Fasta.of(parsedSequences);
        }
    }


    private A addSequenceToMap(final Map<A, Long> sequences, final A sequence) {
        if (!sequence.isEmpty()) {
            sequences.put(sequence, sequences.getOrDefault(sequence, 0L) + 1L);
        }

        return sequenceMonoid.zero();
    }

    private A appendLineToSequence(
        final String line, final int lineNumber, final A sequence
    ) throws IOException {
        return sequenceMonoid.sum(sequence, parseSequence(line, lineNumber));
    }

    private A parseSequence(
        final String sequence, final int lineNumber
    ) throws IOException {
        try {
            return sequenceConstructor.f(sequence);

        } catch (final IllegalArgumentException iae) {
            throw new IOException(format(
                "Could not parse sequence previous to line {0}: {1}",
                lineNumber, sequence
            ), iae);
        }
    }

}
