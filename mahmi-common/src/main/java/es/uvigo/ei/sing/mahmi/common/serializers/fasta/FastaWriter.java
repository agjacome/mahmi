package es.uvigo.ei.sing.mahmi.common.serializers.fasta;

import static java.lang.System.lineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.AllArgsConstructor;
import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.compounds.Compound;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.CompoundSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleobaseSequence;

@AllArgsConstructor(staticName = "fastaWriter")
public final class FastaWriter<A extends CompoundSequence<? extends Compound>> {

    private static final int LINE_WIDTH = 80;

    public static FastaWriter<AminoAcidSequence> forAminoAcid() {
        return new FastaWriter<>();
    }

    public static FastaWriter<NucleobaseSequence> forNucleobase() {
        return new FastaWriter<>();
    }

    public void toFile(final Fasta<A> fasta, final File file) throws IOException {
        toOutput(fasta, new FileOutputStream(file));
    }

    public void toPath(final Fasta<A> fasta, final Path path) throws IOException {
        toFile(fasta, path.toFile());
    }

    public void toOutput(
        final Fasta<A> fasta, final OutputStream outputStream
    ) throws IOException{
        try (val writer = new OutputStreamWriter(outputStream)) {

            for (final A sequence : fasta.getSequences()) {
                writeHeader(writer, sequence);
                writeSequence(writer, sequence);
            }

            writer.flush();
        }
    }

    private void writeHeader(
        final OutputStreamWriter writer, final A sequence
    ) throws IOException {
        val sha = sequence.getSHA1().asHexString();
        writer.append(">").append(sha).append(lineSeparator());
    }

    private void writeSequence(
        final OutputStreamWriter writer, final A sequence
    ) throws IOException {
        val counter = new AtomicInteger();

        for (val residue : sequence.getResidues()) {
            writer.append(residue.getCode());
            if (counter.incrementAndGet() % LINE_WIDTH == 0)
                writer.append(lineSeparator());
        }

        writer.append(lineSeparator());
    }

}
