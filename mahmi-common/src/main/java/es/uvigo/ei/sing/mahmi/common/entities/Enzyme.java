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
import fj.P2;

@Getter
@ToString
@EqualsAndHashCode(exclude = "id")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class Enzyme {

    private final Identifier id;
    private final String     name;

    @VisibleForJAXB
    public Enzyme() {
        this(Identifier.empty(), "");
    }

    public static Enzyme enzyme(final String name) {
        return new Enzyme(Identifier.empty(), name);
    }

    public static Enzyme enzyme(final int id, final String name) {
        return enzyme(name).withId(id);
    }

    public Enzyme withId(final int id) {
        return new Enzyme(Identifier.of(id), name);
    }

    public P2<Identifier, String> toProduct() {
        return p(id, name);
    }

}
