package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Wither;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleobaseSequence;
import es.uvigo.ei.sing.mahmi.common.serializers.jaxb.NucleobaseFastaAdapter;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

/**
 * {@linkplain MetaGenome} is a class that represents a metagenome
 * 
 * @author Alberto Gutierrez-Jacome
 * 
 * @see Entity
 * @see Identifier
 * @see Fasta
 * @see Project
 *
 */
@Getter
@Wither
@AllArgsConstructor(staticName = "metagenome")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class MetaGenome implements Entity<MetaGenome> {

	/**
	 * The metagenome identifier
	 */
	private final Identifier id;

	/**
	 * The project of the metagenome
	 */
	private final Project project;

	/**
	 * The metagenome FASTA
	 */
	@XmlJavaTypeAdapter(NucleobaseFastaAdapter.class)
	private final Fasta<NucleobaseSequence> fasta;

	/**
	 * {@linkplain MetaGenome} default constructor
	 */
	@VisibleForJAXB
	public MetaGenome() {
		this(new Identifier(), new Project(), Fasta.empty());
	}

	/**
	 * Constructs a new instance of {@linkplain MetaGenome} without {@link Identifier}
	 * 
	 * @param project
	 *            The project of the metagenome
	 * @param fasta
	 *            The metagenome FASTA
	 * @return A new instance of {@linkplain MetaGenome}
	 */
	public static MetaGenome metagenome(final Project project,
										final Fasta<NucleobaseSequence> fasta) {
		return metagenome(Identifier.empty(), project, fasta);
	}

}
