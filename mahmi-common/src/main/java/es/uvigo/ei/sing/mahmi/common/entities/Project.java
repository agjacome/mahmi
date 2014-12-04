package es.uvigo.ei.sing.mahmi.common.entities;

import static fj.P.p;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;
import fj.P3;

@Getter
@ToString
@EqualsAndHashCode(exclude = "id")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class Project {

    private final Identifier id;
    private final String     name;
    private final String     repository;

    @VisibleForJAXB
    public Project() {
        this(Identifier.empty(), "", "");
    }

    public static Project project(final String name, final String repository) {
        return new Project(Identifier.empty(), name, repository);
    }

    public static Project project(final int id, final String name, final String repository) {
        return project(name, repository).withId(id);
    }

    public Project withId(final int id) {
        return new Project(Identifier.of(id), name, repository);
    }

    public P3<Identifier, String, String> toProduct() {
        return p(id, name, repository);
    }

}

