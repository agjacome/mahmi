package es.uvigo.ei.sing.mahmi.common.serializers.fasta;

import static fj.data.Validation.fail;
import static fj.data.Validation.success;

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
import fj.data.Option;
import fj.data.Validation;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PROTECTED)
abstract class AbstractFastaReader<A extends ChemicalCompoundSequence<? extends ChemicalCompound>> implements FastaReader<A> {

    private final Monoid<A> sequenceMonoid = getSequenceMonoid();

    @Override
    public Validation<IOException, Fasta<A>> fromInput(final InputStream inputStream) {
        // FIXME: quite ugly code here, try to clean up a bit.
        // Also, it can be easy parallelized: operations over parsedSequence
        // are fully associative (meaning it doesn't really matter in what
        // order we parse the file: no LinkedHashMap required). Think
        // algebraically.
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            val parsedSequences  = new LinkedHashMap<A, Integer>();
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

            return success(getFastaFromMap(parsedSequences));

        } catch (final IOException ioe) {
            return fail(ioe);
        }
    }

    private A addSequenceToMap(final Map<A, Integer> sequences, final A sequence) {
        if (!sequence.isEmpty())
            sequences.put(sequence, sequences.getOrDefault(sequence, 0) + 1);

        return sequenceMonoid.zero();
    }

    private A appendLineToSequence(final String line, final int lineNumber, final A sequence) throws IOException {
        return sequenceMonoid.sum(sequence, parseSequenceLine(line, lineNumber));
    }

    private A parseSequenceLine(final String line, final int lineNumber) throws IOException {
        val sequence = getSequenceFromString(line);

        if (sequence.isNone()) {
            throw new IOException(String.format(
                "Could not parse line %d as sequence: %s", lineNumber, line
            ));
        } else return sequence.some();
    }

    protected abstract Monoid<A> getSequenceMonoid();

    protected abstract Fasta<A> getFastaFromMap(final Map<A, Integer> map);

    protected abstract Option<A> getSequenceFromString(final String str);

}
