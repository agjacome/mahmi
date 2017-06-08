package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Wither;

import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

/**
 * {@linkplain MetaGenomeProteins} is a class that represents a
 * metagenome-protein relation
 * 
 * @author Alberto Gutierrez-Jacome
 * 
 * @see Entity
 * @see Identifier
 * @see Protein
 * @see MetaGenome
 *
 */
@Getter
@Wither
@AllArgsConstructor(staticName = "metagenomeProteins")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class MetaGenomeProteins implements Entity<MetaGenomeProteins> {

	/**
	 * The relation id
	 */
	private final Identifier id;

	/**
	 * The relation metagenome
	 */
	private final MetaGenome metagenome;

	/**
	 * The relation protein
	 */
	private final Protein protein;

	/**
	 * The relation occurrence
	 */
	private final long counter;

	/**
	 * {@linkplain MetaGenomeProteins} default constructor
	 */
	@VisibleForJAXB
	public MetaGenomeProteins() {
		this(Identifier.empty(), new MetaGenome(), new Protein(), 0L);
	}

	/**
	 * Constructs a new instance of {@linkplain MetaGenomeProteins} by a
	 * metagenome and a protein entity
	 * 
	 * @param metagenome
	 *            The relation metagenome
	 * @param protein
	 *            The relation protein
	 * @return A new instance of {@linkplain MetaGenomeProteins}
	 */
	public static MetaGenomeProteins metagenomeProteins(final MetaGenome metagenome,
														final Protein protein) {
		return metagenomeProteins(Identifier.empty(), metagenome, protein, 0L);
	}

	/**
	 * Constructs a new instance of {@linkplain MetaGenomeProteins} by a
	 * identifier, a metagenome entity and a protein entity
	 * 
	 * @param id
	 *            The relation identifier
	 * @param metagenome
	 *            The relation metagenome
	 * @param protein
	 *            The relation protein
	 * @return A new instance of {@linkplain MetaGenomeProteins}
	 */
	public static MetaGenomeProteins metagenomeProteins(final Identifier id,
														final MetaGenome metagenome,
														final Protein protein) {
		return metagenomeProteins(id, metagenome, protein, 0L);
	}

	/**
	 * Constructs a new instance of {@linkplain MetaGenomeProteins} by a
	 * metagenome and protein entities and a occurrences counter
	 * 
	 * @param metagenome
	 *            The relation metagenome
	 * @param protein
	 *            The relation protein
	 * @param counter
	 *            The relation occurrence
	 * @return A new instance of {@linkplain MetaGenomeProteins}
	 */
	public static MetaGenomeProteins metagenomeProteins(final MetaGenome metagenome,
														final Protein protein,
														final long counter) {
		return metagenomeProteins(Identifier.empty(), metagenome, protein, counter);
	}

}
