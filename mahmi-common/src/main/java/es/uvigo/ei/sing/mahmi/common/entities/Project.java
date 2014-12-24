package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

@EqualsAndHashCode(exclude = "id")
@AllArgsConstructor(staticName = "project")
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
@Data public final class Project implements Entity<Project> {

    private Identifier id;
    private String     name;
    private String     repository;

    @VisibleForJAXB public Project() {
        this(new Identifier(), "", "");
    }

    public static Project project(final String name, final String repository) {
        return project(Identifier.empty(), name, repository);
    }

}

