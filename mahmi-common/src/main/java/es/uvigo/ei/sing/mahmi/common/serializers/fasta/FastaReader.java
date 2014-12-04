package es.uvigo.ei.sing.mahmi.common.serializers.fasta;

import static fj.data.Validation.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.ChemicalCompound;
import es.uvigo.ei.sing.mahmi.common.entities.fasta.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.ChemicalCompoundSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.DNASequence;
import fj.data.Validation;

public interface FastaReader<A extends ChemicalCompoundSequence<? extends ChemicalCompound>> {

    public static FastaReader<AminoAcidSequence> forAminoAcid() {
        return new ProteinFastaReader();
    }

    public static FastaReader<DNASequence> forDNA() {
        return new GenomeFastaReader();
    }

    public Validation<IOException, Fasta<A>> fromInput(final InputStream inputStream);

    public default Validation<IOException, Fasta<A>> fromFile(final File file) {
        try {
            return fromInput(new FileInputStream(file));
        } catch (final FileNotFoundException fnfe) {
            return fail(new IOException(fnfe));
        }
    }

    public default Validation<IOException, Fasta<A>> fromPath(final Path path) {
        return fromFile(path.toFile());
    }

}
