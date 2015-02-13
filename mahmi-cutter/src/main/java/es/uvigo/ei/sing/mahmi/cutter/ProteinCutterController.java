package es.uvigo.ei.sing.mahmi.cutter;

import java.util.concurrent.CompletableFuture;

import lombok.AllArgsConstructor;
import lombok.val;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;

import fj.F;
import fj.data.HashMap;
import fj.data.Set;
import fj.function.Try0;

import es.uvigo.ei.sing.mahmi.common.entities.Digestion;
import es.uvigo.ei.sing.mahmi.common.entities.Enzyme;
import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.Project;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.HashExtensionMethods;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableExtensionMethods;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.DigestionsDAO;
import es.uvigo.ei.sing.mahmi.database.daos.MetaGenomesDAO;
import es.uvigo.ei.sing.mahmi.database.daos.PeptidesDAO;
import es.uvigo.ei.sing.mahmi.database.daos.ProteinsDAO;
import es.uvigo.ei.sing.mahmi.database.utils.Table_Stats;

import static java.util.concurrent.CompletableFuture.runAsync;

import static es.uvigo.ei.sing.mahmi.common.utils.extensions.FutureExtensionMethods.sequence;

@Slf4j
@AllArgsConstructor(staticName = "proteinCutterCtrl")
@ExtensionMethod({ HashExtensionMethods.class, IterableExtensionMethods.class })
public final class ProteinCutterController {

    private final ProteinCutter  cutter;
    private final MetaGenomesDAO metaGenomesDAO;
    private final ProteinsDAO    proteinsDAO;
    private final PeptidesDAO    peptidesDAO;
    private final DigestionsDAO  digestionsDAO;
    private final Table_Stats    tableStats;

    public CompletableFuture<Void> cutProjectProteins(
        final Project     project,
        final Set<Enzyme> enzymes,
        final F<Integer, Boolean> sizeFilter
    ) {
        return runAsync(() -> {
            log.info("Cutting proteins of {} with {}", project, enzymes);

            metaGenomesDAO.forEachMetaGenomeOf(project, mg -> {
                cutMetaGenomeProteins(mg, enzymes, sizeFilter).join();
            });

            tableStats.updateStats(3, peptidesDAO.count());

            log.info("Finished cutting proteins of {}", project);
        });
     }

    public CompletableFuture<Void> cutMetaGenomeProteins(
        final MetaGenome  metaGenome,
        final Set<Enzyme> enzymes,
        final F<Integer, Boolean> sizeFilter
    ) {
        return runAsync(() -> {
            log.info("Cutting proteins of {} with {}", metaGenome, enzymes);

            final java.util.List<CompletableFuture<Void>> futures = new java.util.LinkedList<>();
            proteinsDAO.forEachProteinOf(metaGenome, proteins -> {
                futures.add(cutProteins(proteins, enzymes, sizeFilter));
            });

            sequence(futures).thenRun(
                () -> log.info("Finished cutting proteins of {}", metaGenome)
            );
        });
    }

    public CompletableFuture<Void> cutProteins(
        final Set<Protein> proteins,
        final Set<Enzyme>  enzymes,
        final F<Integer, Boolean> sizeFilter
    ) {
        return runAsync(() -> {
            log.info("Digesting {} proteins", proteins.size());

            final Set<Digestion> cuts = cutter.cutProteins(proteins, enzymes, sizeFilter);
            insertCuts(cuts);
        });
    }

    private void insertCuts(final Set<Digestion> digestions) {
        final Set<Peptide> peptides = digestions.map(
            Peptide.hash.toOrd(),
            d -> d.getPeptide()
        );

        insertDigestions(digestions, insertPeptides(peptides));
    }

    private Set<Peptide> insertPeptides(final Set<Peptide> peptides) {
        log.info("Inserting {} peptides in database", peptides.size());

        return databaseAction(() -> peptidesDAO.insertAll(peptides));
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
        final Set<Digestion> digestions,
        final Set<Peptide>   peptides
    ) {
        final HashMap<Peptide, Peptide> peptideMap = peptides.toIdentityMap(
            Peptide.equal, Peptide.hash
        );

        return digestions.map(digestions.ord(), d -> {
            final Peptide p =peptideMap.get(d.getPeptide()).some();
            return d.withPeptide(p);
        });
    }

    private <A> A databaseAction(final Try0<A, DAOException> f) {
        try {
            return f.f();
        } catch (final DAOException daoe) {
            throw CutterException.withCause(daoe);
        }
    }

}
