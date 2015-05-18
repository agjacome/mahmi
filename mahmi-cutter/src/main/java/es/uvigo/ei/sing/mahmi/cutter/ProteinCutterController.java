package es.uvigo.ei.sing.mahmi.cutter;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import lombok.AllArgsConstructor;
import lombok.val;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;

import fj.F;
import fj.data.Set;
import fj.function.Try0;

import es.uvigo.ei.sing.mahmi.common.entities.Digestion;
import es.uvigo.ei.sing.mahmi.common.entities.Enzyme;
import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.Peptide;
import es.uvigo.ei.sing.mahmi.common.entities.Project;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.HashExtensionMethods;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableExtensionMethods;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.DigestionsDAO;
import es.uvigo.ei.sing.mahmi.database.daos.MetaGenomesDAO;
import es.uvigo.ei.sing.mahmi.database.daos.PeptidesDAO;
import es.uvigo.ei.sing.mahmi.database.daos.ProteinsDAO;
import es.uvigo.ei.sing.mahmi.database.daos.TableStatsDAO;

import static java.util.concurrent.CompletableFuture.runAsync;

import static es.uvigo.ei.sing.mahmi.common.entities.TableStat.tableStat;

@Slf4j
@AllArgsConstructor(staticName = "proteinCutterCtrl")
@ExtensionMethod({ HashExtensionMethods.class, IterableExtensionMethods.class })
public final class ProteinCutterController {

    private final ProteinCutter  cutter;
    private final MetaGenomesDAO metaGenomesDAO;
    private final ProteinsDAO    proteinsDAO;
    private final PeptidesDAO    peptidesDAO;
    private final DigestionsDAO  digestionsDAO;
    private final TableStatsDAO  tableStatsDAO;

    public CompletableFuture<Void> cutProjectProteins(
        final Project     project,
        final Set<Enzyme> enzymes,
        final F<Integer, Boolean> sizeFilter
    ) {
        return runAsync(() -> {
            log.info("Cutting proteins of {} with {}", project, enzymes);

            metaGenomesDAO.forEachMetaGenomeOf(project, metagenome ->
                cutMetaGenomeProteins(metagenome, enzymes, sizeFilter)
            );

            tableStatsDAO.update(tableStat(Identifier.of(4),"", peptidesDAO.count()));

            log.info("Finished cutting proteins of {}", project);
        });
     }

    private void cutMetaGenomeProteins(
        final MetaGenome  metagenome,
        final Set<Enzyme> enzymes,
        final F<Integer, Boolean> sizeFilter
    ) {
        log.info("Cutting proteins of {} with {}", metagenome, enzymes);

        proteinsDAO.forEachProteinOf(
            metagenome, proteins -> cutProteins(proteins, enzymes, sizeFilter)
        );

        log.info("Finished cutting proteins of {}", metagenome);
    }

    private void cutProteins(
        final Set<Protein> proteins,
        final Set<Enzyme>  enzymes,
        final F<Integer, Boolean> sizeFilter
    ) {
        log.info("Digesting {} proteins", proteins.size());

        final Set<Digestion> cuts = cutter.cutProteins(proteins, enzymes, sizeFilter);
        insertCuts(cuts);
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

    // FIXME: quickfixed, commented error-causing code
    private Set<Digestion> updateReferences(
        final Set<Digestion> digestions,
        final Set<Peptide>   peptides
    ) {
        final java.util.List<Peptide> ps = new ArrayList<>(peptides.toCollection());

        Set<Digestion> set = Set.empty(Digestion.hash.toOrd());

        for (final Digestion dig: digestions) {
            final Peptide peptideWithID = ps.stream().filter(
                p -> Peptide.equal.eq(dig.getPeptide(), p)
            ).findFirst().get();

            final Digestion digestionWithPeptideID = dig.withPeptide(peptideWithID);
            set = set.insert(digestionWithPeptideID);
        }

        return set;

        // final HashMap<Peptide, Peptide> peptideMap = peptides.toIdentityMap(
        //     Peptide.equal, Peptide.hash
        // );
        //
        // return digestions.map(digestions.ord(), dig -> {
        //     val p = peptideMap.get(dig.getPeptide()).some();
        //     return dig.withPeptide(p);
        // });
    }

    private <A> A databaseAction(final Try0<A, DAOException> f) {
        try {
            return f.f();
        } catch (final DAOException daoe) {
            throw CutterException.withCause(daoe);
        }
    }

}
