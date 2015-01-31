package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.SHA1;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

@EqualsAndHashCode(exclude = "id")
@AllArgsConstructor(staticName = "protein")
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
@Data public final class Protein implements Entity<Protein> {

    private Identifier        id;
    private AminoAcidSequence sequence;

    @VisibleForJAXB public Protein() {
        this(new Identifier(), AminoAcidSequence.empty());
    }

    public static Protein protein(final AminoAcidSequence sequence) {
        return protein(Identifier.empty(), sequence);
    }

    public SHA1 getSHA1() {
        return sequence.getSHA1();
    }

}
