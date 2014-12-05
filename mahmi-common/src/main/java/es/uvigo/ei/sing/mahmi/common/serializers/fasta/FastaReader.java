package es.uvigo.ei.sing.mahmi.common.serializers.fasta;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.ChemicalCompound;
import es.uvigo.ei.sing.mahmi.common.entities.fasta.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.ChemicalCompoundSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.DNASequence;

public interface FastaReader<A extends ChemicalCompoundSequence<? extends ChemicalCompound>> {

    public static FastaReader<AminoAcidSequence> forAminoAcid() {
        return new ProteinFastaReader();
    }

    public static FastaReader<DNASequence> forDNA() {
        return new GenomeFastaReader();
    }

    public Fasta<A> fromInput(final InputStream inputStream) throws IOException;

    public default Fasta<A> fromFile(final File file) throws IOException {
        return fromInput(new FileInputStream(file));
    }

    public default Fasta<A> fromPath(final Path path) throws IOException {
        return fromFile(path.toFile());
    }

}
