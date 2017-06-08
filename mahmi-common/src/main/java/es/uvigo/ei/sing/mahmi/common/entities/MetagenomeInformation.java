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
 * {@linkplain MetagenomeInformation} is a class that represents the metagenome
 * additional information of a {@link Protein}
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
@AllArgsConstructor(staticName = "metagenomeInformation")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class MetagenomeInformation implements Entity<MetagenomeInformation> {
	/**
	 * The identifier of the information
	 */
	@XmlTransient
	private Identifier id;

	/**
	 * The protein of the information
	 */
	@XmlTransient
	private Protein protein;

	/**
	 * The length of the gene
	 */
	private String geneLength;

	/**
	 * The completeness of the gene
	 */
	private String geneCompleteness;

	/**
	 * The origin of the gene
	 */
	private String geneOrigin;

	/**
	 * The species Phylum annotation
	 */
	private String speciesAnnotationPhylum;

	/**
	 * The species Genus annotation
	 */
	private String speciesAnnotationGenus;

	/**
	 * The KEGG phylum annotation
	 */
	private String keggAnnotationPhylum;

	/**
	 * The sample occurence frequency
	 */
	private String sampleOcurrenceFrequency;

	/**
	 * The individual occurrence frequency
	 */
	private String individualOcurrenceFrequency;

	/**
	 * The KEGG functional category
	 */
	private String keggFunctionalCategory;

	/**
	 * {@linkplain MetagenomeInformation} default constructor
	 */
	@VisibleForJAXB
	public MetagenomeInformation() {
	}

	/**
	 * Constructs a new instance of {@linkplain MetagenomeInformation} without {@link Identifier}
	 * 
	 * @param protein
	 *            The protein of the information
	 * @param geneLength
	 *            The gene length
	 * @param geneCompleteness
	 *            The gene completeness
	 * @param geneOrigin
	 *            The gene origin
	 * @param speciesAnnotationPhylum
	 *            The species Phylum annotation
	 * @param speciesAnnotationGenus
	 *            The species Genus annotation
	 * @param keggAnnotationPhylum
	 *            The KEGG Phylum annotation
	 * @param sampleOcurrenceFrequency
	 *            The sample occurrence frequency
	 * @param individualOcurrenceFrequency
	 *            The individual occurrence frequency
	 * @param keggFunctionalCategory
	 *            The KEGG functional category
	 * @return A new instance of {@linkplain MetagenomeInformation}
	 */
	public static MetagenomeInformation metagenomeInformation(	final Protein protein,
																final String geneLength,
																final String geneCompleteness,
																final String geneOrigin,
																final String speciesAnnotationPhylum,
																final String speciesAnnotationGenus,
																final String keggAnnotationPhylum,
																final String sampleOcurrenceFrequency,
																final String individualOcurrenceFrequency,
																final String keggFunctionalCategory) {
		return metagenomeInformation(Identifier.empty(), protein, geneLength, geneCompleteness,
				geneOrigin, speciesAnnotationPhylum, speciesAnnotationGenus, keggAnnotationPhylum,
				sampleOcurrenceFrequency, individualOcurrenceFrequency, keggFunctionalCategory);
	}

}
