package es.uvigo.ei.sing.mahmi.common.utils.wrappers;

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
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(staticName = "wrapProteinsToCut")
@XmlRootElement(name = "cutProteins") @XmlAccessorType(XmlAccessType.FIELD)
public final class MultipleProteinsToCutWrapper {

    // Gawd, this sucks too much already

    private final Set<Protein> proteins;
    private final Set<Enzyme>  enzymes;
    private final int          minSize;
    private final int          maxSize;

    @VisibleForJAXB
    public MultipleProteinsToCutWrapper() {
        this(emptySet(), emptySet(), 0, Integer.MAX_VALUE);
    }

}
