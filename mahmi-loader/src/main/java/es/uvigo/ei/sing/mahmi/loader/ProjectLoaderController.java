package es.uvigo.ei.sing.mahmi.loader;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import lombok.AllArgsConstructor;
import lombok.val;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;

import fj.P2;
import fj.data.HashMap;
import fj.data.Set;

import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.Project;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleobaseSequence;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.HashExtensionMethods;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableExtensionMethods;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.MetaGenomesDAO;
import es.uvigo.ei.sing.mahmi.database.daos.ProjectsDAO;
import es.uvigo.ei.sing.mahmi.database.daos.ProteinsDAO;
import es.uvigo.ei.sing.mahmi.database.utils.Table_Stats;

import static java.util.concurrent.CompletableFuture.runAsync;

import static fj.P.p;
import static fj.Unit.unit;

import static es.uvigo.ei.sing.mahmi.common.entities.MetaGenome.metagenome;

@Slf4j
@AllArgsConstructor(staticName = "projectLoaderCtrl")
@ExtensionMethod({ HashExtensionMethods.class, IterableExtensionMethods.class })
public final class ProjectLoaderController {

    private final ProjectLoader  loader;
    private final ProjectsDAO    projectsDAO;
    private final MetaGenomesDAO metaGenomesDAO;
    private final ProteinsDAO    proteinsDAO;
    private final Table_Stats    tableStats;


    public P2<Project, CompletableFuture<Void>> loadProject(
        final Project project, final Path projectPath
    ) throws LoaderException {
        val inserted = insertProject(project);
        val future   = runAsync(() -> loadProjectFiles(inserted, projectPath));

        return p(inserted, future);
    }

    public void loadFastas(
        final Project                   project,
        final Fasta<NucleobaseSequence> genomeFasta,
        final Fasta<AminoAcidSequence>  proteinFasta
    ) throws LoaderException {
        val metaGenome = insertMetaGenome(metagenome(project, genomeFasta));
        loadProteinFasta(metaGenome, proteinFasta);

        System.gc();
    }


    private void loadProjectFiles(final Project project, final Path path) {
        log.info("Started loading Fasta files of {} from {}", project, path);

        val projectFiles = loader.loadProject(path);
        projectFiles.forEach(paths -> loadFastas(project, paths._1(), paths._2()));

        tableStats.updateStats(1, projectsDAO.count());
        tableStats.updateStats(2, metaGenomesDAO.count());
        tableStats.updateStats(3, proteinsDAO.count());
        
        log.info("Finished loading Fasta files of {} from {}", project, path);
    }

    private void loadProteinFasta(
        final MetaGenome metaGenome, final Fasta<AminoAcidSequence> fasta
    ) {
        final HashMap<Protein, Long> frequencies = fasta.toStream()
            .map(Protein::protein)
            .frequencies(Protein.equal, Protein.hash);

        final Set<Protein> toInsert = frequencies.keys().toSet(Protein.hash.toOrd());
        final HashMap<Protein, Long> inserted = insertProteins(toInsert).toIdentityMap(
            Protein.equal, Protein.hash
        ).mapValues(p -> frequencies.get(p).some());

        insertMetaGenomeProteins(metaGenome, inserted);
    }


    private Project insertProject(final Project project) {
        log.info("Inserting {} into database", project);
        return daoAction(() -> projectsDAO.insert(project));
    }

    private MetaGenome insertMetaGenome(final MetaGenome metaGenome) {
        log.info("Inserting {} into database", metaGenome);
        return daoAction(() -> metaGenomesDAO.insert(metaGenome));
    }

    private Set<Protein> insertProteins(final Set<Protein> proteins) {
        log.info("Inserting {} proteins into database", proteins.size());
        return daoAction(() -> proteinsDAO.insertAll(proteins));
    }

    private void insertMetaGenomeProteins(
        final MetaGenome metaGenome, final HashMap<Protein, Long> proteins
    ) {
        log.info("Associating {} proteins to {} in database", proteins.size(), metaGenome);
        daoAction(() -> {
            metaGenomesDAO.addProteins(metaGenome, proteins);
            return unit();
        });
    }

    private <A> A daoAction(final Supplier<A> daoF) throws LoaderException {
        try {
            return daoF.get();
        } catch (final DAOException daoe) {
            throw LoaderException.withCause(daoe);
        }
    }

}
