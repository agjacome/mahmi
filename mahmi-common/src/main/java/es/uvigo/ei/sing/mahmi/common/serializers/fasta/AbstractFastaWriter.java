package es.uvigo.ei.sing.mahmi.common.serializers.fasta;

import static fj.Unit.unit;
import static fj.data.Validation.fail;
import static fj.data.Validation.success;
import static java.lang.Math.min;
import static java.lang.System.lineSeparator;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map.Entry;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.compounds.ChemicalCompound;
import es.uvigo.ei.sing.mahmi.common.entities.fasta.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.ChemicalCompoundSequence;
import es.uvigo.ei.sing.mahmi.common.utils.SHA1;
import fj.Unit;
import fj.data.Validation;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
abstract class AbstractFastaWriter<A extends ChemicalCompoundSequence<? extends ChemicalCompound>> implements FastaWriter<A> {

    @Override
    public Validation<IOException, Unit> toOutput(final Fasta<A> fasta, final OutputStream outputStream) {
        // FIXME: quite ugly code, try to clean up a bit.
        try (final Writer writer = new OutputStreamWriter(outputStream)) {

            for (final Entry<A, Integer> entry : fasta.getSequences().entrySet()) {
                val sequence = entry.getKey();
                val counter  = entry.getValue();

                val idLine  = "> " + getSHA1FromSequence(sequence).asHexString();
                val seqLine = getStringFromSequence(sequence);

                for (int i = 0; i < counter; ++i) {
                    writer.append(idLine).append(String.format(" | %d", i + 1))
                          .append(lineSeparator());

                    for (int j = 0; j < seqLine.length(); j += 40) {
                        writer.append(seqLine.substring(i, min(i + 40, seqLine.length())))
                              .append(lineSeparator());
                    }
                }

                writer.flush();
            }

            return success(unit());

        } catch (final IOException ioe) {
            return fail(ioe);
        }
    }

    private SHA1 getSHA1FromSequence(final A sequence) {
        return SHA1.of(getStringFromSequence(sequence));
    }

    protected abstract String getStringFromSequence(final A sequence);

}
