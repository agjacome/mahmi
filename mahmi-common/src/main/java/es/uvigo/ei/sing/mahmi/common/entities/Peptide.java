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
@AllArgsConstructor(staticName = "peptide")
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
@Data public final class Peptide implements Entity<Peptide> {

    private Identifier        id;
    private AminoAcidSequence sequence;

    @VisibleForJAXB public Peptide() {
        this(new Identifier(), AminoAcidSequence.empty());
    }

    public static Peptide peptide(final AminoAcidSequence sequence) {
        return peptide(Identifier.empty(), sequence);
    }

    public SHA1 getSHA1() {
        return sequence.getSHA1();
    }

}
