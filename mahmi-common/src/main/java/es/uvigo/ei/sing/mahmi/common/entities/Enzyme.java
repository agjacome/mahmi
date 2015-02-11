package es.uvigo.ei.sing.mahmi.common.entities;

import static fj.Equal.stringEqual;
import static fj.Hash.stringHash;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Wither;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;
import fj.Equal;
import fj.Hash;

@Getter @Wither
@AllArgsConstructor(staticName = "enzyme")
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class Enzyme implements Entity<Enzyme> {

    public static final Hash<Enzyme>  hash  = stringHash.comap(Enzyme::getName);
    public static final Equal<Enzyme> equal = stringEqual.comap(Enzyme::getName);

    private final Identifier id;
    private final String     name;

    @VisibleForJAXB public Enzyme() {
        this(new Identifier(), "");
    }

    public static Enzyme enzyme(final String name) {
        return enzyme(Identifier.empty(), name);
    }

}
