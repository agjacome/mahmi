package es.uvigo.ei.sing.mahmi.mgloader.loaders;

import static es.uvigo.ei.sing.mahmi.common.entities.MetaGenome.metaGenome;
import static es.uvigo.ei.sing.mahmi.common.utils.extensions.CollectionsExtensionMethods.*;
import static es.uvigo.ei.sing.mahmi.common.utils.extensions.ValidationExtensionMethods.sequenceV;
import static java.util.stream.Collectors.toSet;
import static jersey.repackaged.com.google.common.collect.Sets.newHashSet;
import static jersey.repackaged.com.google.common.collect.Sets.union;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import es.uvigo.ei.sing.mahmi.common.entities.Identifier;
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
import fj.Unit;
import fj.data.Option;
import fj.data.Validation;

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


    public Validation<LoaderException, Project> loadProject(final Project project, final Path projectPath) {
        log.info("Loading {} from {}", project, projectPath);
        return insertProject(project).f().map(LoaderException::withCause).bind(
            p -> sequenceV(loadProjectFiles(p, projectPath)).map(us -> p)
        );
    }


    private Stream<Validation<LoaderException, Unit>> loadProjectFiles(final Project p, final Path path) {
        log.info("Loading Fasta files of {} from {}", p, path);
        return loader.loadProject(path)
            .map(v -> v.f().map(LoaderException::withCause))
            .map(v -> v.bind(fs -> insertProjectFastas(p, fs).f().map(LoaderException::withCause)));
    }

    private Validation<DAOException, Unit> insertProjectFastas(
        final Project project, final P2<GenomeFasta, ProteinFasta> fastas
    ) {
        log.info("Inserting Fastas of {} into database", project);
        return insertMetaGenome(metaGenome(project, fastas._1())).bind(
            metaGenome -> insertProteinFasta(metaGenome, fastas._2())
        );
    }

    private Validation<DAOException, Unit> insertProteinFasta(
        final MetaGenome metaGenome, final ProteinFasta proteinFasta
    ) {
        final Map<Protein, Integer> freqs = mapKeys(proteinFasta.getSequences(), Protein::protein);
        return getOrInsertAllProteins(freqs.keySet())
            .map(pSet -> setToIdentityMap(pSet))
            .map(pMap -> mapKeys(freqs, pMap::get))
            .bind(ps -> associateProteinsToMG(metaGenome, ps));
    }

    private Validation<DAOException, Project> insertProject(final Project project) {
        log.info("Inserting {} in database", project);
        return projectsDAO.insert(project);
    }

    private Validation<DAOException, MetaGenome> insertMetaGenome(final MetaGenome metaGenome) {
        log.info("Inserting {} in database", metaGenome);
        return metaGenomesDAO.insert(metaGenome);
    }

    private Validation<DAOException, Set<Protein>> getOrInsertAllProteins(final Set<Protein> proteins) {
        log.info("Inserting {} proteins into database", proteins.size());
        final Set<Protein> inserted = getExistingProteins(proteins);
        final Set<Protein> toInsert = proteins.parallelStream().filter(p -> !inserted.contains(p)).collect(toSet());

        return proteinsDAO.insertAll(toInsert).map(ps -> union(newHashSet(ps), inserted));
    }

    private Set<Protein> getExistingProteins(final Set<Protein> proteins) {
        // FIXME: this should be handled by DAO, it's just a temporal fix
        return proteins.parallelStream()
            .map(p -> proteinsDAO.getBySequence(p.getSequence()))
            .filter(v -> v.isSuccess() && v.exists(Option::isSome))
            .map(v -> v.success().some())
            .collect(toSet());
    }

    private Validation<DAOException, Unit> associateProteinsToMG(
        final MetaGenome metaGenome, final Map<Protein, Integer> proteins
    ) {
        log.info("Associating {} to {} proteins in database", metaGenome, proteins.size());
        final Map<Identifier, Long> pIds = mapMap(proteins, Protein::getId, Integer::longValue);
        return metaGenomesDAO.addAllProteinsToMetaGenome(metaGenome.getId(), pIds);
    }

}
