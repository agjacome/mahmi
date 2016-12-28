package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.CompoundSequence;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.SHA1;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;
import fj.Equal;
import fj.Hash;
import fj.Ord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Wither;

@Getter @Wither
@AllArgsConstructor(staticName = "referencePeptide")
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class ReferencePeptide implements Entity<ReferencePeptide> {

    public static final Hash<ReferencePeptide>  hash  = CompoundSequence.hash.contramap(ReferencePeptide::getSequence);
    public static final Equal<ReferencePeptide> equal = CompoundSequence.equal.contramap(ReferencePeptide::getSequence);
    public static final Ord<ReferencePeptide>   ord   = CompoundSequence.ord.contramap(ReferencePeptide::getSequence);

    private final Identifier        id;
    private final AminoAcidSequence sequence;
    private final String            bioactivity;

    @VisibleForJAXB public ReferencePeptide() {
        this(new Identifier(), AminoAcidSequence.empty(), "");
    }

    public static ReferencePeptide referencePeptide(final AminoAcidSequence sequence, final String bioactivity) {
        return referencePeptide(Identifier.empty(), sequence, bioactivity);
    }

    public SHA1 getSHA1() {
        return sequence.getSHA1();
    }

}
