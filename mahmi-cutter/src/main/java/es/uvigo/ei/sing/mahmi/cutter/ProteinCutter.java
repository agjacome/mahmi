package es.uvigo.ei.sing.mahmi.cutter;

import org.expasy.mzjava.proteomics.mol.digest.CleavageSiteMatcher;
import org.expasy.mzjava.proteomics.mol.digest.Protease;
import org.expasy.mzjava.proteomics.mol.digest.ProteinDigester;

import fj.F;
import fj.data.HashMap;
import fj.data.HashSet;
import fj.data.Option;
import fj.data.Set;
import lombok.AllArgsConstructor;
import lombok.val;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;

import es.uvigo.ei.sing.mahmi.common.entities.Digestion;
import es.uvigo.ei.sing.mahmi.common.entities.Enzyme;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableExtensionMethods;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.OptionExtensionMethods;

import static es.uvigo.ei.sing.mahmi.common.entities.Digestion.digestion;
import static es.uvigo.ei.sing.mahmi.cutter.CutterException.withCause;
import static es.uvigo.ei.sing.mahmi.cutter.CutterException.withMessage;

@Slf4j
@AllArgsConstructor(staticName = "proteinCutter")
@ExtensionMethod({ IterableExtensionMethods.class, OptionExtensionMethods.class })
public final class ProteinCutter {

    public Set<Digestion> cutProtein(
        final Protein     protein,
        final Set<Enzyme> enzymes,
        final F<Integer, Boolean> sizeFilter
    ) throws CutterException {
        val digestions = new HashSet<>(Digestion.equal, Digestion.hash);

        for (val enzyme : enzymes)
            digest(protein, enzyme, sizeFilter).forEach(digestions::set);

        return digestions.toSet(Digestion.ord);
    }

    public Set<Digestion> cutProteins(
        final Set<Protein> proteins,
        final Set<Enzyme>  enzymes,
        final F<Integer, Boolean> sizeFilter
    ) throws CutterException {
        val digestions = new HashSet<>(Digestion.equal, Digestion.hash);

        for (val protein : proteins)
            cutProtein(protein, enzymes, sizeFilter).forEach(digestions::set);

        return digestions.toSet(Digestion.ord);
    }

    private Set<Digestion> digest(
        final Protein protein,
        final Enzyme  enzyme,
        final F<Integer, Boolean> sizeFilter
    ) {
        log.debug("Cutting {} with {}", protein, enzyme);

        val digester = getDigesterFor(enzyme);
        val sequence = protein.getSequence();
        val cutFreqs = cutAminoAcidSequence(digester, sequence, sizeFilter);

        return createDigestions(cutFreqs, protein, enzyme);
    }

    private ProteinDigester getDigesterFor(final Enzyme enzyme) {
        val matcher = enzyme.getName().equals("PEPSINE_PH_GT_2")
            ? getPepsineMatcher()
            : getMatcherByName(enzyme.getName());

        return new ProteinDigester.Builder(matcher).build();
    }

    private HashMap<Peptide, Long> cutAminoAcidSequence(
        final ProteinDigester     digester,
        final AminoAcidSequence   sequence,
        final F<Integer, Boolean> sizeFilter
    ) {
        val mzPeptides = digester.digest(toMzProtein(sequence));

        return mzPeptides.toStream()
            .filter(p -> sizeFilter.f(p.size()))
            .map(this::toAminoAcidSequence)
            .map(Peptide::peptide)
            .frequencies(Peptide.equal, Peptide.hash);
    }

    private Set<Digestion> createDigestions(
        final HashMap<Peptide, Long> cuts,
        final Protein protein,
        final Enzyme  enzyme
    ) {
        val digestions = new HashSet<>(Digestion.equal, Digestion.hash);

        for (val peptide : cuts) {
            val counter   = cuts.get(peptide).or(1L);
            val digestion = digestion(protein, peptide, enzyme, counter);
            digestions.set(digestion);
        }

        return digestions.toSet(Digestion.ord);
    }

    private org.expasy.mzjava.proteomics.mol.Protein toMzProtein(
        final AminoAcidSequence sequence
    ) {
        return new org.expasy.mzjava.proteomics.mol.Protein(
            "", sequence.asString()
        );
    }

    private AminoAcidSequence toAminoAcidSequence(
        final org.expasy.mzjava.proteomics.mol.Peptide mzPeptide
    ) {
        val sequence = mzPeptide.toSymbolString();

        final Option<AminoAcidSequence> aa = AminoAcidSequence.fromString(sequence);
        if (aa.isSome()) return aa.some();

        throw withMessage("Illegal aminoacid sequence produced by MZJava: " + sequence);
    }

    private CleavageSiteMatcher getMatcherByName(final String name) {
        try {
            return Protease.valueOf(name).getCleavageSiteMatcher();
        } catch (final IllegalArgumentException iae) {
            log.error("Invalid Protease name: {}", name);
            throw withCause(iae);
        }
    }

    private CleavageSiteMatcher getPepsineMatcher() {
        return new CleavageSiteMatcher(
            "[^RKH][^RP][FYWL]|[^P][^P][^P] or [^RKH][^RP][^P]|[FYWL][^P][^P]"
        );
    }


}
