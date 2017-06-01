package es.uvigo.ei.sing.mahmi.psort;

import java.util.EnumSet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.val;

import static java.util.stream.Collectors.joining;

/**
 * {@linkplain PSortFilterType} is an enum that represents the PSortB
 * subcellular locations
 * 
 * @author Aitor Blanco-Miguez
 *
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PSortFilterType {

	Cytoplasmic("Cytoplasmic"), 
	CytoplasmicMembrane("CytoPlasmic"), 
	Extracellular("Extracellular"), 
	Unknown("Unknown");

	private final String regex;

	/**
	 * Constructs a subcellular location filter expression for PSortB
	 * 
	 * @param filter The subcellular locations
	 * @return The compiled subcellular location expression
	 */
	static String compile(final EnumSet<PSortFilterType> filter) {
		val or = filter.stream().map(p -> p.getRegex()).collect(joining("|"));
		return ".*(" + or + ").*";
	}

	/**
	 * Getter function of {@code regex} variable
	 * 
	 * @return The subcellular location
	 */
	String getRegex() {
		return regex;
	}

	/**
	 * Gets a single subcellular location {@linkplain EnumSet}
	 * 
	 * @return The single subcelluar location {@linkplain EnumSet}
	 */
	public EnumSet<PSortFilterType> single() {
		return EnumSet.of(this);
	}

	/**
	 * Gets a multiple subcellular location {@linkplain EnumSet}
	 * 
	 * @param that The second subcellular location
	 * @return The multiple subcellular location {@linkplain EnumSet}
	 */
	public EnumSet<PSortFilterType> or(final PSortFilterType that) {
		return EnumSet.of(this, that);
	}

}
