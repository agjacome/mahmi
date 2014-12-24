package es.uvigo.ei.sing.mahmi.cutter;

import static es.uvigo.ei.sing.mahmi.common.utils.extensions.CollectionExtensionMethods.setToMap;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import es.uvigo.ei.sing.mahmi.common.entities.Digestion;
import es.uvigo.ei.sing.mahmi.common.entities.Enzyme;
import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.Project;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.DigestionsDAO;
import es.uvigo.ei.sing.mahmi.database.daos.MetaGenomesDAO;
import es.uvigo.ei.sing.mahmi.database.daos.PeptidesDAO;
import es.uvigo.ei.sing.mahmi.database.daos.ProteinsDAO;
import fj.TryCatch0;

@Slf4j
@AllArgsConstructor(staticName = "proteinCutterCtrl")
public final class ProteinCutterController {

    private final ProteinCutter  cutter;
    private final MetaGenomesDAO metaGenomesDAO;
    private final ProteinsDAO    proteinsDAO;
    private final PeptidesDAO    peptidesDAO;
    private final DigestionsDAO  digestionsDAO;

    public CompletableFuture<Void> cutProjectProteins(
        final Project            project,
        final Collection<Enzyme> enzymes,
        final Predicate<Integer> sizeFilter
    ) {

        return runAsync(() -> metaGenomesDAO.forEachMetaGenomeOf(
            project, mgs -> cutMetaGenomesProteins(mgs, enzymes, sizeFilter)
        ));
    }


    private void cutMetaGenomesProteins(
        final Collection<MetaGenome> metaGenomes,
        final Collection<Enzyme>     enzymes,
        final Predicate<Integer>     sizeFilter
    ) {
        metaGenomes.forEach(metaGenome ->
            cutMetaGenomeProteins(metaGenome, enzymes, sizeFilter)
        );
    }

    private void cutMetaGenomeProteins(
        final MetaGenome         metaGenome,
        final Collection<Enzyme> enzymes,
        final Predicate<Integer> sizeFilter
    ) {
        proteinsDAO.forEachProteinOf(metaGenome, proteins ->
            cutProteins(proteins, enzymes, sizeFilter)
        );
    }

    private void cutProteins(
        final Collection<Protein> proteins,
        final Collection<Enzyme>  enzymes,
        final Predicate<Integer>  sizeFilter
    ) {
        log.info("Cutting {} proteins with {} enzymes", proteins.size(), enzymes.size());

        val cuts = cutter.cutProteins(proteins, enzymes, sizeFilter);
        insertCuts(cuts);

        System.gc();
    }

    private void insertCuts(final Set<Digestion> digestions) {
        val peptides = digestions.stream()
            .map(Digestion::getPeptide).collect(toSet());

        insertDigestions(digestions, insertPeptides(peptides));
    }

    private Set<Peptide> insertPeptides(final Set<Peptide> peptides) {
        log.info("Inserting {} peptides in database", peptides.size());

        return databaseAction(() ->
            new HashSet<>(peptidesDAO.insertAll(peptides))
        );
    }

    private void insertDigestions(
        final Set<Digestion> digestions,
        final Set<Peptide>   peptides
    ) {
        val updated = updateReferences(digestions, peptides);

        log.info("Inserting {} digestions in database", digestions.size());
        databaseAction(() -> digestionsDAO.insertAll(updated));
    }

    private Set<Digestion> updateReferences(
        final Collection<Digestion> digestions,
        final Collection<Peptide>   peptides
    ) {
        val peptideMap = setToMap(new HashSet<>(peptides));

        return digestions.stream().map(digestion -> {
            val peptide = peptideMap.get(digestion.getPeptide());
            return digestion.setPeptide(peptide);
        }).collect(toSet());
    }

    private <A> A databaseAction(final TryCatch0<A, DAOException> f) {
        try {
            return f.f();
        } catch (final DAOException daoe) {
            throw CutterException.withCause(daoe);
        }
    }

}
