package es.uvigo.ei.sing.mahmi.common.entities;

import static es.uvigo.ei.sing.mahmi.common.entities.Enzyme.enzyme;
import static es.uvigo.ei.sing.mahmi.common.entities.Peptide.peptide;
import static es.uvigo.ei.sing.mahmi.common.entities.Protein.protein;
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
import fj.P4;

@Getter
@ToString
@EqualsAndHashCode(exclude = "counter")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class Digestion {

    private final Identifier id;
    private final Protein    protein;
    private final Peptide    peptide;
    private final Enzyme     enzyme;
    private final long       counter;

    @VisibleForJAXB
    public Digestion() {
        this(
            Identifier.empty(),
            protein(AminoAcidSequence.empty()),
            peptide(AminoAcidSequence.empty()),
            enzyme(""),
            0L
        );
    }

    public static Digestion digestion(
        final Protein protein, final Peptide peptide, final Enzyme enzyme
    ) {
        return new Digestion(Identifier.empty(), protein, peptide, enzyme, 0L);
    }

    public static Digestion digestion(
        final Protein protein, final Peptide peptide, final Enzyme enzyme, final long counter
    ) {
        return new Digestion(Identifier.empty(), protein, peptide, enzyme, counter);
    }

    public static Digestion digestion(
        final int id, final Protein protein, final Peptide peptide, final Enzyme enzyme
    ) {
        return digestion(protein, peptide, enzyme, 0L).withId(id);
    }

    public static Digestion digestion(
        final int id, final Protein protein, final Peptide peptide, final Enzyme enzyme, final long counter
    ) {
        return digestion(protein, peptide, enzyme, counter).withId(id);
    }

    public Digestion withId(final int id) {
        return new Digestion(Identifier.of(id), protein, peptide, enzyme, counter);
    }

    public Digestion withCounter(final long counter) {
        return new Digestion(id, protein, peptide, enzyme, counter);
    }

    public Digestion withPeptide(final Peptide peptide) {
        return new Digestion(id, protein, peptide, enzyme, counter);
    }

    public P4<Protein, Peptide, Enzyme, Long> toProduct() {
        return p(protein, peptide, enzyme, counter);
    }

}
