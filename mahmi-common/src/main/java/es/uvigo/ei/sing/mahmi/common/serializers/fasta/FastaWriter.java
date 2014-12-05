package es.uvigo.ei.sing.mahmi.common.serializers.fasta;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.ChemicalCompound;
import es.uvigo.ei.sing.mahmi.common.entities.fasta.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.ChemicalCompoundSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.DNASequence;

public interface FastaWriter<A extends ChemicalCompoundSequence<? extends ChemicalCompound>> {

    public static FastaWriter<AminoAcidSequence> forAminoAcid() {
        return new ProteinFastaWriter();
    }

    public static FastaWriter<DNASequence> forDNA() {
        return new GenomeFastaWriter();
    }

    public void toOutput(final Fasta<A> fasta, final OutputStream outputStream) throws IOException;

    public default void toFile(final Fasta<A> fasta, final File file) throws IOException {
        toOutput(fasta, new FileOutputStream(file));
    }

    public default void toPath(final Fasta<A> fasta, final Path path) throws IOException {
        toFile(fasta, path.toFile());
    }

}
