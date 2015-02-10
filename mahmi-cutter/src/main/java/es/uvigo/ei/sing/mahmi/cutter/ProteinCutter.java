package es.uvigo.ei.sing.mahmi.cutter;

import static es.uvigo.ei.sing.mahmi.common.entities.Digestion.digestion;
import static es.uvigo.ei.sing.mahmi.common.utils.extensions.CollectionExtensionMethods.frequencies;
import static es.uvigo.ei.sing.mahmi.cutter.CutterException.withCause;
import static es.uvigo.ei.sing.mahmi.cutter.CutterException.withMessage;
import static fj.P.lazy;

import java.security.cert.PKIXRevocationChecker.Option;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import lombok.AllArgsConstructor;
import lombok.val;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;

import org.expasy.mzjava.proteomics.mol.digest.CleavageSiteMatcher;
import org.expasy.mzjava.proteomics.mol.digest.Protease;
import org.expasy.mzjava.proteomics.mol.digest.ProteinDigester;

import es.uvigo.ei.sing.mahmi.common.entities.Digestion;
import es.uvigo.ei.sing.mahmi.common.entities.Enzyme;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.OptionExtensionMethods;

@Slf4j
@AllArgsConstructor(staticName = "proteinCutter")
@ExtensionMethod({ Option.class, OptionExtensionMethods.class })
public final class ProteinCutter {

    public Set<Digestion> cutProtein(
        final Protein            protein,
        final Collection<Enzyme> enzymes,
        final Predicate<Integer> sizeFilter
    ) throws CutterException {
        val digestions = new HashSet<Digestion>(enzymes.size());

        for (val enzyme : enzymes) {
            val ds = digest(protein, enzyme, sizeFilter);
            digestions.addAll(ds);
        }

        return digestions;
    }

    public Set<Digestion> cutProteins(
        final Collection<Protein> proteins,
        final Collection<Enzyme>  enzymes,
        final Predicate<Integer>  sizeFilter
    ) throws CutterException {
        val digestions = new HashSet<Digestion>(
            proteins.size() * enzymes.size()
        );

        for (val protein : proteins) {
            val ds = cutProtein(protein, enzymes, sizeFilter);
            digestions.addAll(ds);
        }

        return digestions;
    }


    private Set<Digestion> digest(
        final Protein            protein,
        final Enzyme             enzyme,
        final Predicate<Integer> sizeFilter
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

    private Map<Peptide, Long> cutAminoAcidSequence(
        final ProteinDigester    digester,
        final AminoAcidSequence  sequence,
        final Predicate<Integer> sizeFilter
    ) {
        val mzPeptides = digester.digest(toMzProtein(sequence));

        return mzPeptides.parallelStream()
            .filter(p -> sizeFilter.test(p.size()))
            .map(this::toAminoAcidSequence)
            .map(Peptide::peptide)
            .collect(frequencies());
    }

    private Set<Digestion> createDigestions(
        final Map<Peptide, Long> cuts,
        final Protein            protein,
        final Enzyme             enzyme
    ) {
        val digestions = new HashSet<Digestion>(cuts.size());

        for (val entry : cuts.entrySet()) {
            val peptide = entry.getKey();
            val counter = entry.getValue();

            digestions.add(digestion(protein, peptide, enzyme, counter));
        }

        return digestions;
    }


    @SuppressWarnings("deprecation")
    private org.expasy.mzjava.proteomics.mol.Protein toMzProtein(
        final AminoAcidSequence sequence
    ) {
        return new org.expasy.mzjava.proteomics.mol.Protein(
            "", sequence.toString()
        );
    }

    private AminoAcidSequence toAminoAcidSequence(
        final org.expasy.mzjava.proteomics.mol.Peptide mzPeptide
    ) {
        val sequence = mzPeptide.toSymbolString();
        return AminoAcidSequence.fromString(sequence).orThrow(
            lazy(u -> withMessage("Illegal aminoacid sequence produced by MZJava: " + sequence))
        );
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
