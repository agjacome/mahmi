package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import fj.Equal;
import fj.Hash;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Wither;

import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

import static fj.Equal.p2Equal;
import static fj.Equal.stringEqual;
import static fj.Hash.p2Hash;
import static fj.Hash.stringHash;
import static fj.P.p;

@Getter @Wither
@AllArgsConstructor(staticName = "project")
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class Project implements Entity<Project> {

    public static final Hash<Project> hash =
        p2Hash(stringHash, stringHash).contramap(p -> p(p.name, p.repository));

    public static final Equal<Project> equal =
        p2Equal(stringEqual, stringEqual).contramap(p -> p(p.name, p.repository));

    private final Identifier id;
    private final String     name;
    private final String     repository;

    @VisibleForJAXB
    public Project() {
        this(new Identifier(), "", "");
    }

    public static Project project(final String name, final String repository) {
        return project(Identifier.empty(), name, repository);
    }

}

