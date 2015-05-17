package es.uvigo.ei.sing.mahmi.common.entities.sequences;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.AminoAcid;

import static java.util.Collections.emptyList;

import static es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableUtils.listify;

public final class AminoAcidSequence extends CompoundSequence<AminoAcid> {

    private AminoAcidSequence(final List<AminoAcid> residues) {
        super(residues);
    }

    public static AminoAcidSequence empty() {
        return new AminoAcidSequence(emptyList());
    }

    public static AminoAcidSequence fromIterable(
        final Iterable<AminoAcid> residues
    ) {
        return new AminoAcidSequence(listify(residues));
    }

    public static Optional<AminoAcidSequence> fromString(
        final String sequence
    ) {
        final List<AminoAcid> residues = new ArrayList<>(sequence.length());

        for (final char code: sequence.toCharArray()) {
            final Optional<AminoAcid> aa = AminoAcid.fromCode(code);

            if (aa.isPresent())
                residues.add(aa.get());
            else
                return Optional.empty();
        }

        return Optional.of(residues).map(AminoAcidSequence::new);
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
            && this.residues.equals(((AminoAcidSequence) that).residues);
    }

}
