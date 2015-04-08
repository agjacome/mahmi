package es.uvigo.ei.sing.mahmi.calculator;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

//Source http://www.fmi.ch/paf/tools/pepeval.html
@Slf4j
public class PeptideCalculator {
	
	final char[] aminoac    = { 'A', 'R', 'N', 'D', 'C', 'E', 'Q', 'G', 'H', 'I', 'L', 'K', 'M',
	                            'F', 'P', 'S', 'T', 'W', 'Y', 'V' };

	final Double[] cterm    = { 2.35, 2.17, 2.02, 2.09, 1.71, 2.19, 2.17, 2.34, 1.82, 2.36, 2.36, 
			                    2.18, 2.28, 1.83, 1.99, 2.21, 2.63, 2.38, 2.2, 2.32 };
	
	final Double[] nterm    = { 7.5, 6.76, 7.22, 7.7, 8.12, 7.19, 6.73, 7.5, 7.18, 7.48, 7.46, 
			                    6.67, 6.98, 6.96, 8.36, 6.86, 7.02, 7.11, 6.83, 7.44 };
    
	final Double[] sidegr   = { 0.0, -12.5, 0.0, 4.07, 8.28, 4.45, 0.0, 0.0, -6.08, 0.0, 0.0, 
			                    -9.8, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 9.84, 0.0 };

	final Double[] aminmass = { 71.0788, 156.1875, 114.1038, 115.0886, 103.1388, 129.1155,
	                            128.1307, 57.0519, 137.1411, 113.1594, 113.1594, 128.1741,
	                            131.1926, 147.1766, 97.1167, 87.0782, 101.1051, 186.2132,
	                            163.176, 99.1326 };
	
	public double calculatePI (String sequence) {
		String formula = "";		
		int position=findElement(sequence.charAt(0));		
		formula += "1/(1+(Math.pow(10,(pI-" + nterm[position] + "))))";
		position=findElement(sequence.charAt(sequence.length() - 1));
		formula += "-1/(1+(Math.pow(10,(" + cterm[position] + "-pI))))";
		
		for (int i = 0; i < sequence.length(); i++){
			if(sequence.charAt(i) != ' '){
				position=findElement(sequence.charAt(i));				
				if (sidegr[position] != 0){
					if(sidegr[position].toString().charAt(0) == '-'){
						formula += "+1/(1+(Math.pow(10,(pI" + sidegr[position] + "))))";
					}else{
						formula += "-1/(1+(Math.pow(10,(" + sidegr[position] + "-pI))))";
					}
				}
			}
		}		
		double pI = findPi(formula, 0.5, 0.0) - 0.5;
		pI = findPi(formula, .01, pI) - .01;			
		return Math.round(pI*Math.pow(10,2))/Math.pow(10,2);
	}
	
	public double calculateMW(String sequence){
		char aminoAcid;
		double mW = 18.01524;
		for (int i = 0; i < sequence.length(); i++){
			aminoAcid = sequence.charAt(i);
			if(aminoAcid != ' '){
				mW += aminmass[findElement(aminoAcid)];
			}
		}
		return Math.round(mW*Math.pow(10,2))/Math.pow(10,2);
	}

	public double findPi(String formula, double n, double pI){
		val manager = new ScriptEngineManager();
	    val engine = manager.getEngineByName("js");   
		val aux=formula.replaceAll("pI",String.valueOf(pI));
		try {
			if((Double) engine.eval(aux) >= 0.0) {
				pI=findPi(formula, n, pI+n);
			}
		} catch (ScriptException e) {
			log.error(e.getMessage());
		}		
		return pI;
	}

	public int findElement(char aminoAcid) {
		for (int j = 0; j < 20; j++){
			if (aminoac[j] == aminoAcid) {
				return j;
			}
		}
		return 0;
	}
}
