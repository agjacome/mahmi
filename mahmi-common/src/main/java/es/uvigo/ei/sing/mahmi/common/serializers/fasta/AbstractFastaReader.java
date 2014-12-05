package es.uvigo.ei.sing.mahmi.common.serializers.fasta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import es.uvigo.ei.sing.mahmi.common.entities.compounds.ChemicalCompound;
import es.uvigo.ei.sing.mahmi.common.entities.fasta.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.ChemicalCompoundSequence;
import fj.Monoid;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PROTECTED)
abstract class AbstractFastaReader<A extends ChemicalCompoundSequence<? extends ChemicalCompound>> implements FastaReader<A> {

    private final Monoid<A> sequenceMonoid = getSequenceMonoid();

    @Override
    public Fasta<A> fromInput(final InputStream inputStream) throws IOException {
        // FIXME: quite ugly code here, try to clean up a bit.
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            val parsedSequences  = new LinkedHashMap<A, Long>();
            val lineCounter      = new AtomicInteger(0);

            String line;
            A previousSequence = sequenceMonoid.zero();

            while ((line = reader.readLine()) != null) {
                final int lineNumber = lineCounter.incrementAndGet();
                line = line.trim();

                previousSequence = line.startsWith(">")
                    ? addSequenceToMap(parsedSequences, previousSequence)
                    : appendLineToSequence(line, lineNumber, previousSequence);
            }

            addSequenceToMap(parsedSequences, previousSequence);
            log.info("Parsed {} sequences from Fasta of {} lines", parsedSequences.size(), lineCounter.get());

            return getFastaFromMap(parsedSequences);
        }
    }

    private A addSequenceToMap(final Map<A, Long> sequences, final A sequence) {
        if (!sequence.isEmpty())
            sequences.put(sequence, sequences.getOrDefault(sequence, 0L) + 1L);

        return sequenceMonoid.zero();
    }

    private A appendLineToSequence(final String line, final int lineNumber, final A sequence) throws IOException {
        return sequenceMonoid.sum(sequence, parseSequenceLine(line, lineNumber));
    }

    private A parseSequenceLine(final String line, final int lineNumber) throws IOException {
        try {
            return getSequenceFromString(line);
        } catch (final Exception e) {
            log.error("Could not parse line {0} ase sequence: {1}", lineNumber, line);
            throw new IOException(e);
        }
    }

    protected abstract Monoid<A> getSequenceMonoid();

    protected abstract Fasta<A> getFastaFromMap(final Map<A, Long> map);

    protected abstract A getSequenceFromString(final String str);

}
