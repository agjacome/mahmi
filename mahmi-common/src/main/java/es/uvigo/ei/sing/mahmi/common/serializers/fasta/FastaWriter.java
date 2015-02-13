package es.uvigo.ei.sing.mahmi.common.serializers.fasta;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import lombok.AllArgsConstructor;
import lombok.val;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.Compound;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.CompoundSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleobaseSequence;

import static java.lang.System.lineSeparator;

@AllArgsConstructor(staticName = "fastaWriter")
public final class FastaWriter<A extends CompoundSequence<? extends Compound>> {

    public static final int MAX_LINE_LENGTH = 70;

    public static FastaWriter<AminoAcidSequence> forAminoAcid() {
        return new FastaWriter<>();
    }

    public static FastaWriter<NucleobaseSequence> forNucleobase() {
        return new FastaWriter<>();
    }

    public void toPath(
        final Fasta<A> fasta, final Path path
    ) throws IOException {
        toWriter(fasta, Files.newBufferedWriter(path));
    }

    public void toOutput(
        final Fasta<A> fasta, final OutputStream outputStream
    ) throws IOException{
        toWriter(fasta, new OutputStreamWriter(
            outputStream, StandardCharsets.UTF_8
        ));
    }

    public void toWriter(
        final Fasta<A> fasta, final Writer w
    ) throws IOException {
        try (val writer = new BufferedWriter(w)) {
            for (final A sequence : fasta) {
                writeHeader(writer, sequence);
                writeSequence(writer, sequence);
            }
            writer.flush();
        }
    }

    private void writeHeader(
        final BufferedWriter writer, final A sequence
    ) throws IOException {
        val sha = sequence.getSHA1().asHexString();
        writer.append(">").append(sha).append(lineSeparator());
    }

    private void writeSequence(
        final BufferedWriter writer, final A sequence
    ) throws IOException {
        final char [ ] cs = sequence.asString().toCharArray();

        for (int i = 0; i < cs.length; ++i) {
            writer.append(cs[i]);
            if (i % MAX_LINE_LENGTH == 0)
                writer.append(lineSeparator());
        }

        writer.append(lineSeparator());
    }

}
