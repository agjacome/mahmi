package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

import static es.uvigo.ei.sing.mahmi.common.utils.Contracts.requireNonNull;

@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class Project extends Entity {

    private final String name;
    private final String repository;

    @VisibleForJAXB
    public Project() {
        this(Identifier.empty(), "", "");
    }

    private Project(
        final Identifier id, final String name, final String repository
    ) {
        super(id);

        this.name       = requireNonNull(name, "Project name cannot be NULL");
        this.repository = requireNonNull(repository, "Project repository cannot be NULL");
    }

    public static Project Project(
        final Identifier id, final String name, final String repository
    ) {
        return new Project(id, name, repository);
    }

    public static Project Project(
        final String name, final String repository
    ) {
        return new Project(Identifier.empty(), name, repository);
    }

    public String getName() {
        return name;
    }

    public String getRepository() {
        return repository;
    }

    @Override
    public int hashCode() {
        // Entity overridden for special case: case-insensitive strings
        final int nameHash = name.toLowerCase().hashCode();
        final int repoHash = repository.toLowerCase().hashCode();
        return 41 * (41 + nameHash) + repoHash;
    }

    @Override
    public boolean equals(final Object other) {
        // Entity overridden for special case: case-insensitive strings
        if (this  == other) return true;
        if (other == null ) return false;

        if (getClass() != other.getClass()) return false;

        final Project that = (Project) other;
        return this.name.equalsIgnoreCase(that.name)
            && this.repository.equalsIgnoreCase(that.repository);
    }

}