package es.uvigo.ei.sing.mahmi.common.entities.sequences;

import java.util.Objects;

import lombok.experimental.ExtensionMethod;

import fj.Equal;
import fj.Hash;
import fj.data.Seq;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.Compound;
import es.uvigo.ei.sing.mahmi.common.utils.SHA1;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.SeqExtensionMethods;

@ExtensionMethod({ SeqExtensionMethods.class, Objects.class })
public abstract class CompoundSequence<A extends Compound> {

    public static final Hash<CompoundSequence<? extends Compound>> hash =
        SHA1.hash.comap(CompoundSequence::getSHA1);

    public static final Equal<CompoundSequence<? extends Compound>> equal =
        SHA1.equal.comap(CompoundSequence::getSHA1);

    private String asString = null;

    public boolean isEmpty() {
        return getResidues().isEmpty();
    }

    public SHA1 getSHA1() {
        return SHA1.of(asString());
    }

    public synchronized String asString() {
        if (asString.isNull()) {
            asString = getResidues().map(Compound::getCode).buildString();
        }

        return asString;
    }

    public abstract Seq<A> getResidues();

}
