package es.uvigo.ei.sing.mahmi.common.entities.sequences;

import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.List;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.Compound;
import es.uvigo.ei.sing.mahmi.common.utils.SHA1;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.joining;

public abstract class CompoundSequence<A extends Compound> implements Iterable<A> {

    protected final List<A> residues;

    private volatile SoftReference<String> asString;

    protected CompoundSequence(final List<A> residues) {
        this.residues = unmodifiableList(residues);
    }

    public List<A> getResidues() {
        return residues;
    }

    public boolean isEmpty() {
        return getResidues().isEmpty();
    }

    public SHA1 getSHA1() {
        return SHA1.of(toString());
    }

    @Override
    public Iterator<A> iterator() {
        return residues.iterator();
    }

    @Override
    public final synchronized String toString() {
        if (asString == null || asString.get() == null) {
            asString = new SoftReference<>(residues.stream().map(
                c -> String.valueOf(c.getCode())
           ).collect(joining()));
        }

        return asString.get();
    }

}
