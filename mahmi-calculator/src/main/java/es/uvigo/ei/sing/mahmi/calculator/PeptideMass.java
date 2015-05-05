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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mW == null) ? 0 : mW.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PeptideMass other = (PeptideMass) obj;
		if (mW == null) {
			if (other.mW != null)
				return false;
		} else if (!mW.equals(other.mW))
			return false;
		return true;
	}
	
	
}
