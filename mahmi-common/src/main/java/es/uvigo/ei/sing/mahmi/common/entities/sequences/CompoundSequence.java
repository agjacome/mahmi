 package es.uvigo.ei.sing.mahmi.common.entities.sequences;

import static fj.Show.showS;
import es.uvigo.ei.sing.mahmi.common.entities.compounds.Compound;
import es.uvigo.ei.sing.mahmi.common.utils.SHA1;
import fj.Equal;
import fj.Hash;
import fj.Show;
import fj.data.List;

public interface CompoundSequence<A extends Compound> {

    public static final Hash<CompoundSequence<? extends Compound>> hash =
        SHA1.hash.comap(CompoundSequence::getSHA1);
        // listHash(Compound.hash).comap(CompoundSequence::getSequence);

    public static final Equal<CompoundSequence<? extends Compound>> equal =
        SHA1.equal.comap(CompoundSequence::getSHA1);
        // listEqual(Compound.equal).comap(CompoundSequence::getSequence);

    public static final Show<CompoundSequence<? extends Compound>> show =
        showS(cs -> List.asString(cs.getResidues().map(Compound::getCode)));

    public List<A> getResidues();

    public default boolean isEmpty() {
        return getResidues().isEmpty();
    }

    public default SHA1 getSHA1() {
        return SHA1.of(getResidues().map(Compound::getCode));
    }

}
