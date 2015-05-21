package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Wither;

import fj.Equal;
import fj.Hash;
import fj.Ord;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.CompoundSequence;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.SHA1;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

@Getter @Wither
@AllArgsConstructor(staticName = "peptide")
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class Peptide implements Entity<Peptide> {

    // public static final Hash<Peptide>  hash  = Hash.stringHash.comap(p -> p.getSequence().asString());
    // public static final Equal<Peptide> equal = Equal.stringEqual.comap(p -> p.getSequence().asString());
    // public static final Ord<Peptide>   ord   = Ord.stringOrd.comap(p -> p.getSequence().asString());
    public static final Hash<Peptide>  hash  = CompoundSequence.hash.comap(Peptide::getSequence);
    public static final Equal<Peptide> equal = CompoundSequence.equal.comap(Peptide::getSequence);
    public static final Ord<Peptide>   ord   = CompoundSequence.ord.comap(Peptide::getSequence);

    private final Identifier        id;
    private final AminoAcidSequence sequence;

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
