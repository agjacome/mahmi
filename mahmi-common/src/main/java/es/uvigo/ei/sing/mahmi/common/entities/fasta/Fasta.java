package es.uvigo.ei.sing.mahmi.common.entities.fasta;

import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import es.uvigo.ei.sing.mahmi.common.entities.compounds.ChemicalCompound;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.ChemicalCompoundSequence;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Fasta<A extends ChemicalCompoundSequence<? extends ChemicalCompound>> {

    protected final Map<A, Integer> sequences;

}
