package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

import static es.uvigo.ei.sing.mahmi.common.entities.Enzyme.Enzyme;
import static es.uvigo.ei.sing.mahmi.common.entities.Peptide.Peptide;
import static es.uvigo.ei.sing.mahmi.common.entities.Protein.Protein;
import static es.uvigo.ei.sing.mahmi.common.utils.Contracts.requireNonNull;

@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class Digestion extends Entity {

    private final Protein protein;
    private final Peptide peptide;
    private final Enzyme  enzyme;
    private final long    counter;

    @VisibleForJAXB
    public Digestion() {
        this(
            Identifier.empty(),
            Protein(AminoAcidSequence.empty()),
            Peptide(AminoAcidSequence.empty()),
            Enzyme(""),
            0L
        );
    }

    private Digestion(
        final Identifier id,
        final Protein    protein,
        final Peptide    peptide,
        final Enzyme     enzyme,
        final long       counter
    ) {
        super(id);

        this.protein = requireNonNull(protein, "Digestion protein cannot be NULL");
        this.peptide = requireNonNull(peptide, "Digestion peptide cannot be NULL");
        this.enzyme  = requireNonNull(enzyme,  "Digestion enzyme cannot be NULL");
        this.counter = counter;
    }

    public static Digestion Digestion(
        final Protein protein, final Peptide peptide, final Enzyme enzyme
    ) {
        return new Digestion(Identifier.empty(), protein, peptide, enzyme, 0L);
    }

    public static Digestion Digestion(
        final Identifier id,
        final Protein    protein,
        final Peptide    peptide,
        final Enzyme     enzyme
    ) {
        return new Digestion(id, protein, peptide, enzyme, 0L);
    }

    public static Digestion Digestion(
        final Protein protein,
        final Peptide peptide,
        final Enzyme  enzyme,
        final long    counter
    ) {
        return new Digestion(Identifier.empty(), protein, peptide, enzyme, counter);
    }


    public static Digestion Digestion(
        final Identifier id,
        final Protein    protein,
        final Peptide    peptide,
        final Enzyme     enzyme,
        final long       counter
    ) {
        return new Digestion(id, protein, peptide, enzyme, counter);
    }

    public Protein getProtein() {
        return protein;
    }

    public Peptide getPeptide() {
        return peptide;
    }

    public Enzyme getEnzyme() {
        return enzyme;
    }

    public long getCounter() {
        return counter;
    }

    @Override
    public int hashCode() {
        // Entity overridden for special case: counter is irrelevant to equality
        final int proteinHash = protein.hashCode();
        final int peptideHash = peptide.hashCode();
        final int enzymeHash  = enzyme.hashCode();
        return 41 * (41 * (41 + proteinHash) + peptideHash) + enzymeHash;
    }

    @Override
    public boolean equals(final Object other) {
        // Entity overridden for special case: counter is irrelevant to equality
        if (this  == other) return true;
        if (other == null ) return false;

        if (getClass() != other.getClass()) return false;

        final Digestion that = (Digestion) other;
        return this.protein.equals(that.protein)
            && this.peptide.equals(that.peptide)
            && this.enzyme.equals(that.enzyme);
    }

}
