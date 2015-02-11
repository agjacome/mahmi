package es.uvigo.ei.sing.mahmi.common.entities.sequences;

import java.util.Objects;

import lombok.experimental.ExtensionMethod;
import es.uvigo.ei.sing.mahmi.common.entities.compounds.Compound;
import es.uvigo.ei.sing.mahmi.common.utils.SHA1;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.SeqExtensionMethods;
import fj.Equal;
import fj.Hash;
import fj.data.Seq;

@ExtensionMethod({ SeqExtensionMethods.class, Objects.class })
public abstract class CompoundSequence<A extends Compound> {

    public static final Hash<CompoundSequence<? extends Compound>> hash =
        SHA1.hash.comap(CompoundSequence::getSHA1);

    public static final Equal<CompoundSequence<? extends Compound>> equal =
        SHA1.equal.comap(CompoundSequence::getSHA1);

    private StringBuilder strBuilder = null;

    public boolean isEmpty() {
        return getResidues().isEmpty();
    }

    public SHA1 getSHA1() {
        return SHA1.of(asString());
    }

    public String asString() {
        if (strBuilder.isNull())
            strBuilder = getResidues().map(Compound::getCode).buildString();

        return strBuilder.toString();
    }

    public abstract Seq<A> getResidues();

}
