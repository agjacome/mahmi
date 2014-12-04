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

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(staticName = "wrapProjectToLoad")
@XmlRootElement(name = "loadProject") @XmlAccessorType(XmlAccessType.FIELD)
public class ProjectToLoadWrapper {

    private final Project project;
    private final String  path;

    public ProjectToLoadWrapper() {
        this(project("", ""), "/tmp");
    }

}
