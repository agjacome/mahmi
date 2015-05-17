package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

import static es.uvigo.ei.sing.mahmi.common.utils.Contracts.requireNonNull;

@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class Measure extends Entity {

    private final String name;
    private final long   counter;

    @VisibleForJAXB
    public Measure() {
        this(Identifier.empty(), "", 0L);
    }

    private Measure(
        final Identifier id, final String name, final long counter
    ) {
        super(id);

        this.name    = requireNonNull(name, "Measure name cannot be NULL");
        this.counter = counter;
    }

    public static Measure Measure(
        final Identifier id, final String name, final long counter
    ) {
        return new Measure(id, name, counter);
    }

    public static Measure Measure(final Identifier id, final String name) {
        return new Measure(id, name, 0L);
    }

    public static Measure Measure(final String name, final long counter) {
        return new Measure(Identifier.empty(), name, counter);
    }

    public static Measure Measure(final String name) {
        return new Measure(Identifier.empty(), name, 0L);
    }

    public String getName() {
        return name;
    }

    public long getCounter() {
        return counter;
    }

    @Override
    public int hashCode() {
        // Entity overridden for special case:
        // - case-insensitive name
        // - counter is irrelevant to equality
        return name.toLowerCase().hashCode();
    }

    @Override
    public boolean equals(final Object that) {
        // Entity overridden for special case:
        // - case-insensitive name
        // - counter is irrelevant to equality
        if (this == that) return true;
        if (that == null) return false;

        return getClass() == that.getClass()
            && name.equalsIgnoreCase(((Measure) that).name);
    }

}