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
@AllArgsConstructor(staticName = "bioactivePeptide")
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class BioactivePeptide implements Entity<BioactivePeptide> {

    public static final Hash<BioactivePeptide>  hash  = CompoundSequence.hash.contramap(BioactivePeptide::getSequence);
    public static final Equal<BioactivePeptide> equal = CompoundSequence.equal.contramap(BioactivePeptide::getSequence);
    public static final Ord<BioactivePeptide>   ord   = CompoundSequence.ord.contramap(BioactivePeptide::getSequence);

    private final Identifier        id;
    private final AminoAcidSequence sequence;
    private final double            similarity;
    private final ReferencePeptide  reference;

    @VisibleForJAXB public BioactivePeptide() {
        this(new Identifier(), AminoAcidSequence.empty(), 0.0, new ReferencePeptide());
    }

    public static BioactivePeptide bioactivePeptide(final AminoAcidSequence sequence, 
										    	   final double similarity,
										    	   final ReferencePeptide reference) {
        return bioactivePeptide(Identifier.empty(), sequence, similarity, reference);
    }

    public SHA1 getSHA1() {
        return sequence.getSHA1();
    }

}
