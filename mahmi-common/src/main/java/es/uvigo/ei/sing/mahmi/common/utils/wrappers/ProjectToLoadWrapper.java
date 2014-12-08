package es.uvigo.ei.sing.mahmi.common.utils.wrappers;

import static es.uvigo.ei.sing.mahmi.common.entities.Project.project;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import es.uvigo.ei.sing.mahmi.common.entities.Project;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(staticName = "loadProject")
@XmlRootElement(name = "loadProject") @XmlAccessorType(XmlAccessType.FIELD)
public class ProjectToLoadWrapper {

    // Required because Java sucks, and JAXB sucks and Jersey sucks

    private final Project project;
    private final String  path;

    @VisibleForJAXB
    public ProjectToLoadWrapper() {
        this(project("", ""), "/tmp");
    }

}
