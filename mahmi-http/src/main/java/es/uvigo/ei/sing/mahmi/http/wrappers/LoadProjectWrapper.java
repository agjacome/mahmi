package es.uvigo.ei.sing.mahmi.http.wrappers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;

import es.uvigo.ei.sing.mahmi.common.entities.Project;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

import static es.uvigo.ei.sing.mahmi.common.entities.Project.project;

@Getter
@AllArgsConstructor(staticName = "wrap")
@XmlRootElement(name = "loadProject") @XmlAccessorType(XmlAccessType.FIELD)
public class LoadProjectWrapper {

    private final Project project;
    private final String  path;

    @VisibleForJAXB
    public LoadProjectWrapper() {
        this.project = project("", "");
        this.path    = "/tmp";
    }

}
