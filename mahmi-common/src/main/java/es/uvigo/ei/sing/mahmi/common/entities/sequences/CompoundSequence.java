package es.uvigo.ei.sing.mahmi.common.entities.sequences;

import java.util.Objects;

import fj.Equal;
import fj.Hash;
import fj.Ord;
import fj.P1;
import fj.data.Seq;
import lombok.experimental.ExtensionMethod;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.Compound;
import es.uvigo.ei.sing.mahmi.common.utils.SHA1;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.SeqExtensionMethods;

import static fj.P.lazy;

/**
 * {@linkplain CompoundSequence} is an abstract class that represents a sequence
 * of {@link Compound}s
 * 
 * @author Alberto Gutierrez-Jacome
 *
 * @param <A>
 *            The specific compound
 * 
 * @see Compound
 */
@ExtensionMethod({ SeqExtensionMethods.class, Objects.class })
public abstract class CompoundSequence<A extends Compound> {

	public static final Hash<CompoundSequence<? extends Compound>> hash = SHA1.hash
			.contramap(CompoundSequence::getSHA1);
	public static final Equal<CompoundSequence<? extends Compound>> equal = SHA1.equal
			.contramap(CompoundSequence::getSHA1);
	public static final Ord<CompoundSequence<? extends Compound>> ord = SHA1.ord
			.contramap(CompoundSequence::getSHA1);

	/**
	 * The compound sequence as String
	 */
	private final P1<String> asString = lazy(
			() -> getResidues().map(Compound::getCode).buildString()).memo();

	/**
	 * The compound sequence as SHA1
	 */
	private final P1<SHA1> sha = lazy(() -> SHA1.of(asString())).memo();

	public boolean isEmpty() {
		return getResidues().isEmpty();
	}

	/**
	 * Gets the compound sequence as SHA1
	 * 
	 * @return The compound sequence as SHA1
	 */
	public SHA1 getSHA1() {
		return sha._1();
	}

	/**
	 * Gets the compound sequence as String
	 * 
	 * @return The compound sequence as String
	 */
	public String asString() {
		return asString._1();
	}

	/**
	 * Gets a {@code Seq) of the specific compound
	 * 
	 * @return A {@code Seq) of the specific compound
	 */
	public abstract Seq<A> getResidues();

}
