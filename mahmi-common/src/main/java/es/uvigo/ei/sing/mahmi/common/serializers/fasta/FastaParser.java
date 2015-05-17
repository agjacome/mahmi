package es.uvigo.ei.sing.mahmi.common.serializers.fasta;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Function;

import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Parsers;
import org.codehaus.jparsec.pattern.CharPredicate;
import org.codehaus.jparsec.pattern.CharPredicates;
import org.codehaus.jparsec.pattern.Pattern;
import org.codehaus.jparsec.pattern.Patterns;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.Compound;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.CompoundSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleotideSequence;
import es.uvigo.ei.sing.mahmi.common.utils.Tuple;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.IOUtils;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.joining;

import static es.uvigo.ei.sing.mahmi.common.utils.Contracts.requireNonNull;
import static es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableUtils.mapify;

public final class FastaParser<A extends CompoundSequence<? extends Compound>> {

    public static final Parser<Tuple<String, String>> rawEntryParser;
    public static final Parser<Map<String, String>>   rawFastaParser;

    static {
        final CharPredicate sequenceChar = CharPredicates.or(CharPredicates.IS_ALPHA, CharPredicates.isChar('*'), CharPredicates.isChar('-'));

        final Pattern lineBreak    = Patterns.many(CharPredicates.isChar ('\n'));
        final Pattern notLineBreak = Patterns.many(CharPredicates.notChar('\n'));

        final Pattern headerPattern  = Patterns.isChar('>').next(notLineBreak).next(lineBreak);
        final Pattern seqLinePattern = Patterns.isChar(sequenceChar).atLeast(1).next(lineBreak);

        final Parser<String> header   = headerPattern.toScanner("Secuence Header").source().map(s -> s.substring(1).trim());
        final Parser<String> seqLine  = seqLinePattern.toScanner("Sequence Line").source().map(s -> s.trim());
        final Parser<String> sequence = seqLine.atLeast(1).map(lines -> lines.stream().collect(joining()));

        rawEntryParser = Parsers.sequence(header, sequence, Tuple::of);
        rawFastaParser = rawEntryParser.many().map(entries -> mapify(entries, Tuple::getLeft, Tuple::getRight));
    }

    public static <A extends CompoundSequence<? extends Compound>> Parser<Tuple<String, A>> typedEntryParser(
        final Function<String, A> sequenceMapper
    ) {
        return rawEntryParser.map(tuple -> tuple.map(identity(), sequenceMapper));
    }

    public static <A extends CompoundSequence<? extends Compound>> Parser<Map<String, A>> typedFastaParser(
        final Function<String, A> sequenceMapper
    ) {
        return typedEntryParser(sequenceMapper).many().map(
            entries -> mapify(entries, Tuple::getLeft, Tuple::getRight)
        );
    }

    // ---------------------------

    private final Parser<Map<String, A>> parser;

    private FastaParser(final Function<String, A> sequencer) {
        this.parser = typedFastaParser(requireNonNull(sequencer, "FastaParser sequence mapper cannot be NULL"));
    }

    public static <A extends CompoundSequence<? extends Compound>> FastaParser<A> of(
        final Function<String, A> sequencer
    ) {
        return new FastaParser<>(sequencer);
    }

    public static FastaParser<AminoAcidSequence> forAminoAcidSequences() {
        return new FastaParser<>(
            str -> AminoAcidSequence.fromString(str).orElseThrow(
                () -> new RuntimeException(new IOException("Could not correctly parse AminoAcid sequence:\n" + str))
            )
        );
    }

    public static FastaParser<NucleotideSequence> forNucleotideSequences() {
        return new FastaParser<>(
            str -> NucleotideSequence.fromString(str).orElseThrow(
                () -> new RuntimeException(new IOException("Could not correctly parse Nucleotide sequence:\n" + str))
            )
        );
    }

    public Fasta<A> parseFile(final Path path) throws IOException {
        return parseString(IOUtils.read(path));
    }

    public Fasta<A> parseReadable(final Readable readable) throws IOException {
        return parseString(IOUtils.read(readable));
    }

    public Fasta<A> parseString(final String str) throws IOException {
        return Fasta.of(parser.parse(str, "Fasta Parser"));
    }

}
