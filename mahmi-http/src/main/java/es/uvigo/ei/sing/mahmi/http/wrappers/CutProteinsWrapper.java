package es.uvigo.ei.sing.mahmi.http.wrappers;

import java.util.LinkedHashSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Value;
import es.uvigo.ei.sing.mahmi.common.entities.Enzyme;
import es.uvigo.ei.sing.mahmi.common.entities.Project;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

@AllArgsConstructor(staticName = "cutProteins")
@XmlRootElement(name = "cutProteins") @XmlAccessorType(XmlAccessType.FIELD)
@Value public final class CutProteinsWrapper {

    private final Project project;

    @XmlElementWrapper(name = "enzymes")
    @XmlElements({@XmlElement(name = "enzyme", type = Enzyme.class)})
    private final LinkedHashSet<Enzyme> enzymes;

    private final int minSize;
    private final int maxSize;

    @VisibleForJAXB
    public CutProteinsWrapper() {
        this(new Project(), new LinkedHashSet<>(), 0, Integer.MAX_VALUE);
    }

}
