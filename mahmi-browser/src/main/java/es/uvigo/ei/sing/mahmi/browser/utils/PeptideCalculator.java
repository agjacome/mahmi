package es.uvigo.ei.sing.mahmi.browser.utils;

import es.uvigo.ei.sing.mahmi.browser.Browser;

/**
 * {@linkplain Browser} is a class that provides isoelectric point and molecular
 * weight calculator using ProMoST algorithm
 * 
 * @author Aitor Blanco-Miguez
 *
 */
public class PeptideCalculator {

	final static char[] aminoac = { 'A', 'R', 'N', 'D', 'C', 'E', 'Q', 'G', 'H', 'I', 'L', 'K', 'M',
			'F', 'P', 'S', 'T', 'W', 'Y', 'V' };

	final static Double[] cterm = { 2.35, 2.17, 2.02, 2.09, 1.71, 2.19, 2.17, 2.34, 1.82, 2.36,
			2.36, 2.18, 2.28, 1.83, 1.99, 2.21, 2.63, 2.38, 2.2, 2.32 };

	final static Double[] nterm = { 7.5, 6.76, 7.22, 7.7, 8.12, 7.19, 6.73, 7.5, 7.18, 7.48, 7.46,
			6.67, 6.98, 6.96, 8.36, 6.86, 7.02, 7.11, 6.83, 7.44 };

	final static Double[] sidegr = { 0.0, -12.5, 0.0, 4.07, 8.28, 4.45, 0.0, 0.0, -6.08, 0.0, 0.0,
			-9.8, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 9.84, 0.0 };

	final static Double[] aminmass = { 71.0788, 156.1875, 114.1038, 115.0886, 103.1388, 129.1155,
			128.1307, 57.0519, 137.1411, 113.1594, 113.1594, 128.1741, 131.1926, 147.1766, 97.1167,
			87.0782, 101.1051, 186.2132, 163.176, 99.1326 };

	/**
	 * Calculates the isoelectric point of an amino acid sequence
	 * 
	 * @param sequence
	 *            An amino acid sequence
	 * @return The isoelectric point of the sequence
	 */
	public double calculatePI(final String sequence) {
		double pI = findPi(doFormula(0.0, sequence), 0.5, 0.0, sequence) - 0.5;
		pI = findPi(doFormula(pI, sequence), .01, pI, sequence) - .01;
		return Math.round(pI * Math.pow(10, 2)) / Math.pow(10, 2);
	}

	/**
	 * Calculates the molecular weight of an amino acid sequence
	 * 
	 * @param sequence
	 *            An amino acid sequence
	 * @return The molecular weight of the sequence
	 */
	public Double calculateMW(final String sequence) {
		char aminoAcid;
		Double mW = 18.01524;
		for (int i = 0; i < sequence.length(); i++) {
			aminoAcid = sequence.charAt(i);
			if (aminoAcid != ' ') {
				mW += aminmass[findElement(aminoAcid)];
			}
		}
		return Math.round(mW * Math.pow(10, 2)) / Math.pow(10, 2);
	}

	/**
	 * Private formula for isoelectric point calculation
	 * 
	 * @param pI The in calculation isoelectric point
	 * @param sequence The amino acid sequence
	 * @return The isoelectric point formula result
	 */
	private double doFormula(final double pI, final String sequence) {
		double result = 1 / (1 + (Math.pow(10, (pI - nterm[findElement(sequence.charAt(0))]))));
		result = result - 1 / (1 + (Math.pow(10,
				(cterm[findElement(sequence.charAt(sequence.length() - 1))] - pI))));
		for (int i = 0; i < sequence.length(); i++) {
			if (sequence.charAt(i) != ' ') {
				final int position = findElement(sequence.charAt(i));
				if (sidegr[position] != 0) {
					if (sidegr[position].toString().charAt(0) == '-') {
						result = result + 1 / (1 + (Math.pow(10, (pI + sidegr[position]))));
					} else {
						result = result - 1 / (1 + (Math.pow(10, (sidegr[position] - pI))));
					}
				}
			}
		}
		return result;
	}

	/**
	 * Calculates the isoelectric point of an amino acid sequence
	 * 
	 * @param formulaResult the result of the pI formula
	 * @param n The n factor
	 * @param pI The in calculation isoelectric point
	 * @param sequence The amino acid secuence
	 * @return The calculated isoelectric point
	 */
	private double findPi(double formulaResult, double n, double pI, final String sequence) {
		if (formulaResult >= 0.0) {
			pI = findPi(doFormula(pI + n, sequence), n, pI + n, sequence);
		}
		return pI;
	}

	/**
	 * Gets the position in the {@link #aminoac} array of an amino acid
	 * 
	 * @param aminoAcid The amino acid to find
	 * @return The position in the array of the amino acid
	 */
	private int findElement(final char aminoAcid) {
		for (int j = 0; j < 20; j++) {
			if (aminoac[j] == aminoAcid) {
				return j;
			}
		}
		return 0;
	}
}
