package es.uvigo.ei.sing.mahmi.cutter.cutters;

import es.uvigo.ei.sing.mahmi.common.entities.Digestion;
import es.uvigo.ei.sing.mahmi.common.entities.Enzyme;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import fj.F;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.expasy.mzjava.proteomics.mol.digest.CleavageSiteMatcher;
import org.expasy.mzjava.proteomics.mol.digest.Protease;
import org.expasy.mzjava.proteomics.mol.digest.ProteinDigester;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static es.uvigo.ei.sing.mahmi.common.entities.Digestion.digestion;
import static es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence.fromString;
import static es.uvigo.ei.sing.mahmi.common.utils.Comparators.between;
import static es.uvigo.ei.sing.mahmi.common.utils.extensions.CollectionsExtensionMethods.frequencies;
import static java.util.stream.Collectors.toSet;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProteinCutter {

    public static ProteinCutter proteinCutter() {
        return new ProteinCutter();
    }

    public Set<Digestion> cutProtein(
        final Protein protein, final Set<Enzyme> enzymes, final int minSize, final int maxSize
    ) {
        return enzymes.parallelStream().flatMap(
            enzyme -> cutProtein(protein, enzyme, minSize, maxSize)
        ).collect(toSet());
    }

    public Set<Digestion> cutAllProteins(
        final Set<Protein> proteins, final Set<Enzyme> enzymes, final int minSize, final int maxSize
    ) {
        return proteins.parallelStream().flatMap(
            protein -> cutProtein(protein, enzymes, minSize, maxSize).parallelStream()
        ).collect(toSet());
    }

    private Stream<Digestion> cutProtein(
        final Protein protein, final Enzyme enzyme, final int minSize, final int maxSize
    ) {
        final Map<Peptide, Long> cuts = getPeptideCuts(getDigester(enzyme), protein.getSequence(), minSize, maxSize);
        return cuts.entrySet().parallelStream().map(
            e -> digestion(protein, e.getKey(), enzyme, e.getValue())
        );
    }

    private ProteinDigester getDigester(final Enzyme enzyme) {
        // FIXME: this implies that every Enzyme name is a valid MzJava
        // Protease,handle the invalid case (enzyme name does not match any
        // protease) in a sensible and safe manner.

        // TODO: replace by following when MzJava Pepsisne-bug becomes fixed:
        // return new ProteinDigester.Builder(Protease.valueOf(enzyme.getName())).build();
        return enzyme.getName().equals("PEPSINE_PH_GT_2")
             ? getPepsineDigester()
             : new ProteinDigester.Builder(Protease.valueOf(enzyme.getName())).build();
    }

    private ProteinDigester getPepsineDigester() {
        // TODO: to-remove when MzJava Pepsine-bug gets fixed
        return new ProteinDigester.Builder(new CleavageSiteMatcher(
            "[^RKH][^RP][FYWL]|[^P][^P][^P] or [^RKH][^RP][^P]|[FYWL][^P][^P]"
        )).build();
    }

    private Map<Peptide, Long> getPeptideCuts(
        final ProteinDigester digester, final AminoAcidSequence aas, final int minSize, final int maxSize
    ) {
        final F<org.expasy.mzjava.proteomics.mol.Peptide, String> errF =
            p -> "MzJava returned an invalid AminoAcid sequence: " + p.toSymbolString();

        final Stream<Peptide> peptides = digester.digest(mzProtein(aas)).parallelStream()
            .filter(p -> between(minSize, p.size(), maxSize))
            .map(p -> fromString(p.toSymbolString()).valueE(errF.f(p)))
            .map(Peptide::peptide);

        return frequencies(peptides);
    }

    private org.expasy.mzjava.proteomics.mol.Protein mzProtein(final AminoAcidSequence aas) {
        return new org.expasy.mzjava.proteomics.mol.Protein(
            aas.toSHA1().asHexString(), aas.toString()
        );
    }

}
