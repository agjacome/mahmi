package es.uvigo.ei.sing.mahmi.cutter.cutters;

import es.uvigo.ei.sing.mahmi.common.entities.Digestion;
import es.uvigo.ei.sing.mahmi.common.entities.Enzyme;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.DigestionsDAO;
import es.uvigo.ei.sing.mahmi.database.daos.PeptidesDAO;
import fj.data.Option;
import fj.data.Validation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Set;

import static jersey.repackaged.com.google.common.collect.Sets.newHashSet;
import static jersey.repackaged.com.google.common.collect.Sets.union;
import static es.uvigo.ei.sing.mahmi.common.utils.extensions.CollectionsExtensionMethods.setToIdentityMap;
import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProteinCutterController {

    // NOTE: always assume that received enzymes and proteins are already
    // inserted in DB, and have valid identifiers. A CutterException is
    // returned if that expectation does not hold already, because the
    // DAOException that the DigestionsDAO will return is transformed into it.

    private final ProteinCutter cutter;
    private final PeptidesDAO   peptidesDAO;
    private final DigestionsDAO digestionsDAO;

    public static ProteinCutterController proteinCutterCtrl(
        final ProteinCutter cutter, final PeptidesDAO peptidesDAO, final DigestionsDAO digestionsDAO
    ) {
        return new ProteinCutterController(cutter, peptidesDAO, digestionsDAO);
    }

    public Validation<CutterException, Set<Digestion>> cutProtein(
        final Protein protein, final Set<Enzyme> enzymes, final int minSize, final int maxSize
    ) {
        return getOrInsertAllDigestions(
            cutter.cutProtein(protein, enzymes, minSize, maxSize)
        ).f().map(CutterException::withCause);
    }

    public Validation<CutterException, Set<Digestion>> cutAllProteins(
        final Set<Protein> proteins, final Set<Enzyme> enzymes, final int minSize, final int maxSize
    ) {
        return getOrInsertAllDigestions(
            cutter.cutAllProteins(proteins, enzymes, minSize, maxSize)
        ).f().map(CutterException::withCause);
    }

    private Validation<DAOException, Set<Peptide>> getOrInsertAllPeptides(final Set<Peptide> peptides) {
        final Set<Peptide> inserted = getExistingPeptides(peptides);
        final Set<Peptide> toInsert = peptides.parallelStream().filter(p -> !inserted.contains(p)).collect(toSet());

        return peptidesDAO.insertAll(toInsert).map(itps -> union(newHashSet(itps), inserted));
    }

    private Set<Peptide> getExistingPeptides(final Set<Peptide> peptides) {
        // FIXME: this should be handled by DAO, it's just a temporal fix
        return peptides.parallelStream()
            .map(p -> peptidesDAO.getBySequence(p.getSequence()))
            .filter(v -> v.isSuccess() && v.exists(Option::isSome))
            .map(v -> v.success().some())
            .collect(toSet());
    }

    private Validation<DAOException, Set<Digestion>> getOrInsertAllDigestions(final Set<Digestion> digestions) {
        final Set<Peptide> peptides = digestions.parallelStream().map(Digestion::getPeptide).collect(toSet());

        return getOrInsertAllPeptides(peptides).map(ps -> replacePeptides(digestions, ps)).bind(ds -> {
            final Set<Digestion> inserted = getExistingDigestions(ds);
            final Set<Digestion> toInsert = ds.parallelStream().filter(d -> !inserted.contains(d)).collect(toSet());

            return digestionsDAO.insertAll(toInsert).map(itds -> union(newHashSet(itds), inserted));
        });
    }

    private Set<Digestion> getExistingDigestions(final Set<Digestion> digestions) {
        // FIXME: this should be handled by DAO, it's just a temporal fix
        return digestions.parallelStream()
            .map(d -> digestionsDAO.get(d.getEnzyme().getId(), d.getProtein().getId(), d.getPeptide().getId()))
            .filter(v -> v.isSuccess() && v.exists(Option::isSome))
            .map(v -> v.success().some())
            .collect(toSet());
    }

    private Set<Digestion> replacePeptides(final Set<Digestion> digestions, final Set<Peptide> peptides) {
        final Map<Peptide, Peptide> peptideMap = setToIdentityMap(peptides);

        return digestions.parallelStream().map(
            digestion -> digestion.withPeptide(peptideMap.get(digestion.getPeptide()))
        ).collect(toSet());
    }

}
