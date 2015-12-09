package es.uvigo.ei.sing.mahmi.funpep.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.AminoAcid;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaReader;

public final class FunpepToMahmi {

    // Disallow construction
    private FunpepToMahmi() { }

    public static AminoAcid asMahmi(final funpep.data.AminoAcid aa) {
        return AminoAcid.fromCode(aa.code()).orSome(AminoAcid.XAA);
    }

    public static Fasta<AminoAcidSequence> asMahmi(
        final funpep.data.Fasta<AminoAcid> fasta
    ) {
        try (final BufferedReader reader = new BufferedReader(new StringReader(fasta.toString()))) {

            return FastaReader.forAminoAcid().fromReader(reader);

        } catch (final IOException ioe) {
            throw new RuntimeException(
                "Could not convert funpep Fasta to MAHMI Fasta", ioe
            );
        }
    }

}
