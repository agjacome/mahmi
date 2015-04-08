package es.uvigo.ei.sing.mahmi.calculator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;

@AllArgsConstructor
@Getter
@Setter
public class PeptidePI implements Comparable<PeptidePI>{
	private Double pI;		
	private AminoAcidSequence sequence;

	@Override
	public int compareTo(PeptidePI ppi) { 
        return this.pI.compareTo(ppi.pI);
	}
}
