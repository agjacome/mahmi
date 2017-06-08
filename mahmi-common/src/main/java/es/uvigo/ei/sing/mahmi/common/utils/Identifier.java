package es.uvigo.ei.sing.mahmi.common.utils;

import fj.Equal;
import fj.Hash;
import fj.Ord;
import fj.data.Natural;
import fj.data.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

import static fj.Equal.longEqual;
import static fj.Equal.optionEqual;
import static fj.Hash.longHash;
import static fj.Hash.optionHash;
import static fj.Ord.longOrd;
import static fj.Ord.optionOrd;
import static fj.data.Natural.natural;
import static fj.data.Option.none;

/**
 * {@linkplain Identifier} is a class that represents the identifier of a
 * database entity
 * 
 * @author Alberto Gutierrez-Jacome
 *
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Identifier {

	public static final Hash<Identifier> hash   = optionHash(longHash.contramap(Natural::longValue))
			.contramap(Identifier::getValue);

	public static final Equal<Identifier> equal = optionEqual(
			longEqual.contramap(Natural::longValue)).contramap(Identifier::getValue);

	public static final Ord<Identifier> ord     = optionOrd(longOrd.contramap(Natural::longValue))
			.contramap(Identifier::getValue);

	/**
	 * The value of the identifier
	 */
	private final Option<Natural> value;

	/**
	 * {@linkplain Identifier} default empty constructor
	 */
	@VisibleForJAXB
	public Identifier() {
		this(none());
	}

	/**
	 * Constructs a empty instance of {@linkplain Identifier}
	 * 
	 * @return A empty instance of {@linkplain Identifier}
	 */
	public static Identifier empty() {
		return new Identifier(none());
	}

	/**
	 * Constructs a new instance of {@linkplain Identifier} by a {@code long}
	 * value
	 * 
	 * @param value
	 *            The value of the identifier
	 * @return A new instance of {@linkplain Identifier}
	 */
	public static Identifier of(final long value) {
		return new Identifier(natural(value));
	}

	/**
	 * Checks if a {@linkplain Identifier} is empty
	 * 
	 * @return Whether a {@linkplain Identifier} is empty
	 */
	public boolean isEmpty() {
		return value.isNone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return value.option("NULL", n -> "" + n.intValue());
	}

}
