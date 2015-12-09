package es.uvigo.ei.sing.mahmi.funpep.util;

import java.io.IOException;
import java.io.StringWriter;

import lombok.val;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaWriter;

import static funpep.data.AminoAcid.AminoAcidParser;

import static es.uvigo.ei.sing.mahmi.funpep.util.JavaToScala.asScala;

public final class MahmiToFunpep {

    // Disallow construction
    private MahmiToFunpep() { }

    public static funpep.data.Fasta<funpep.data.AminoAcid> asFunpep(
        final Fasta<AminoAcidSequence> faa
    ) {
        try (final StringWriter writer = new StringWriter()) {

            FastaWriter.forAminoAcid().toWriter(faa, writer);

            val opt = funpep.data.FastaParser$.MODULE$.fromString(
                writer.toString(), AminoAcidParser(), asScala(aa -> aa)
            ).toOption();

            if (opt.isEmpty()) throw new IOException(); else return opt.get();

        } catch (final IOException ioe) {
            throw new RuntimeException(
                "Could not convert MAHMI Fasta to funpep Fasta", ioe
            );
        }
    }

}
