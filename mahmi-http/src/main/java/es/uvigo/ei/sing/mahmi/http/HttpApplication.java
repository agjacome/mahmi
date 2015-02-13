package es.uvigo.ei.sing.mahmi.http;

import java.util.Set;

import javax.ws.rs.core.Application;

import jersey.repackaged.com.google.common.collect.Sets;

import lombok.AllArgsConstructor;

import es.uvigo.ei.sing.mahmi.cutter.ProteinCutterController;
import es.uvigo.ei.sing.mahmi.database.connection.ConnectionPool;
import es.uvigo.ei.sing.mahmi.database.daos.DigestionsDAO;
import es.uvigo.ei.sing.mahmi.database.daos.EnzymesDAO;
import es.uvigo.ei.sing.mahmi.database.daos.MetaGenomeProteinsDAO;
import es.uvigo.ei.sing.mahmi.database.daos.MetaGenomesDAO;
import es.uvigo.ei.sing.mahmi.database.daos.PeptidesDAO;
import es.uvigo.ei.sing.mahmi.database.daos.ProjectsDAO;
import es.uvigo.ei.sing.mahmi.database.daos.ProteinsDAO;
import es.uvigo.ei.sing.mahmi.database.utils.Table_Stats;
import es.uvigo.ei.sing.mahmi.loader.ProjectLoaderController;

import static es.uvigo.ei.sing.mahmi.cutter.ProteinCutter.proteinCutter;
import static es.uvigo.ei.sing.mahmi.cutter.ProteinCutterController.proteinCutterCtrl;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLDigestionsDAO.mysqlDigestionsDAO;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLEnzymesDAO.mysqlEnzymesDAO;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLMetaGenomeProteinsDAO.mysqlMetaGenomeProteinsDAO;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLMetaGenomesDAO.mysqlMetaGenomesDAO;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLPeptidesDAO.mysqlPeptidesDAO;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLProjectsDAO.mysqlProjectsDAO;
import static es.uvigo.ei.sing.mahmi.database.daos.mysql.MySQLProteinsDAO.mysqlProteinsDAO;
import static es.uvigo.ei.sing.mahmi.database.utils.Table_Stats.tableStats;
import static es.uvigo.ei.sing.mahmi.http.services.DigestionService.digestionService;
import static es.uvigo.ei.sing.mahmi.http.services.EnzymeService.enzymeService;
import static es.uvigo.ei.sing.mahmi.http.services.MetaGenomeProteinsService.metaGenomeProteinsService;
import static es.uvigo.ei.sing.mahmi.http.services.MetaGenomeService.metaGenomeService;
import static es.uvigo.ei.sing.mahmi.http.services.PeptideService.peptideService;
import static es.uvigo.ei.sing.mahmi.http.services.ProjectService.projectService;
import static es.uvigo.ei.sing.mahmi.http.services.ProteinService.proteinService;
import static es.uvigo.ei.sing.mahmi.loader.MGRastProjectLoader.mgRastLoader;
import static es.uvigo.ei.sing.mahmi.loader.ProjectLoaderController.projectLoaderCtrl;

@AllArgsConstructor(staticName = "httpApplication")
public final class HttpApplication extends Application {

    private final ConnectionPool connectionPool;

    @Override
    public Set<Object> getSingletons() {
        final DigestionsDAO         digestionsDAO         = mysqlDigestionsDAO(connectionPool);
        final EnzymesDAO            enzymesDAO            = mysqlEnzymesDAO(connectionPool);
        final MetaGenomesDAO        metaGenomesDAO        = mysqlMetaGenomesDAO(connectionPool);
        final MetaGenomeProteinsDAO metaGenomeProteinsDAO = mysqlMetaGenomeProteinsDAO(connectionPool);
        final PeptidesDAO           peptidesDAO           = mysqlPeptidesDAO(connectionPool);
        final ProjectsDAO           projectsDAO           = mysqlProjectsDAO(connectionPool);
        final ProteinsDAO           proteinsDAO           = mysqlProteinsDAO(connectionPool);
        final Table_Stats           tableStats            = tableStats(connectionPool);

        final ProjectLoaderController loaderController = projectLoaderCtrl(
            mgRastLoader(), projectsDAO, metaGenomesDAO, proteinsDAO, tableStats
        );

        final ProteinCutterController cutterController = proteinCutterCtrl(
            proteinCutter(), metaGenomesDAO, proteinsDAO, peptidesDAO, digestionsDAO, tableStats
        );

        return Sets.newHashSet(
            (Object) digestionService(digestionsDAO, cutterController),
            (Object) enzymeService(enzymesDAO),
            (Object) metaGenomeService(metaGenomesDAO),
            (Object) metaGenomeProteinsService(metaGenomeProteinsDAO),
            (Object) peptideService(peptidesDAO),
            (Object) projectService(projectsDAO, loaderController),
            (Object) proteinService(proteinsDAO)
        );
    }

}
