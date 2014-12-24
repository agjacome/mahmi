package es.uvigo.ei.sing.mahmi.common.serializers.fasta;

import static java.lang.Math.min;
import static java.lang.String.format;
import static java.lang.System.lineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.util.Map.Entry;

import lombok.AllArgsConstructor;
import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.compounds.ChemicalCompound;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.ChemicalCompoundSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;

@AllArgsConstructor(staticName = "fastaWriter")
public final class FastaWriter<A extends ChemicalCompoundSequence<? extends ChemicalCompound>> {

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

            for (final Entry<A, Long> entry : fasta.getSequences().entrySet()) {
                val sequence = entry.getKey();
                val counter  = entry.getValue();

                val sha = "> " + sequence.toSHA1().asHexString();
                val seq = sequence.toString();

                for (int i = 0; i < counter; ++i) {
                    writer.append(sha).append(format(" | %d", i + 1))
                          .append(lineSeparator());

                    for (int j = 0; j < seq.length(); j += 60) {
                        val line = seq.substring(i, min(i + 60, seq.length()));
                        writer.append(line)
                              .append(lineSeparator());
                    }
                }
            }

            writer.flush();
        }
    }

}
