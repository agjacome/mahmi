package es.uvigo.ei.sing.mahmi.common.serializers.fasta;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;

import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Parsers;
import org.codehaus.jparsec.pattern.CharPredicate;
import org.codehaus.jparsec.pattern.CharPredicates;
import org.codehaus.jparsec.pattern.Pattern;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.Compound;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.CompoundSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleotideSequence;
import es.uvigo.ei.sing.mahmi.common.utils.Tuple;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.IOUtils;

import static java.util.stream.Collectors.joining;

import static org.codehaus.jparsec.pattern.Patterns.isChar;
import static org.codehaus.jparsec.pattern.Patterns.many;

import static es.uvigo.ei.sing.mahmi.common.utils.Contracts.requireNonNull;
import static es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableUtils.mapify;

public final class FastaParser<A extends CompoundSequence<? extends Compound>> {

    private static final Parser<Tuple<String, String>> rawEntryParser;
    private static final Parser<Map<String, String>>   rawFastaParser;

    static {
        final CharPredicate sequenceChar = CharPredicates.or(CharPredicates.IS_ALPHA, CharPredicates.isChar('*'));

        final Pattern lineBreak    = many(CharPredicates.isChar ('\n'));
        final Pattern notLineBreak = many(CharPredicates.notChar('\n'));

        final Pattern headerPattern  = isChar('>').next(notLineBreak).next(lineBreak);
        final Pattern seqLinePattern = isChar(sequenceChar).atLeast(1).next(lineBreak);

        final Parser<String> header   = headerPattern.toScanner("Secuence Header").source().map(str -> str.substring(1).trim());
        final Parser<String> seqLine  = seqLinePattern.toScanner("Sequence Line").source().map(str -> str.trim());
        final Parser<String> sequence = seqLine.atLeast(1).map(lines -> lines.stream().collect(joining()));

        rawEntryParser = Parsers.sequence(header, sequence, Tuple::of);
        rawFastaParser = rawEntryParser.many().map(entries -> mapify(entries, Tuple::getLeft, Tuple::getRight));
    }

    private final Function<String, Optional<A>> sequenceMapper;

    private FastaParser(final Function<String, Optional<A>> sequenceMapper) {
        this.sequenceMapper = requireNonNull(sequenceMapper, "FastaParser sequence mapper cannot be NULL");
    }

    public static <A extends CompoundSequence<? extends Compound>> FastaParser<A> of(
        final Function<String, Optional<A>> sequenceMapper
    ) {
        return new FastaParser<>(sequenceMapper);
    }

    public static FastaParser<AminoAcidSequence> forAminoAcidSequences() {
        return new FastaParser<>(AminoAcidSequence::fromString);
    }

    public static FastaParser<NucleotideSequence> forNucleotideSequences() {
        return new FastaParser<>(NucleotideSequence::fromString);
    }

    public Fasta<A> parseFile(final Path path) throws IOException {
        return parseString(IOUtils.read(path));
    }

    public Fasta<A> parseReadable(final Readable readable) throws IOException {
        return parseString(IOUtils.read(readable));
    }

    // FIXME: can be done more efficiently. Make rawFastaParser a function that
    // receives the sequenceMapper and maps the values as they are being parsed.
    // That way, the file/string has to be walked only one time, instead of one
    // in the parsing phase and another one in the "conversion" phase.
    // Raw-parsing in local tests is around 0.2 seconds, parsing-and-conversion
    // with the implemented version is around 1.3 seconds (tested with Fasta of
    // 37296 sequences).
    public Fasta<A> parseString(final String str) throws IOException {
        final Map<String, A> sequences = new HashMap<>();

        for (final Entry<String, String> entry : rawFastaParser.parse(str, "Fasta Parser").entrySet()) {
            final String header   = entry.getKey();
            final String sequence = entry.getValue();

            sequences.put(header, sequenceMapper.apply(sequence).orElseThrow(
                () -> new IOException("Could not correctly parse sequence:\n" + sequence)
            ));
        }

        return Fasta.of(sequences);
    }

}
