package es.uvigo.ei.sing.mahmi.http.wrappers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Value;
import es.uvigo.ei.sing.mahmi.common.entities.Project;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

@AllArgsConstructor(staticName = "loadProject")
@XmlRootElement(name = "loadProject") @XmlAccessorType(XmlAccessType.FIELD)
@Value public class LoadProjectWrapper {

    private final Project project;
    private final String  path;

    @VisibleForJAXB
    public LoadProjectWrapper() {
        this(new Project(), "/tmp");
    }

}
