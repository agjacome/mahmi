package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import fj.Equal;
import fj.Hash;
import fj.Ord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Wither;

import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

import static fj.Equal.stringEqual;
import static fj.Hash.stringHash;
import static fj.Ord.stringOrd;

@Getter @Wither
@AllArgsConstructor(staticName = "enzyme")
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class Enzyme implements Entity<Enzyme> {

    public static final Hash<Enzyme>  hash  = stringHash.contramap(Enzyme::getName);
    public static final Equal<Enzyme> equal = stringEqual.contramap(Enzyme::getName);
    public static final Ord<Enzyme>   ord   = stringOrd.contramap(Enzyme::getName);

    private final Identifier id;
    private final String     name;

    @VisibleForJAXB public Enzyme() {
        this(new Identifier(), "");
    }

    public static Enzyme enzyme(final String name) {
        return enzyme(Identifier.empty(), name);
    }

}
