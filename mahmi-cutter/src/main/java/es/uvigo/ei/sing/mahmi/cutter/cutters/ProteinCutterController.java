package es.uvigo.ei.sing.mahmi.cutter.cutters;

import static es.uvigo.ei.sing.mahmi.common.utils.extensions.CollectionsExtensionMethods.setToIdentityMap;
import static java.util.stream.Collectors.toSet;

import java.util.Set;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import es.uvigo.ei.sing.mahmi.common.entities.Digestion;
import es.uvigo.ei.sing.mahmi.common.entities.Enzyme;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.DigestionsDAO;
import es.uvigo.ei.sing.mahmi.database.daos.PeptidesDAO;

@Slf4j
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


    public Set<Digestion> cutProtein(
        final Protein protein, final Set<Enzyme> enzymes, final int minSize, final int maxSize
    ) throws CutterException {
        val digestions = cutter.cutProtein(protein, enzymes, minSize, maxSize);
        return insertDigestionsWithPeptides(digestions);
    }

    public Set<Digestion> cutAllProteins(
        final Set<Protein> proteins, final Set<Enzyme> enzymes, final int minSize, final int maxSize
    ) throws CutterException {
        val digestions = cutter.cutAllProteins(proteins, enzymes, minSize, maxSize);
        return insertDigestionsWithPeptides(digestions);
    }


    private Set<Digestion> insertDigestionsWithPeptides(final Set<Digestion> digestions) throws CutterException {
        val oldPeptides = digestions.parallelStream().map(Digestion::getPeptide).collect(toSet());
        val newPeptides = insertPeptides(oldPeptides);

        val newDigestions = setDigestionsPeptides(digestions, newPeptides);
        return insertDigestions(newDigestions);
    }

    private Set<Peptide> insertPeptides(final Set<Peptide> peptides) throws CutterException {
        try {
            log.info("Inserting {} peptides into database", peptides);
            return peptidesDAO.insertAll(peptides);
        } catch (final DAOException daoe) {
            log.error("Database error while inserting {} peptides", peptides.size());
            throw CutterException.withCause(daoe);
        }
    }

    private Set<Digestion> insertDigestions(final Set<Digestion> digestions) throws CutterException {
        try {
            log.info("Inserting {} digestions into database", digestions);
            return digestionsDAO.insertAll(digestions);
        } catch (final DAOException daoe) {
            log.error("Database error while inserting {} digestions", digestions.size());
            throw CutterException.withCause(daoe);
        }
    }

    private Set<Digestion> setDigestionsPeptides(final Set<Digestion> digestions, final Set<Peptide> peptides) {
        val peptideMap = setToIdentityMap(peptides);

        return digestions.parallelStream().map(
            d -> d.withPeptide(peptideMap.get(d.getPeptide()))
        ).collect(toSet());
    }

}
