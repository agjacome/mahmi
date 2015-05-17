package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.SHA1;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

import static es.uvigo.ei.sing.mahmi.common.utils.Contracts.requireNonNull;

@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class Protein extends Entity {

    private final AminoAcidSequence sequence;

    @VisibleForJAXB
    public Protein() {
        this(Identifier.empty(), AminoAcidSequence.empty());
    }

    private Protein(final Identifier id, final AminoAcidSequence sequence) {
        super(id);

        this.sequence = requireNonNull(sequence, "Protein sequence cannot be NULL");
    }

    public static Protein Protein(
        final Identifier id, final AminoAcidSequence sequence
    ) {
        return new Protein(id, sequence);
    }

    public static Protein Protein(final AminoAcidSequence sequence) {
        return new Protein(Identifier.empty(), sequence);
    }

    public AminoAcidSequence getSequence() {
        return sequence;
    }

    public SHA1 getSHA1() {
        return sequence.getSHA1();
    }

}
