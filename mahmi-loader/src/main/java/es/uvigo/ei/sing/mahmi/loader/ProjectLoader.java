package es.uvigo.ei.sing.mahmi.loader;

import java.nio.file.Path;

import fj.P2;
import fj.data.Stream;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleobaseSequence;

/**
 * {@linkplain ProjectLoader} is an interface for MAHMI project loaders
 * 
 * @author Alberto Gutierrez-Jacome
 *
 */
public interface ProjectLoader {

	/**
	 * Loads a MAHMI project
	 * 
	 * @param projectPath
	 *            The folder containing the project
	 * @return The pair of metagenomes and proteomes of the project
	 * @throws LoaderException
	 */
	public Stream<P2<Fasta<NucleobaseSequence>, Fasta<AminoAcidSequence>>> loadProject(
        final Path projectPath
    ) throws LoaderException;

}
