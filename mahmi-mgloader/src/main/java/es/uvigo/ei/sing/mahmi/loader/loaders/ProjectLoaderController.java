package es.uvigo.ei.sing.mahmi.loader.loaders;

import static es.uvigo.ei.sing.mahmi.common.entities.MetaGenome.metaGenome;
import static es.uvigo.ei.sing.mahmi.common.utils.extensions.CollectionsExtensionMethods.mapKeys;
import static es.uvigo.ei.sing.mahmi.common.utils.extensions.CollectionsExtensionMethods.setToIdentityMap;
import static java.util.concurrent.CompletableFuture.runAsync;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import es.uvigo.ei.sing.mahmi.common.entities.MetaGenome;
import es.uvigo.ei.sing.mahmi.common.entities.Project;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.fasta.GenomeFasta;
import es.uvigo.ei.sing.mahmi.common.entities.fasta.ProteinFasta;
import es.uvigo.ei.sing.mahmi.database.daos.DAOException;
import es.uvigo.ei.sing.mahmi.database.daos.MetaGenomesDAO;
import es.uvigo.ei.sing.mahmi.database.daos.ProjectsDAO;
import es.uvigo.ei.sing.mahmi.database.daos.ProteinsDAO;
import fj.P2;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProjectLoaderController {

    private final ProjectLoader  loader;
    private final ProjectsDAO    projectsDAO;
    private final MetaGenomesDAO metaGenomesDAO;
    private final ProteinsDAO    proteinsDAO;

    public static ProjectLoaderController projectLoaderCtrl(
        final ProjectLoader  projectLoader,
        final ProjectsDAO    projectsDAO,
        final MetaGenomesDAO metaGenomesDAO,
        final ProteinsDAO    proteinsDAO
    ) {
        return new ProjectLoaderController(projectLoader, projectsDAO, metaGenomesDAO, proteinsDAO);
    }


    public Project loadProject(final Project project, final Path projectPath) throws LoaderException {
        log.info("Loading {} from {}", project, projectPath);
        val insertedProject = insertProject(project);

        loadProjectFiles(insertedProject, projectPath);

        return insertedProject;
    }


    private void loadProjectFiles(final Project project, final Path path) throws LoaderException {
        runAsync(() -> {
            log.info("Start :: Loading Fasta files of {} from {}", project, path);
            loader.loadProject(path).forEach(paths -> insertProjectFastas(project, paths));
            log.info("Finish :: Loading Fasta files of {} from {}", project, path);
        });
    }

    private void insertProjectFastas(final Project project, final P2<GenomeFasta, ProteinFasta> fastas) throws LoaderException {
        log.info("Inserting Fastas of {} into database", project);
        MetaGenome metaGenome = metaGenome(project, fastas._1());
                   metaGenome = insertMetaGenome(metaGenome);

        insertProteinFasta(metaGenome, fastas._2());
    }

    private void insertProteinFasta(final MetaGenome metaGenome, final ProteinFasta proteinFasta) throws LoaderException {
        Map<Protein, Long> frequencies = mapKeys(proteinFasta.getSequences(), Protein::protein);
        val proteins = insertProteins(frequencies.keySet());

        frequencies = mapKeys(frequencies, setToIdentityMap(proteins)::get);
        addProteinsToMetaGenome(metaGenome, frequencies);
    }

    private Project insertProject(final Project project) throws LoaderException {
        try {
            log.info("Inserting {} in database", project);
            return projectsDAO.insert(project);
        } catch (final DAOException daoe) {
            log.error("Database error while inserting project", daoe);
            throw LoaderException.withCause(daoe);
        }
    }

    private MetaGenome insertMetaGenome(final MetaGenome metaGenome) throws LoaderException {
        try {
            log.info("Inserting {} in database", metaGenome);
            return metaGenomesDAO.insert(metaGenome);
        } catch (final DAOException daoe) {
            log.error("Database error while inserting metagenome", daoe);
            throw LoaderException.withCause(daoe);
        }
    }

    private Set<Protein> insertProteins(final Set<Protein> proteins) throws LoaderException {
        try {
            log.info("Inserting {} proteins into database", proteins.size());
            return proteinsDAO.insertAll(proteins);
        } catch (final DAOException daoe) {
            log.error("Database error while inserting proteins", daoe);
            throw LoaderException.withCause(daoe);
        }
    }

    private void addProteinsToMetaGenome(final MetaGenome metaGenome, final Map<Protein, Long> proteins) throws LoaderException {
        try {
            log.info("Associating {} proteins to {} in database", proteins.size(), metaGenome);
            val proteinIds = mapKeys(proteins, Protein::getId);
            metaGenomesDAO.addAllProteinsToMetaGenome(metaGenome.getId(), proteinIds);
        } catch (final DAOException daoe) {
            log.error("Database error while associating proteins to metagenome", daoe);
            throw LoaderException.withCause(daoe);
        }
    }

}