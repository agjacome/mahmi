package es.uvigo.ei.sing.mahmi.common.serializers.fasta;

import static fj.data.Validation.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.ChemicalCompound;
import es.uvigo.ei.sing.mahmi.common.entities.fasta.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.ChemicalCompoundSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.DNASequence;
import fj.Unit;
import fj.data.Validation;

public interface FastaWriter<A extends ChemicalCompoundSequence<? extends ChemicalCompound>> {

    public static FastaWriter<AminoAcidSequence> forAminoAcid() {
        return new ProteinFastaWriter();
    }

    public static FastaWriter<DNASequence> forDNA() {
        return new GenomeFastaWriter();
    }

    public Validation<IOException, Unit> toOutput(final Fasta<A> fasta, final OutputStream outputStream);

    public default Validation<IOException, Unit> toFile(final Fasta<A> fasta, final File file) {
        try {
            return toOutput(fasta, new FileOutputStream(file));
        } catch (final FileNotFoundException fnfe) {
            return fail(new IOException(fnfe));
        }
    }

    public default Validation<IOException, Unit> toPath(final Fasta<A> fasta, final Path path) {
        return toFile(fasta, path.toFile());
    }

}
