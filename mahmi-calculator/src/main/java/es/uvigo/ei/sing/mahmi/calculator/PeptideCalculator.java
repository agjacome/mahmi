package es.uvigo.ei.sing.mahmi.calculator;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

//Source http://www.fmi.ch/paf/tools/pepeval.html
public class PeptideCalculator {
	
	final char[] aminoac = { 'A', 'R', 'N', 'D', 'C', 'E', 'Q', 'G', 'H', 'I', 'L', 'K', 'M',
	        'F', 'P', 'S', 'T', 'W', 'Y', 'V' };

	final Double[] cterm = { 2.35, 2.17, 2.02, 2.09, 1.71, 2.19, 2.17, 2.34, 1.82, 2.36, 2.36, 2.18, 
	           2.28, 1.83, 1.99, 2.21, 2.63, 2.38, 2.2, 2.32 };
	
	Double[] nterm;

	Double[] sidegr= { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
					   0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };

	final Double[] aminmass = { 71.0788, 156.1875, 114.1038, 115.0886, 103.1388, 129.1155,
	          128.1307, 57.0519, 137.1411, 113.1594, 113.1594, 128.1741,
	          131.1926, 147.1766, 97.1167, 87.0782, 101.1051, 186.2132,
	          163.176, 99.1326 };

	final Double[] aminmono = { 71.03711, 156.10111, 114.04293, 115.02694, 103.00919, 129.04259,
	          128.05858, 57.02146, 137.05891, 113.08406, 113.08406, 128.09496,
	          131.04049, 147.06841, 97.05276, 87.03203, 101.04768, 186.07931,
	          163.06333, 99.06841 };

	
	//check=true:Bjellqvist false:Skoog
	public void CalCulate(boolean check, String sequence) {
		String FoRmU = "";
		double pH = 0.0;
		int number=0;
		double AminMass = 18.01524;
		double AminMono = 18.01056;
		if (check) {		
			Double[] aux = { 7.5, 6.76, 7.22, 7.7, 8.12, 7.19, 6.73, 7.5, 7.18, 7.48, 7.46, 6.67, 
					           6.98, 6.96, 8.36, 6.86, 7.02, 7.11, 6.83, 7.44 };
			nterm=aux;
			sidegr[1] = -12.5;
			sidegr[3] = 4.07;
			sidegr[4] = 8.28;
			sidegr[5] = 4.45;
			sidegr[8] = -6.08;
			sidegr[11] = -9.8;
			sidegr[18] = 9.84;
		}else{		
			Double[] aux = {  9.69, 9.04, 8.8, 9.82, 10.78, 9.67, 9.13, 9.6, 9.17, 9.68, 9.6, 8.95,
							    9.21, 9.13, 10.6, 9.15, 10.43, 9.39, 9.11, 9.62 };
			nterm=aux;
			sidegr[1] = -12.48;
			sidegr[3] = 3.86;
			sidegr[4] = 8.33;
			sidegr[5] = 4.25;
			sidegr[8] = -6.0;
			sidegr[11] = -10.53;
			sidegr[18] = 10.07;
		}
		
		if (sequence.length() != 0){
			char aa1 = sequence.charAt(0);
			number=findElement(aa1);
			FoRmU += "1/(1+(Math.pow(10,(pH-" + nterm[number] + "))))";
			aa1 = sequence.charAt(sequence.length() - 1);
			number=findElement(aa1);
			FoRmU += "-1/(1+(Math.pow(10,(" + cterm[number] + "-pH))))";
			for (int i = 0; i < sequence.length(); i++){
				aa1 = sequence.charAt(i);
				if(aa1 != ' '){
					number=findElement(aa1);
					AminMass += aminmass[number];
					AminMono += aminmono[number];
					if (sidegr[number] != 0){
						if(sidegr[number].toString().charAt(0) == '-'){
							FoRmU += "+1/(1+(Math.pow(10,(pH" + sidegr[number] + "))))";
						}else{
							FoRmU += "-1/(1+(Math.pow(10,(" + sidegr[number] + "-pH))))";
						}
					}
				}
			}
			double n = 0.5;
			pH=findpi(FoRmU, n, pH);
			pH = pH-n;
			n=.01;
			pH=findpi(FoRmU, n, pH);
			pH = pH-n;
			System.out.println("pi: "+ Math.round(pH*Math.pow(10,2))/Math.pow(10,2));
			System.out.println("MonoMass"+Math.round(AminMono*Math.pow(10,2))/Math.pow(10,2));
			System.out.println("AverMass"+Math.round(AminMass*Math.pow(10,2))/Math.pow(10,2));
		}else{
			System.out.println("Enter a peptide sequence!");
		}
	}

	public double findpi(String FoRmU, double n, double pH){
		ScriptEngineManager manager = new ScriptEngineManager();
	    ScriptEngine engine = manager.getEngineByName("js");        
	    Double result;
		try {
			String aux=FoRmU.replaceAll("pH",String.valueOf(pH));
			result = (Double) engine.eval(aux);
			System.out.println(result+", "+n+", "+pH);
			if(result >= 0.0) {
				pH = pH+n;
				pH=findpi(FoRmU, n, pH);
			}
		} catch (ScriptException e) {
			e.printStackTrace();
		}		
		return pH;
	}

	public int findElement(char aa1) {
		for (int j = 0; j < 20; j++){
			if (aminoac[j] == aa1) {
				return j;
			}
		}
		return 0;
	}
}
