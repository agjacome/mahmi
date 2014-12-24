package es.uvigo.ei.sing.mahmi.http.wrappers;

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
import es.uvigo.ei.sing.mahmi.common.entities.Project;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(staticName = "cutProteins")
@XmlRootElement(name = "cutProteins") @XmlAccessorType(XmlAccessType.FIELD)
public final class CutProteinsWrapper {

    private final Project      project;
    private final Set<Enzyme>  enzymes;
    private final int          minSize;
    private final int          maxSize;

    @VisibleForJAXB
    public CutProteinsWrapper() {
        this(new Project(), emptySet(), 0, Integer.MAX_VALUE);
    }

}
