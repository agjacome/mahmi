package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Wither;

import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

/**
 * {@linkplain ProteinInformation} is a class that represents a Uniprot protein information
 * 
 * @author Aitor Blanco-Miguez
 * 
 * @see Entity
 * @see Identifier
 * @see Protein
 *
 */
@Getter
@Wither
@AllArgsConstructor(staticName = "proteinInformation")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class ProteinInformation implements Entity<ProteinInformation> {
	/**
	 * The Uniprot information protein
	 */
	@XmlTransient
	private Identifier id;

	/**
	 * The protein {@link Entity} of the information
	 */
	@XmlTransient
	private Protein protein;

	/**
	 * The Uniprot id
	 */
	private String uniprotId;

	/**
	 * The Uniprot organism
	 */
	private String uniprotOrganism;

	/**
	 * The Uniprot protein
	 */
	private String uniprotProtein;

	/**
	 * The Uniprot gene
	 */
	private String uniprotGene;

	/**
	 * {@linkplain ProteinInformation} default constructor
	 */
	@VisibleForJAXB
	public ProteinInformation() {
	}

	/**
	 * Constructs a new instance of {@linkplain ProteinInformation} without {@link Identifier}
	 * 
	 * @param protein
	 *            The protein {@link Entity} of the information
	 * @param uniprotId
	 *            The Uniprot id
	 * @param uniprotOrganism
	 *            The Uniprot organism
	 * @param uniprotProtein
	 *            The Uniprot protein
	 * @param uniprotGene
	 *            The Uniprot gene
	 * @return A new instance of {@linkplain ProteinInformation}
	 */
	public static ProteinInformation proteinInformation(final Protein protein,
														final String uniprotId,
														final String uniprotOrganism,
														final String uniprotProtein,
														final String uniprotGene) {
		return proteinInformation(Identifier.empty(), protein, uniprotId, uniprotOrganism,
				uniprotProtein, uniprotGene);
	}

}
