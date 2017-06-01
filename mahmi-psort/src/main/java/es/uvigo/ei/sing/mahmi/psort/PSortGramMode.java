package es.uvigo.ei.sing.mahmi.psort;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * {@linkplain PSortGramMode} is an enum that represents the PSortB gram mode
 * 
 * @author Aitor Blanco-Miguez
 *
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PSortGramMode {

	Positive("--positive"), 
	Negative("--negative");

	private final String modeFlag;

}
