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
@AllArgsConstructor(staticName = "digestion")
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
@Data public final class Digestion implements Entity<Digestion> {

    private Identifier id;
    private Protein    protein;
    private Peptide    peptide;
    private Enzyme     enzyme;
    private long       counter;

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
