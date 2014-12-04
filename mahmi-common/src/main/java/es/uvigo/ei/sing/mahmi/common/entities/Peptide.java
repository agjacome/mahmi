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
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;
import fj.P2;

@Getter
@ToString
@EqualsAndHashCode(exclude = "id")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class Peptide {

    private final Identifier        id;
    private final AminoAcidSequence sequence;

    @VisibleForJAXB
    public Peptide() {
        this(Identifier.empty(), AminoAcidSequence.empty());
    }

    public static Peptide peptide(final AminoAcidSequence sequence) {
        return new Peptide(Identifier.empty(), sequence);
    }

    public static Peptide peptide(final int id, final AminoAcidSequence sequence) {
        return peptide(sequence).withId(id);
    }

    public Peptide withId(final int id) {
        return new Peptide(Identifier.of(id), sequence);
    }

    public P2<Identifier, AminoAcidSequence> toProduct() {
        return p(id, sequence);
    }

}
