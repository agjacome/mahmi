package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import fj.Equal;
import fj.Hash;
import fj.Ord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Wither;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.CompoundSequence;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.SHA1;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

/**
 * {@linkplain Protein} is a class that represents a protein
 * 
 * @author Alberto Gutierrez-Jacome
 * 
 * @see Entity
 * @see Identifier
 * @see AminoAcidSequence
 */
@Getter
@Wither
@AllArgsConstructor(staticName = "protein")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class Protein implements Entity<Protein> {

	public static final Hash<Protein> hash = CompoundSequence.hash.contramap(Protein::getSequence);
	public static final Equal<Protein> equal = CompoundSequence.equal
			.contramap(Protein::getSequence);
	public static final Ord<Protein> ord = CompoundSequence.ord.contramap(Protein::getSequence);

	/**
	 * The protein identifier
	 */
	private final Identifier id;

	/**
	 * The protein amino acid sequence
	 */
	private final AminoAcidSequence sequence;

	/**
	 * The protein name
	 */
	private final String name;

	/**
	 * {@linkplain Protein} default constructor
	 */
	@VisibleForJAXB
	public Protein() {
		this(new Identifier(), AminoAcidSequence.empty(), "");
	}

	/**
	 * Constructs a new instance of {@linkplain Protein} without {@link Identifier}
	 * 
	 * @param sequence
	 *            The protein amino acid sequence
	 * @return A new instance of {@linkplain Protein}
	 */
	public static Protein protein(final AminoAcidSequence sequence) {
		return protein(Identifier.empty(), sequence, "");
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
