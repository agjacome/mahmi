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
@AllArgsConstructor(staticName = "enzyme")
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
@Data public final class Enzyme implements Entity<Enzyme> {

    private Identifier id;
    private String     name;

    @VisibleForJAXB public Enzyme() {
        this(new Identifier(), "");
    }

    public static Enzyme enzyme(final String name) {
        return enzyme(Identifier.empty(), name);
    }

}
