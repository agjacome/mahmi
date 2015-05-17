package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

import static es.uvigo.ei.sing.mahmi.common.utils.Contracts.requireNonNull;

@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class Enzyme extends Entity {

    private final String name;

    @VisibleForJAXB
    public Enzyme() {
        this(Identifier.empty(), "");
    }

    private Enzyme(final Identifier id, final String name) {
        super(id);

        this.name = requireNonNull(name, "Enzyme name cannot be NULL");
    }

    public static Enzyme Enzyme(final Identifier id, final String name) {
        return new Enzyme(id, name);
    }

    public static Enzyme Enzyme(final String name) {
        return new Enzyme(Identifier.empty(), name);
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        // Entity overridden for special case: case-insensitive name
        return name.toLowerCase().hashCode();
    }

    @Override
    public boolean equals(final Object that) {
        // Entity overridden for special case: case-insensitive name
        if (this == that) return true;
        if (that == null) return false;

        return getClass() == that.getClass()
            && this.name.equalsIgnoreCase(((Enzyme) that).name);
    }

}
