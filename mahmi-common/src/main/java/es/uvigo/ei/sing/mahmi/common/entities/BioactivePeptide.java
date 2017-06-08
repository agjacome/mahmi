package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.CompoundSequence;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.SHA1;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;
import fj.Equal;
import fj.Hash;
import fj.Ord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Wither;

/**
 * {@linkplain BioactivePeptide} is a class that represents a potential bioactive peptide
 * 
 * @author Alberto Gutierrez-Jacome
 * 
 * @see Entity
 * @see Identifier
 * @see AminoAcidSequence
 * @see ReferencePeptide
 *
 */
@Getter
@Wither
@AllArgsConstructor(staticName = "bioactivePeptide")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class BioactivePeptide implements Entity<BioactivePeptide> {

	public static final Hash<BioactivePeptide> hash = CompoundSequence.hash
			.contramap(BioactivePeptide::getSequence);
	public static final Equal<BioactivePeptide> equal = CompoundSequence.equal
			.contramap(BioactivePeptide::getSequence);
	public static final Ord<BioactivePeptide> ord = CompoundSequence.ord
			.contramap(BioactivePeptide::getSequence);

	/**
	 * The bioactive peptide identifier
	 */
	private final Identifier id;

	/**
	 * The bioactive peptide amino acid sequence
	 */
	private final AminoAcidSequence sequence;

	/**
	 * The percentage of similarity between {@code this} and the reference peptide
	 */
	private final double similarity;

	/**
	 * The reference peptide
	 */
	private final ReferencePeptide reference;

	/**
	 * {@linkplain BioactivePeptide} default constructor
	 */
	@VisibleForJAXB
	public BioactivePeptide() {
		this(new Identifier(), AminoAcidSequence.empty(), 0.0, new ReferencePeptide());
	}

	/**
	 * Constructs a new instance of {@linkplain BioactivePeptide} without {@link Identifier}
	 * 
	 * @param sequence
	 *            The amino acid sequence of the peptide
	 * @param similarity
	 *            The percentage of similitude with the reference peptide
	 * @param reference
	 *            The reference peptide {@linkplain BioactivePeptide}
	 * @return A new instance of {@linkplain BioactivePeptide}
	 */
	public static BioactivePeptide bioactivePeptide(final AminoAcidSequence sequence,
													final double similarity,
													final ReferencePeptide reference) {
		return bioactivePeptide(Identifier.empty(), sequence, similarity, reference);
	}

	/**
	 * Gets the SHA1 of the sequence
	 * 
	 * @return The SHA1 of the {@link #sequence}
	 */
	public SHA1 getSHA1() {
		return sequence.getSHA1();
	}

}
