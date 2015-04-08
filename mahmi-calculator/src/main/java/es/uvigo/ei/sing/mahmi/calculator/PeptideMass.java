package es.uvigo.ei.sing.mahmi.calculator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;

@AllArgsConstructor
@Getter
@Setter
public class PeptideMass implements Comparable<PeptideMass>{
	private Double mW;
	private AminoAcidSequence sequence;

	@Override
	public int compareTo(PeptideMass ppm) { 
        return this.mW.compareTo(ppm.mW);
	}
}
