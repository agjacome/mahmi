package es.uvigo.ei.sing.mahmi.common.serializers.fasta;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.Compound;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.CompoundSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleotideSequence;
import es.uvigo.ei.sing.mahmi.common.utils.Tuple;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.StringUtils;

public final class FastaPrinter<A extends CompoundSequence<? extends Compound>> {

    private static final int MAX_LINE_LENGTH = 70;

    public static FastaPrinter<AminoAcidSequence> forAminoAcidSequences() {
        return new FastaPrinter<AminoAcidSequence>();
    }

    public static FastaPrinter<NucleotideSequence> forNucleotideSequences() {
        return new FastaPrinter<NucleotideSequence>();
    }

    public void printFile(
        final Fasta<A> fasta, final Path file
    ) throws IOException {
        try (final BufferedWriter writer = Files.newBufferedWriter(file)) {
            printAppendable(fasta, writer);
        }
    }

    public void printAppendable(
        final Fasta<A> fasta, final Appendable appendable
    ) throws IOException {
        appendable.append(printString(fasta));
    }

    public String printString(final Fasta<A> fasta) throws IOException {
        final StringBuilder sb = new StringBuilder();

        for (final Tuple<String, A> entry : fasta) {
            final String header   = entry.left;
            final String sequence = entry.right.toString();

            sb.append(">").append(header).append('\n');

            StringUtils.splitFixedLength(sequence, MAX_LINE_LENGTH).forEach(
                seqLine -> sb.append(seqLine).append('\n')
            );
        }

        return sb.toString();
    }

}
