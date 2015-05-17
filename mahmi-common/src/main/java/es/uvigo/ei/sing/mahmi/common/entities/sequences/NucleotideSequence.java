package es.uvigo.ei.sing.mahmi.common.entities.sequences;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.Nucleotide;

import static java.util.Collections.emptyList;

import static es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableUtils.listify;

public final class NucleotideSequence extends CompoundSequence<Nucleotide> {

    private NucleotideSequence(final List<Nucleotide> residues) {
        super(residues);
    }

    public static NucleotideSequence empty() {
        return new NucleotideSequence(emptyList());
    }

    public static NucleotideSequence fromIterable(
        final Iterable<Nucleotide> residues
    ) {
        return new NucleotideSequence(listify(residues));
    }

    public static Optional<NucleotideSequence> fromString(
        final String sequence
    ) {
        final List<Nucleotide> residues = new ArrayList<>(sequence.length());

        for (final char code: sequence.toCharArray()) {
            final Optional<Nucleotide> nb = Nucleotide.fromCode(code);

            if (nb.isPresent())
                residues.add(nb.get());
            else
                return Optional.empty();
        }

        return Optional.of(residues).map(NucleotideSequence::new);
    }

    @Override
    public int hashCode() {
        return residues.hashCode();
    }

    @Override
    public boolean equals(final Object that) {
        if (this == that) return true;
        if (that == null) return false;

        return getClass() == that.getClass()
            && this.residues.equals(((NucleotideSequence) that).residues);
    }

}
