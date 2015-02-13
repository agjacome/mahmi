package es.uvigo.ei.sing.mahmi.http.wrappers;

import java.util.LinkedHashSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;

import es.uvigo.ei.sing.mahmi.common.entities.Enzyme;
import es.uvigo.ei.sing.mahmi.common.entities.Project;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

import static es.uvigo.ei.sing.mahmi.common.entities.Project.project;

@Getter
@AllArgsConstructor(staticName = "wrap")
@XmlRootElement(name = "cutProteins") @XmlAccessorType(XmlAccessType.FIELD)
public final class CutProteinsWrapper {

    private final Project project;

    @XmlElementWrapper(name = "enzymes")
    @XmlElements({@XmlElement(name = "enzyme", type = Enzyme.class)})
    private final LinkedHashSet<Enzyme> enzymes;

    private final int minSize;
    private final int maxSize;

    @VisibleForJAXB
    public CutProteinsWrapper() {
        this.project = project("", "");
        this.enzymes = new LinkedHashSet<Enzyme>();
        this.minSize = 0;
        this.maxSize = Integer.MAX_VALUE;
    }

}
