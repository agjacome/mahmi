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

import static fj.Equal.p3Equal;
import static fj.Hash.p3Hash;
import static fj.Ord.p3Ord;
import static fj.P.p;

@Getter @Wither
@AllArgsConstructor(staticName = "digestion")
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class Digestion implements Entity<Digestion> {

    public static final Hash<Digestion> hash =
        p3Hash(Protein.hash, Peptide.hash, Enzyme.hash).contramap(d -> p(d.protein, d.peptide, d.enzyme));

    public static final Equal<Digestion> equal =
        p3Equal(Protein.equal, Peptide.equal, Enzyme.equal).contramap(d -> p(d.protein, d.peptide, d.enzyme));

    public static final Ord<Digestion> ord =
        p3Ord(Protein.ord, Peptide.ord, Enzyme.ord).contramap(d -> p(d.protein, d.peptide, d.enzyme));

    private final Identifier id;
    private final Protein    protein;
    private final Peptide    peptide;
    private final Enzyme     enzyme;
    private final long       counter;

    @VisibleForJAXB public Digestion() {
        this(new Identifier(), new Protein(), new Peptide(), new Enzyme(), 0L);
    }

    public static Digestion digestion(
        final Protein protein, final Peptide peptide, final Enzyme enzyme
    ) {
        return digestion(Identifier.empty(), protein, peptide, enzyme, 0L);
    }

    public static Digestion digestion(
        final Identifier id,
        final Protein    protein,
        final Peptide    peptide,
        final Enzyme     enzyme
    ) {
        return digestion(id, protein, peptide, enzyme, 0L);
    }

    public static Digestion digestion(
        final Protein protein,
        final Peptide peptide,
        final Enzyme  enzyme,
        final long    counter
    ) {
        return digestion(Identifier.empty(), protein, peptide, enzyme, counter);
    }

}
