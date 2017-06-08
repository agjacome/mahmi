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
 * {@linkplain ReferencePeptide} is a class that represents a reference peptide
 * 
 * @author Alberto Gutierrez-Jacome
 * 
 * @see Entity
 * @see Identifier
 * @see AminoAcidSequence
 */
@Getter
@Wither
@AllArgsConstructor(staticName = "referencePeptide")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class ReferencePeptide implements Entity<ReferencePeptide> {

	public static final Hash<ReferencePeptide> hash = CompoundSequence.hash
			.contramap(ReferencePeptide::getSequence);
	public static final Equal<ReferencePeptide> equal = CompoundSequence.equal
			.contramap(ReferencePeptide::getSequence);
	public static final Ord<ReferencePeptide> ord = CompoundSequence.ord
			.contramap(ReferencePeptide::getSequence);

	/**
	 * The reference peptide identifier
	 */
	private final Identifier id;

	/**
	 * The reference peptide amino acid sequence
	 */
	private final AminoAcidSequence sequence;

	/**
	 * The reference peptide bioactivity
	 */
	private final String bioactivity;

	/**
	 * {@linkplain ReferencePeptide} default constructor
	 */
	@VisibleForJAXB
	public ReferencePeptide() {
		this(new Identifier(), AminoAcidSequence.empty(), "");
	}

	/**
	 * Constructs a new instance of {@linkplain ReferencePeptide} without {@link Identifier}
	 * 
	 * @param sequence
	 *            The reference peptide amino acid sequence
	 * @param bioactivity
	 *            The reference peptide bioactivity
	 * @return A new instance of {@linkplain ReferencePeptide}
	 */
	public static ReferencePeptide referencePeptide(final AminoAcidSequence sequence,
													final String bioactivity) {
		return referencePeptide(Identifier.empty(), sequence, bioactivity);
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
