package es.uvigo.ei.sing.mahmi.common.utils.wrappers;

import static es.uvigo.ei.sing.mahmi.common.entities.Protein.protein;
import static java.util.Collections.emptySet;

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import es.uvigo.ei.sing.mahmi.common.entities.Enzyme;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(staticName = "wrapProteinToCut")
@XmlRootElement(name = "cutProtein") @XmlAccessorType(XmlAccessType.FIELD)
public final class OneProteinToCutWrapper {

    // Required because Java sucks, and Jersey sucks, and JAXB sucks

    private final Protein     protein;
    private final Set<Enzyme> enzymes;
    private final int         minSize;
    private final int         maxSize;

    @VisibleForJAXB
    public OneProteinToCutWrapper() {
        this(protein(AminoAcidSequence.empty()), emptySet(), 0, Integer.MAX_VALUE);
    }

}
